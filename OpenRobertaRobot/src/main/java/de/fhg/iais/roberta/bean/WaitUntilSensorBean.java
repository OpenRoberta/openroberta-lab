package de.fhg.iais.roberta.bean;

public class WaitUntilSensorBean {
    private final String implementingClass;
    private final String sensor;
    private final String mode;

    public WaitUntilSensorBean(String implementingClass, String sensor, String mode) {
        this.implementingClass = implementingClass;
        this.sensor = sensor;
        this.mode = mode;
    }

    public String getImplementingClass() {
        return this.implementingClass;
    }

    public String getSensor() {
        return this.sensor;
    }

    public String getMode() {
        return this.mode;
    }
}
