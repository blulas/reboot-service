package com.att.infrastructure.service;

import java.util.List;

import com.att.infrastructure.model.DecisionServiceRequest;
import com.att.infrastructure.model.RebootRequest;
import com.att.infrastructure.model.RebootResponse;
import com.att.infrastructure.model.Reboot;
import com.att.infrastructure.model.RuleTemplate;
import com.att.infrastructure.model.RuleTemplateDataList;
import com.att.infrastructure.model.Summary;
import com.att.infrastructure.model.Details;

public interface IRebootService {

    public List<RebootResponse> reboot(RebootRequest request);
    public RuleTemplateDataList getConfig(String ruleID);
    public List<Summary> getSummary();
    public List<Details> getDetails(String hostID);
    public void updateConfig(RuleTemplate ruleTemplate); 
    public Reboot determineRebootRequirements(DecisionServiceRequest request, boolean showRules);
}