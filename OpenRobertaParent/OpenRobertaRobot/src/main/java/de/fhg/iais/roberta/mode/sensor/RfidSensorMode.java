package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IRfidSensorMode;

public enum RfidSensorMode implements IRfidSensorMode {
    VALUE(), PRESENCE(), SERIAL();

    private final String[] values;

    private RfidSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
