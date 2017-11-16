package de.fhg.iais.roberta.mode.sensors.arduino.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;

public enum InfraredSensorMode implements IInfraredSensorMode {
    OBSTACLE( "Distance" ), SEEK( "Seek" );

    private final String[] values;

    private InfraredSensorMode(String... values) {

        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
    public String getLejosModeName() {
        return this.values[0];
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}