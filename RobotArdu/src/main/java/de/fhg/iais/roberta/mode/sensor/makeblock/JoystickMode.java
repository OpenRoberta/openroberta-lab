package de.fhg.iais.roberta.mode.sensor.makeblock;

import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;

public enum JoystickMode implements IJoystickMode {
    X( "getJoystickX", "X" ), Y( "getJoystickY", "Y" );

    private final String[] values;
    //private final String halJavaMethodName;

    private JoystickMode(String halJavaMethodName, String... values) {
        //this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}