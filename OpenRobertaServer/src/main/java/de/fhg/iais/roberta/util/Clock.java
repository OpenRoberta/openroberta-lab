package de.fhg.iais.roberta.util;

import java.util.Date;

public class Clock {
    private final long time;

    private Clock() {
        this.time = new Date().getTime();
    }

    public static Clock start() {
        return new Clock();
    }

    public long elapsedMsec() {
        return new Date().getTime() - this.time;
    }

    public long elapsedSec() {
        return elapsedMsec() / 1000;
    }

    public String elapsedSecFormatted() {
        long elapsedSec = elapsedSec();
        String suffix = elapsedSec != 1 ? " secs" : " sec";
        return "" + elapsedSec + suffix;
    }

    public String elapsedMsecFormatted() {
        return "" + elapsedMsec() + " msec";
    }

}
