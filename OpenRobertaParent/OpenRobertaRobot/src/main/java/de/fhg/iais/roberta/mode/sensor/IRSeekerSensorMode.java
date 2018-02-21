package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IIRSeekerSensorMode;

public enum IRSeekerSensorMode implements IIRSeekerSensorMode {
    MODULATED( "Modulated" ), UNMODULATED( "Unmodulated" );

    private final String[] values;

    private IRSeekerSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
