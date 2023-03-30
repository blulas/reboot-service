package com.att.infrastructure.model;

import java.sql.Timestamp;

public class Summary {

	private String host;
	private String restart;
	private Integer restartCount;
	private String alertOps;
	private Timestamp lastRebootTime;

	public Summary() {
	}

	public Summary(String host, String restart, Integer restartCount, String alertOps, Timestamp lastRebootTime) {
		
		this.host = host;
		this.restart = restart;
		this.restartCount = restartCount;
		this.alertOps = alertOps;
		this.lastRebootTime = lastRebootTime;
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

	public Timestamp getLastRebootTime() {
		return this.lastRebootTime;
	}

	public void setLastRebootTime(Timestamp lastRebootTime) {
		this.lastRebootTime = lastRebootTime;
	}

    @Override
    public String toString() {
        return "{" + "host:" + getHost() + " restart:" + getRestart() + " restartCount:" + getRestartCount() + " alertOps:" + getAlertOps() + " lastRebootTime:" + getLastRebootTime() + "}";
    }
}