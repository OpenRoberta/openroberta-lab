package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;

public enum TimerSensorMode implements ITimerSensorMode {
    RESET, VALUE, DEFAULT;

    private final String[] values;

    private TimerSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}