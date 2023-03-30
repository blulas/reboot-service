package com.att.infrastructure.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import java.io.InputStream;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import static org.springframework.http.ResponseEntity.ok;

import com.att.infrastructure.model.DecisionServiceRequest;
import com.att.infrastructure.model.RebootRequest;
import com.att.infrastructure.model.RebootResponse;
import com.att.infrastructure.model.Reboot;
import com.att.infrastructure.model.RuleTemplate;
import com.att.infrastructure.model.RuleTemplateDataList;

import com.att.infrastructure.model.Config;
import com.att.infrastructure.model.Summary;
import com.att.infrastructure.model.Details;
import com.att.infrastructure.model.RebootResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController()
public class ServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private IRebootService rebootService;

    @PostMapping("/reboot")
    public ResponseEntity<RebootResponse[]> reboot(@RequestBody RebootRequest request) {

        List<RebootResponse> list = rebootService.reboot(request);
        return ResponseEntity.ok(list.toArray(new RebootResponse[list.size()]));
    }
    
    @CrossOrigin
    @GetMapping("/config/{ruleID}")
    public ResponseEntity<RuleTemplateDataList> getConfig(@PathVariable String ruleID) {
        return new ResponseEntity<>(rebootService.getConfig(ruleID), HttpStatus.OK);
    }
    
    @CrossOrigin
    @GetMapping("/summary")
    public ResponseEntity<Summary[]> getSummary() {

        List<Summary> list = rebootService.getSummary();
        return ResponseEntity.ok(list.toArray(new Summary[list.size()]));
    }
    
    @CrossOrigin
    @GetMapping("/details/{host}")
    public ResponseEntity<Details[]> getDetails(@PathVariable String host) {

        List<Details> list = rebootService.getDetails(host);
        return ResponseEntity.ok(list.toArray(new Details[list.size()]));
    }
    
    @CrossOrigin
    @PostMapping("/updateConfig")
    public ResponseEntity<String> updateConfig(@RequestBody RuleTemplate ruleTemplate) {

        rebootService.updateConfig(ruleTemplate);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/determineRebootRequirements/{showRules}")
    public ResponseEntity<RebootResponse> determineRebootRequirements(@RequestBody DecisionServiceRequest request, @PathVariable boolean showRules) {

        Reboot reboot = rebootService.determineRebootRequirements(request, showRules);
        RebootResponse response = new RebootResponse(reboot.getHost(), reboot.getRestart(), reboot.getRestartCount(), reboot.getAlertOps());
        LOGGER.info("Rule results: " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}