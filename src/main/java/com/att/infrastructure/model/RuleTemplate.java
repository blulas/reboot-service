package com.att.infrastructure.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Name", "Template", "TemplateData"})
public class RuleTemplate {                                    

    private String name;
    private String template;
    private RuleTemplateDataList templateData;

    public RuleTemplate() {
    }

    @JsonProperty("Name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Template")
    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty("TemplateData")
    public RuleTemplateDataList getTemplateData() {
        return this.templateData;
    }

    public void setTemplateData(RuleTemplateDataList templateData) {
        this.templateData = templateData;
    }
    
    @Override
    public String toString() {
        return "{" + "name:" + getName() + " template:" + getTemplate() + " templateData:" + getTemplateData() + "}";
    }
}