package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public class SensorPort implements ISensorPort, Comparable<SensorPort> {

    private final String[] values;

    public SensorPort(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getPortNumber() {
        return this.values[0];
    }

    @Override
    public String getPortName() {
        return this.values[1];
    }

    @Override
    public int compareTo(SensorPort other) {
        return this.values[1].compareTo(other.values[1]);
    }

    @Override
    public String toString() {
        return this.values[1];
    }

}
