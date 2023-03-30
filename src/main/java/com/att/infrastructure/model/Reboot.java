package com.att.infrastructure.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Reboot implements Serializable {

	private static final long serialVersionUID = 1L;

	// Template attributes
	private Integer templateMinutes;
	private Integer templateCount;

	// Object attributes
	private String host;
	private String restart;
	private Integer restartCount;
	private String alertOps;
	private Integer restartedMin;

	public Reboot() {
	}

	public Reboot(Integer templateMinutes, Integer templateCount) {

		this.templateMinutes = templateMinutes;
		this.templateCount = templateCount;
	}

	public Reboot(String host, String restart, Integer restartCount, String alertOps, Integer restartedMin) {

		this.host = host;
		this.restart = restart;
		this.restartCount = restartCount;
		this.alertOps = alertOps;
		this.restartedMin = restartedMin;
	}

    @JsonIgnore
	public Integer getTemplateMinutes() {
		return this.templateMinutes;
	}

	public void setTemplateMinutes(Integer templateMinutes) {
		this.templateMinutes = templateMinutes;
	}

    @JsonIgnore
	public Integer getTemplateCount() {
		return this.templateCount;
	}

	public void setTemplateCount(Integer templateCount) {
		this.templateCount = templateCount;
	}
	
	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRestart() {
		return this.restart;
	}

	public void setRestart(String restart) {
		this.restart = restart;
	}

	public Integer getRestartCount() {
		return this.restartCount;
	}

	public void setRestartCount(Integer restartCount) {
		this.restartCount = restartCount;
	}

	public String getAlertOps() {
		return this.alertOps;
	}

	public void setAlertOps(String alertOps) {
		this.alertOps = alertOps;
	}

	public Integer getRestartedMin() {
		return this.restartedMin;
	}

	public void setRestartedMin(Integer restartedMin) {
		this.restartedMin = restartedMin;
	}

    @Override
    public String toString() {
        return "{" + "host:" + getHost() + " restart:" + getRestart() + " restartCount:" + getRestartCount() + " alertOps:" + getAlertOps() + " restartedMin:" + getRestartedMin() + "}";
    }
}