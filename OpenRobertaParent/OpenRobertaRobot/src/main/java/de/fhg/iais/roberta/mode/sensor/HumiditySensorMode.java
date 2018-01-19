package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IHumiditySensorMode;

public enum HumiditySensorMode implements IHumiditySensorMode {
    VALUE(), HUMIDITY(), TEMPERATURE();

    private final String[] values;

    private HumiditySensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
