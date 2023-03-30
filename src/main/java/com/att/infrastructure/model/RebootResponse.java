package com.att.infrastructure.model;

import java.io.Serializable;

public class RebootResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	// Object attributes
	private String host;
	private String restart;
	private Integer restartCount;
	private String alertOps;

	public RebootResponse() {
	}

	public RebootResponse(String host, String restart, Integer restartCount, String alertOps) {

		this.host = host;
		this.restart = restart;
		this.restartCount = restartCount;
		this.alertOps = alertOps;
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

    @Override
    public String toString() {
        return "{" + "host:" + getHost() + " restart:" + getRestart() + " restartCount:" + getRestartCount() + " alertOps:" + getAlertOps() + "}";
    }
}