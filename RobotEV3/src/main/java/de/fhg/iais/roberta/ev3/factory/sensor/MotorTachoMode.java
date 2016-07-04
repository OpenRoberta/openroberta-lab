package de.fhg.iais.roberta.ev3.factory.sensor;

import de.fhg.iais.roberta.factory.sensor.IMotorTachoMode;

public enum MotorTachoMode implements IMotorTachoMode {
    ROTATION(), DEGREE(), RESET(), DISTANCE();

    private final String[] values;

    private MotorTachoMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}