package de.fag.iais.roberta.mode.sensor.arduino.mbot;

import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;

public enum JoystickMode implements IJoystickMode {
    X( "X" ), Y( "Y" );

    private final String[] values;

    private final String axis;

    private JoystickMode(String axis, String... values) {
        this.axis = axis;
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    public String getAxis() {
        return this.axis;
    }

}