package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;

public enum MotorTachoMode implements IMotorTachoMode {
    DEFAULT, ROTATION(), DEGREE(), RESET(), DISTANCE();

    private final String[] values;

    private MotorTachoMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}