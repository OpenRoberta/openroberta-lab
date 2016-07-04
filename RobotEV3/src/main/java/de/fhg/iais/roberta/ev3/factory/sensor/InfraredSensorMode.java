package de.fhg.iais.roberta.ev3.factory.sensor;

import de.fhg.iais.roberta.factory.sensor.IInfraredSensorMode;

public enum InfraredSensorMode implements IInfraredSensorMode {
    DISTANCE( "getInfraredSensorDistance", "Distance" ), SEEK( "getInfraredSensorSeek", "Seek" );

    private final String[] values;
    private final String halJavaMethodName;

    private InfraredSensorMode(String halJavaMethodName, String... values) {
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