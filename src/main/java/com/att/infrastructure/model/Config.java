package com.att.infrastructure.model;

public class Config {

	private int	ID = 1;
	private int minutes;
	private int count;
	private String ruleID;

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getRuleID() {
		return this.ruleID;
	}

	public void setRuleID(String ruleID) {
		this.ruleID = ruleID;
	}

    @Override
    public String toString() {
        return "{" + "ID:" + getID() + " ruleID:" + getRuleID() + " minutes:" + getMinutes() + " count:" + getCount() + "}";
    }
}