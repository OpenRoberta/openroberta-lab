package de.fhg.iais.roberta.mode.sensor.makeblock;

import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;

public enum JoystickMode implements IJoystickMode {
    X( "X" ), Y( "Y" );

    private final String[] values;

    private JoystickMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}