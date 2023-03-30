package com.att.infrastructure.data;

import java.util.List;
import java.sql.Timestamp;

import com.att.infrastructure.model.Config;
import com.att.infrastructure.model.Summary;
import com.att.infrastructure.model.Details;
import com.att.infrastructure.model.RebootResponse;

public interface IDataAccess {

    public Config getConfig(String ruleID);
    public void updateConfig(Config config);
    public List<Summary> getSummary(); 
    public List<Summary> getSummary(String hostID); 
    public void insertSummary(RebootResponse response, Timestamp currentDateAndTime); 
    public void updateSummary(RebootResponse response, Timestamp currentDateAndTime); 
    public List<Details> getDetails(String hostID); 
    public void insertDetails(Details details); 
}