package com.att.infrastructure.model;

import java.util.ArrayList;

public class RuleTemplateDataList extends ArrayList<RuleTemplateData> {

    public RuleTemplateData findByName(String name) {

        for (RuleTemplateData templateData : this) {
            if (name.equals(templateData.getName())) {
                return templateData;
            }
        }

        return null;
    }
}