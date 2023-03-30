package com.att.infrastructure.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.att.infrastructure.model.DecisionServiceRequest;
import com.att.infrastructure.model.RuleTemplate;
import com.att.infrastructure.model.RuleTemplateData;
import com.att.infrastructure.model.RebootRequest;
import com.att.infrastructure.model.RebootResponse;
import com.att.infrastructure.model.Reboot;
import com.att.infrastructure.model.Host;
import com.att.infrastructure.model.ExecutionDuration;
import com.att.infrastructure.model.RuleTemplateDataList;

import com.att.infrastructure.data.IDataAccess;
import com.att.infrastructure.data.DataAccess;
import com.att.infrastructure.model.Config;
import com.att.infrastructure.model.Summary;
import com.att.infrastructure.model.Details;

import org.kie.api.runtime.KieSession;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.utils.KieHelper;
import org.drools.template.ObjectDataCompiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RebootService implements IRebootService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebootService.class);

    private static final String RULE_ID = "reboot";
    private static final String TEMPLATE = "/RebootRequired.drt";
    private static final String TEMPLATE_MINUTES = "templateMinutes";
    private static final String TEMPLATE_COUNT = "templateCount";
    private static final int DEFAULT_TEMPLATE_MINUTES = 5; 
    private static final int DEFAULT_TEMPLATE_COUNT = 2;
    private static final String RESTART_NO = "No";
    private static final String RESTART_YES = "Yes";

    @Autowired private Environment env;
    @Autowired private IDataAccess dataAccess;
	private KieSession kieSession;

    @PreDestroy
    public void onDestroy() throws Exception {

        this.kieSession.dispose();
        this.kieSession.destroy();
    }

    @Override
    public List<RebootResponse> reboot(RebootRequest request) {

        List<RebootResponse> list = new ArrayList<RebootResponse>();
        for (Host host : request.getHosts()) {

            Reboot rebootRequest = new Reboot();

            // Get the latest request for this host
            List<Summary> summary = dataAccess.getSummary(host.getHost());

            // Note the current date and time
            long now = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(now);

            // Call the rules for each host reboot request
            DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
            int restartCount;
            
            RuleTemplate ruleTemplate = new RuleTemplate();
            ruleTemplate.setName(RULE_ID);
            ruleTemplate.setTemplate(TEMPLATE);

            // Get the rule template data from the database
            RuleTemplateDataList config = getConfig(RULE_ID);
            ruleTemplate.setTemplateData(config);
            decisionServiceRequest.setRuleTemplate(ruleTemplate);

            // Calculate the time difference between reboot requests (if any)
            if (summary.size() < 1) {

                restartCount = 1;
                rebootRequest.setHost(host.getHost());
                rebootRequest.setRestartCount(restartCount);
                rebootRequest.setRestartedMin(0);
                decisionServiceRequest.setRebootRequest(rebootRequest);

            } else {

                Summary s = summary.get(0);

                // Calculate the duration, in minutes, between now and the last reboot request time
                long durationInMinutes = calculateMinutesBetweenTimestamps(currentTimestamp, s.getLastRebootTime());
                LOGGER.info("Reboot Request: host=" + s.getHost() + ", lastRebootTime=" + (Date) s.getLastRebootTime() + ", currentRebootTime=" + (Date) currentTimestamp + ", timeDifference (in minutes)=" + durationInMinutes);

                // Set the input data to the rules
                restartCount = s.getRestartCount();
                rebootRequest.setHost(host.getHost());
                rebootRequest.setRestartCount((restartCount+1));
                rebootRequest.setRestartedMin((int) durationInMinutes);
                decisionServiceRequest.setRebootRequest(rebootRequest);
            }

            // Call the rules
            Reboot rebootResponse = determineRebootRequirements(decisionServiceRequest, false);
            RebootResponse response = new RebootResponse(rebootResponse.getHost(), rebootResponse.getRestart(), rebootResponse.getRestartCount(), rebootResponse.getAlertOps());

            // Only persist the summary if restart=yes
            if (response.getRestart().equals(RESTART_NO)) { 
            	response.setRestartCount(restartCount);
            } else {

                if (summary.size() < 1) {
                    dataAccess.insertSummary(response, currentTimestamp);
                } else {
                    dataAccess.updateSummary(response, currentTimestamp);
                }
            }
            
            // Add the entry to the audit tables
            Details details = new Details();
            details.setHostID(host.getHost());
            details.setMaxRestartCount(response.getRestartCount());
            details.setRequestTime(currentTimestamp);
            details.setAlertOps(response.getAlertOps());
            details.setRestart(response.getRestart());
            dataAccess.insertDetails(details);

            // Return the results
            list.add(response);
        }

        return list;
    }

    @Override
    public RuleTemplateDataList getConfig(String ruleID) {

        RuleTemplateDataList list = new RuleTemplateDataList();
        Config config = dataAccess.getConfig(ruleID);
        
        list.add(new RuleTemplateData(TEMPLATE_MINUTES, String.valueOf(config.getMinutes())));
        list.add(new RuleTemplateData(TEMPLATE_COUNT, String.valueOf(config.getCount())));

        return list;
    }

    @Override
    public List<Summary> getSummary() {

        List<Summary> list = dataAccess.getSummary();
        return list;
    }

    @Override
    public List<Details> getDetails(String hostID) {

        List<Details> list = new ArrayList<Details>();
        return list = dataAccess.getDetails(hostID);
    }

    @Override
    public void updateConfig(RuleTemplate ruleTemplate) {

        RuleTemplateData minutes = ruleTemplate.getTemplateData().findByName(TEMPLATE_MINUTES);
        RuleTemplateData count = ruleTemplate.getTemplateData().findByName(TEMPLATE_COUNT);

        Config config = new Config();
        config.setRuleID(ruleTemplate.getName());
        config.setMinutes(Integer.parseInt(minutes.getValue()));
        config.setCount(Integer.parseInt(count.getValue()));

        dataAccess.updateConfig(config);
    }

    @Override
    public Reboot determineRebootRequirements(DecisionServiceRequest request, boolean showRules) {

        Reboot response = new Reboot();
        LocalDateTime start = LocalDateTime.now();

        try {

            // Read the template from the resources folder
            Class thisClass = Class.forName("com.att.infrastructure.service.ServiceController");            
       		InputStream template = thisClass.getResourceAsStream(request.getRuleTemplate().getTemplate());
            
            if (template == null) {
                LOGGER.error("The template cannot be found!");
            }

            // Process the template data
            List<Reboot> ruleTemplateData = new ArrayList<Reboot>();
            if (request.getRuleTemplate().getTemplateData() != null) {

                Reboot rebootTemplate = new Reboot(new Integer(1), new Integer(2));
                RuleTemplateData templateMinutes = request.getRuleTemplate().getTemplateData().findByName(TEMPLATE_MINUTES);
                RuleTemplateData templateCount = request.getRuleTemplate().getTemplateData().findByName(TEMPLATE_COUNT);

                if (templateMinutes != null && templateCount != null) {

                    LOGGER.info("Setting rule template values: templateMinutes=" + templateMinutes.getValue() + ", templateCount=" + templateCount.getValue());
                    rebootTemplate.setTemplateMinutes(Integer.parseInt(templateMinutes.getValue()));
                    rebootTemplate.setTemplateCount(Integer.parseInt(templateCount.getValue()));
                    ruleTemplateData.add(rebootTemplate);
                }
            } else {

                // Default values in case no template passed or error in template
                Reboot rebootTemplate = new Reboot(DEFAULT_TEMPLATE_MINUTES, DEFAULT_TEMPLATE_COUNT);
                ruleTemplateData.add(rebootTemplate);
            }

            // Produce the rules from the template
            ObjectDataCompiler dataCompiler = new ObjectDataCompiler();
		    String drl = dataCompiler.compile(ruleTemplateData, template);

            // Show the generated rules in the logs?
            if (showRules) {
                LOGGER.info("Generated rules: \r\r\n" + drl);
            }

            // Create and load the session            
            kieSession = getKieSessionByDrl(drl);
            kieSession.insert(request.getRebootRequest());

            // Execute the rules
            LOGGER.info("Inserting data into ruleset: " + request.getRebootRequest() + ", and executing rules...");
    		kieSession.fireAllRules();

            LocalDateTime end = LocalDateTime.now();
            ExecutionDuration ed = calculateExecutionDuration(start, end);
            LOGGER.info("Rule execution was completed in: " + ed.toString());

            // Process the results
            response = request.getRebootRequest();

        } catch (Exception e) {
            LOGGER.error("Exception: message=" + e.getMessage());
        }

        return response;
    }

    protected KieSession getKieSessionByDrl(String drlContent) throws Exception {

		KieHelper kieHelper = new KieHelper();
		kieHelper.addContent(drlContent, ResourceType.DRL);

		Results verify = kieHelper.verify();
		boolean hasMessages = verify.hasMessages(Message.Level.WARNING, Message.Level.ERROR);

		if (hasMessages) {

			List<Message> messages = verify.getMessages(Message.Level.WARNING, Message.Level.ERROR);
			for (Message message : messages) {
				LOGGER.error("ERROR : " + message.getText());
			}

			throw new RuntimeException("verify has errors!");
		}

		return kieHelper.build().newKieSession();
	}

    protected ExecutionDuration calculateExecutionDuration(LocalDateTime begin, LocalDateTime end) {

        ExecutionDuration ed = new ExecutionDuration();
        Timestamp beginTS = Timestamp.valueOf(begin);
        Timestamp endTS = Timestamp.valueOf(end);

        ed.setMilliseconds(endTS.getTime() - beginTS.getTime());
        int seconds = (int) ed.getMilliseconds() / 1000; 
        ed.setHours(seconds / 3600);
        ed.setMinutes((seconds % 3600) / 60);
        ed.setSeconds((seconds % 3600) % 60);

        return ed;
    }

    public static long calculateMinutesBetweenTimestamps(Timestamp currentTime, Timestamp oldTime) {

        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        long diff = milliseconds2 - milliseconds1;
        return diff / (60 * 1000);
    }
}