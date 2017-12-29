package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;

public enum Axis implements ICoordinatesMode, IJoystickMode, IGyroSensorMode {
    DEFAULT, X( "X" ), Y( "Y" ), Z( "Z" ), STRENGTH( "Strength" ), VALUE( "" );

    private final String[] values;

    private Axis(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
