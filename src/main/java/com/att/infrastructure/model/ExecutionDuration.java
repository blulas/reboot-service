package com.att.infrastructure.model;

public class ExecutionDuration {

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private long milliseconds = 0L;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return "hours=" + getHours() + ", minutes=" + getMinutes() + ", seconds=" + getSeconds() + ", milliseconds=" + getMilliseconds();
    }
}