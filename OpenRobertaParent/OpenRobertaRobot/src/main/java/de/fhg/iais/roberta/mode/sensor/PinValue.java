package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IPinValue;

/**
 * This enumeration contain all types of sensors that are used in <b>mbedSensors_pin_getSample</b> Blockly block.
 */
public enum PinValue implements IPinValue {
    ANALOG( "AnalogValue" ), DIGITAL( "DigitalValue" ), PULSE_HIGH( "PulseHigh" ), PULSE_LOW( "PulseLow" ), DEFAULT;

    private final String[] values;

    private PinValue(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
