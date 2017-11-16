package de.fhg.iais.roberta.mode.sensors.arduino.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    ENTER(), LEFT(), RIGHT(), ANY();

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}