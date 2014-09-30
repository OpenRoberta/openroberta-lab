package de.fhg.iais.roberta.ast.syntax.sensor;

/**
 * Mode of the time sensor.
 */
public enum TimerSensorMode {
    RESET, GET_SAMPLE;
    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}