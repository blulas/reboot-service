package com.att.infrastructure.model;

public class RebootRequest {

    private Host hosts[];

    public Host[] getHosts() {
        return this.hosts;
    }

    public void setHosts(Host hosts[]) {
        this.hosts = hosts;
    }
}