package de.fhg.iais.roberta.ast.syntax.sensor;

/**
 * Mode of the time sensor.
 */
public enum TimerSensorMode {
    RESET, GET_SAMPLE;

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}