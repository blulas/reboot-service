package com.att.infrastructure.model;

public class Host {

    private String host;
    private String restart;

    public String getHost() {
        return host;
    }

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "{" + "host:" + getHost() + " restart:" + getRestart() + "}";
    }
}

