package de.fhg.iais.roberta.generic.factory.sensor;

import de.fhg.iais.roberta.factory.sensor.IGyroSensorMode;

public enum GyroSensorMode implements IGyroSensorMode {
    RATE( "getGyroSensorRate", "Rate" ), ANGLE( "getGyroSensorAngle", "Angle" ), RESET( "resetGyroSensor" );

    private final String[] values;
    private final String halJavaMethodName;

    private GyroSensorMode(String halJavaMethodName, String... values) {
        this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
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