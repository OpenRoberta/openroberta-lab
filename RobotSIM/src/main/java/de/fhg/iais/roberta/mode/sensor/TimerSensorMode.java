package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;

public enum TimerSensorMode implements ITimerSensorMode {
    RESET, GET_SAMPLE;

    private final String[] values;

    private TimerSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}