package de.fhg.iais.roberta.bean;

public class WaitUntilSensorBean {
    private final String implementingClass;
    private final String mode;

    public WaitUntilSensorBean(String implementingClass, String mode) {
        this.implementingClass = implementingClass;
        this.mode = mode;
    }

    public String getImplementingClass() {
        return this.implementingClass;
    }

    public String getMode() {
        return this.mode;
    }
}
