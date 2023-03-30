package com.att.infrastructure.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTemplateData {                                    

    private String name;
    private String value;

    public RuleTemplateData() {
    }

    public RuleTemplateData(String name, String value) {

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + "name:" + getName() + " value:" + getValue() + "}";
    }
}