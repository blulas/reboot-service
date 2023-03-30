package com.att.infrastructure.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.att.infrastructure.model.RebootResponse;
import com.att.infrastructure.model.Details;
import com.att.infrastructure.model.Summary;
import com.att.infrastructure.model.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataAccess implements IDataAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAccess.class);

    @Autowired private JdbcTemplate jdbcTemplate;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private static final String SQL_GET_CONFIG = "SELECT id, rMinutes, rCount, ruleId FROM iim.rebootconfig where ruleId=";
    private static final String SQL_UPDATE_CONFIG = "UPDATE iim.rebootconfig SET rMinutes=?, rCount=? WHERE ruleId=?";
    private static final String SQL_SUMMARY = "SELECT host, restartCount, LastRebootTime, AlertOps, Restart FROM iim.reboot";
    private static final String SQL_SUMMARY_HOST = "SELECT host, restartCount, LastRebootTime, AlertOps, Restart FROM iim.reboot where host=";
    private static final String SQL_DETAILS = "SELECT host_id, requestCount, maxRestartCount, requestTime, alertOps, restart FROM iim.reboot_audit where host_id=";
    private static final String SQL_INSERT_SUMMARY = "INSERT into iim.reboot (host, restartCount, LastRebootTime, AlertOps, Restart) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_SUMMARY = "UPDATE iim.reboot SET restartCount = ?, LastRebootTime = ? WHERE host = ?";
    private static final String SQL_INSERT_DETAILS = "INSERT into iim.reboot_audit (host_id, requestCount, maxRestartCount, requestTime, alertOps, restart) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_MAX_RESTART_CT = "SELECT max(requestCount) + 1 FROM iim.reboot_audit where host_id =";

    @Override
    public Config getConfig(String ruleID) {

		String sql = SQL_GET_CONFIG + "'" + ruleID + "'";
        Config config = new Config();

        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    do {
                        config.setID(resultSet.getInt("id"));
                        config.setMinutes(resultSet.getInt("rMinutes"));
                        config.setCount(resultSet.getInt("rCount"));
                        config.setRuleID(resultSet.getString("ruleId"));
                    } while (resultSet.next());
                }
            });
        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }

        return config;
    }

    @Override
    public void updateConfig(Config config) {

        try {
            jdbcTemplate.update(SQL_UPDATE_CONFIG, config.getMinutes(), config.getCount(), config.getRuleID());
        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }
    }
    
    @Override
    public List<Summary> getSummary() {

        List<Summary> list = new ArrayList<Summary>();
 
        try {
            jdbcTemplate.query(SQL_SUMMARY, new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    do {

                        Summary summary = new Summary();
                        summary.setHost(resultSet.getString("host"));
                        summary.setRestartCount(resultSet.getInt("restartCount"));
                        summary.setLastRebootTime(resultSet.getTimestamp("LastRebootTime"));
                        summary.setAlertOps(resultSet.getString("AlertOps"));
                        summary.setRestart(resultSet.getString("Restart"));
                        list.add(summary);

                    } while (resultSet.next());
                }
            });
        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }

        return list;
    }

    @Override
    public List<Summary> getSummary(String hostID) {

		String sql = SQL_SUMMARY_HOST + "'" + hostID + "'";
        List<Summary> list = new ArrayList<Summary>();

        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    do {

                        Summary summary = new Summary();
                        summary.setHost(resultSet.getString("host"));
                        summary.setRestartCount(resultSet.getInt("restartCount"));
                        summary.setLastRebootTime(resultSet.getTimestamp("LastRebootTime"));
                        summary.setAlertOps(resultSet.getString("AlertOps"));
                        summary.setRestart(resultSet.getString("Restart"));
                        list.add(summary);

                    } while (resultSet.next());
                }
            });
        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }

        return list;
    }

    @Override
    public void insertSummary(RebootResponse summary, Timestamp currentDateAndTime) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            jdbcTemplate.update(SQL_INSERT_SUMMARY, summary.getHost(), summary.getRestartCount(), currentDateAndTime, summary.getAlertOps(), summary.getRestart());       

        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }
    }

    @Override
    public void updateSummary(RebootResponse summary, Timestamp currentDateAndTime) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            jdbcTemplate.update(SQL_UPDATE_SUMMARY, summary.getRestartCount(), currentDateAndTime, summary.getHost());       

        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }
    }

    @Override
    public List<Details> getDetails(String hostID) {

        List<Details> list = new ArrayList<Details>();
		String sql = SQL_DETAILS + "'" + hostID + "' ORDER BY requestTime DESC";

        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {

                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    do {

                        Details details = new Details();
                        details.setHostID(resultSet.getString("host_id"));
                        details.setRequestCount(resultSet.getInt("requestCount"));
                        details.setRequestTime(resultSet.getTimestamp("requestTime"));
                        details.setAlertOps(resultSet.getString("alertOps"));
                        details.setRestart(resultSet.getString("restart"));
                        details.setMaxRestartCount(resultSet.getInt("maxRestartCount"));
                        list.add(details);

                    } while (resultSet.next());
                }
            });
        } catch (Exception e) { 
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }

        return list;
    }

    @Override
    public void insertDetails(Details details) {

        try {

        	Integer resquestCount = jdbcTemplate.queryForObject(SQL_MAX_RESTART_CT + "'" + details.getHostID() + "'", Integer.class);
        	resquestCount = (resquestCount == null ? 1 : resquestCount);
        	
            jdbcTemplate.update(SQL_INSERT_DETAILS, details.getHostID(), resquestCount.intValue(), details.getMaxRestartCount(), details.getRequestTime(), details.getAlertOps(), details.getRestart());       

        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error("Data Access Exception: msg=" + e.getMessage());
        }
      
    } 
}
