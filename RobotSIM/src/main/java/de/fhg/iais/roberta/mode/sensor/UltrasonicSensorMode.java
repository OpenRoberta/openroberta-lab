package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;

public enum UltrasonicSensorMode implements IUltrasonicSensorMode {
    DISTANCE( "getUltraSonicSensorDistance", "Distance" ), PRESENCE( "getUltraSonicSensorPresence", "Listen" );

    private final String[] values;
    private final String halJavaMethodName;

    private UltrasonicSensorMode(String halJavaMethodName, String... values) {
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