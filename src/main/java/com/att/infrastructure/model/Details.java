package com.att.infrastructure.model;

import java.sql.Timestamp;

public class Details {

	private String hostID;
	private int requestCount;
	private Timestamp requestTime;
	private String alertOps;
	private String restart;

	private int maxRestartCount;
	
	public Details() {
	}

	public Details(String hostID, int requestCount, Timestamp requestTime, String alertOps, String restart, int maxRestartCount) {

		this.hostID = hostID;
		this.requestCount = requestCount;
		this.requestTime = requestTime;
		this.alertOps = alertOps;
		this.restart = restart;
		this.maxRestartCount = maxRestartCount;
	}
	
	public String getHostID() {
		return this.hostID;
	}

	public void setHostID(String hostID) {
		this.hostID = hostID;
	}

	public int getRequestCount() {
		return this.requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public Timestamp getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public String getAlertOps() {
		return this.alertOps;
	}

	public void setAlertOps(String alertOps) {
		this.alertOps = alertOps;
	}

	public String getRestart() {
		return this.restart;
	}

	public void setRestart(String restart) {
		this.restart = restart;
	}

	public int getMaxRestartCount() {
		return maxRestartCount;
	}

	public void setMaxRestartCount(int maxRestartCount) {
		this.maxRestartCount = maxRestartCount;
	}

    @Override
    public String toString() {
        return "{" + "hostID:" + getHostID() + " requestCount:" + getRequestCount() + " requestTime:" + getRequestTime() + " alertOps:" + getAlertOps() + " restart:" + getRestart()+ " maxRestartCount:" + getMaxRestartCount() + "}";
    }
}