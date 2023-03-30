package com.att.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"RuleTemplate", "RebootRequest"})
public class DecisionServiceRequest {

    private RuleTemplate ruleTemplate;
    private Reboot rebootRequest;

    public DecisionServiceRequest() {
    } 

    @JsonProperty("RuleTemplate")
    public RuleTemplate getRuleTemplate() {
        return this.ruleTemplate;
    }

    public void setRuleTemplate(RuleTemplate ruleTemplate) {
        this.ruleTemplate = ruleTemplate;
    }

    @JsonProperty("RebootRequest")
    public Reboot getRebootRequest() {
        return this.rebootRequest;
    }

    public void setRebootRequest(Reboot rebootRequest) {
        this.rebootRequest = rebootRequest;
    }

    @Override
    public String toString() {
        return "{" + "ruleTemplate:" + getRuleTemplate() + " rebootRequest:" + getRebootRequest() + "}";
    }
}

