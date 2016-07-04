package de.fhg.iais.roberta.ev3.factory.sensor;

import de.fhg.iais.roberta.factory.sensor.ITouchSensorMode;

public enum TouchSensorMode implements ITouchSensorMode {
    TOUCH( "isPressed", "touch" );
    private final String halJavaMethodName;
    private final String[] values;

    private TouchSensorMode(String halJavaMethodName, String... values) {
        this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    public String getLejosModeName() {
        return this.values[0];
    }

    //    @Override
    //    public String getHalJavaMethod() {
    //        return this.halJavaMethodName;
    //    }
    @Override
    public String[] getValues() {
        return this.values;
    }

}