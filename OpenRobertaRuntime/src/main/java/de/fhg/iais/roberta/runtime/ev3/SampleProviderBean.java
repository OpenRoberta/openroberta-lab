package de.fhg.iais.roberta.runtime.ev3;

import java.io.Serializable;

import lejos.robotics.SampleProvider;

/**
 * This bean keeps pair of sensor mode name and sample provider of the given sensor mode. It is used in {@link DeviceHandler} for storing all the sensor modes
 * on a given port.
 *
 * @author kcvejoski
 */
public class SampleProviderBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String modeName;
    private SampleProvider sampleProvider;

    /**
     * Name of the sensor mode
     *
     * @return sensor mode name
     */
    public String getModeName() {
        return this.modeName;
    }

    /**
     * Set the name of the sensor mode
     *
     * @param modeName
     */
    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    /**
     * Get the sample provider
     *
     * @return sample provider of sensor mode
     */
    public SampleProvider getSampleProvider() {
        return this.sampleProvider;
    }

    /**
     * Set the sample provider for the sensor mode
     *
     * @param sampleProvider of the sensor mode
     */
    public void setSampleProvider(SampleProvider sampleProvider) {
        this.sampleProvider = sampleProvider;
    }

}
