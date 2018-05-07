package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IPinPull;

/**
 * This enumeration contains all types of pin pull that are available in <b>mbedSensors_pin_set_pull</b> Blockly block.
 */
public enum PinPull implements IPinPull {
    UP( "Up" ), DOWN( "Down" ), NONE( "None" );

    private final String[] values;

    private PinPull(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
