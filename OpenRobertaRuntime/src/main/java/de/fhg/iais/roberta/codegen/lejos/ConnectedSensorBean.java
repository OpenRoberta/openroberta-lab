package de.fhg.iais.roberta.codegen.lejos;

import java.io.Serializable;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.UARTSensor;

public class ConnectedSensorBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String sensorName;
    private UARTSensor sensor;
    private SampleProviderBean[] sampleProviders;
    private EV3TouchSensor touchSensor;

    /**
     * @return the sensorName
     */
    public String getSensorName() {
        return this.sensorName;
    }

    /**
     * @param sensorName the sensorName to set
     */
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    /**
     * @return the sensor
     */
    public UARTSensor getSensor() {
        return this.sensor;
    }

    /**
     * @param sensor the sensor to set
     */
    public void setSensor(UARTSensor sensor) {
        this.sensor = sensor;
    }

    /**
     * @return the sampleProviders
     */
    public SampleProviderBean[] getSampleProviders() {
        return this.sampleProviders;
    }

    /**
     * @param sampleProviders the sampleProviders to set
     */
    public void setSampleProviders(SampleProviderBean[] sampleProviders) {
        this.sampleProviders = sampleProviders;
    }

    /**
     * @return the touchSensor
     */
    public EV3TouchSensor getTouchSensor() {
        return this.touchSensor;
    }

    /**
     * @param touchSensor the touchSensor to set
     */
    public void setTouchSensor(EV3TouchSensor touchSensor) {
        this.touchSensor = touchSensor;
    }

}
