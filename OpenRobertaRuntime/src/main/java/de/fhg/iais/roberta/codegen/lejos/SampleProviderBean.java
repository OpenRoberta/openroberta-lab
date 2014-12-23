package de.fhg.iais.roberta.codegen.lejos;

import java.io.Serializable;

import lejos.robotics.SampleProvider;

public class SampleProviderBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String modeName;
    private SampleProvider sampleProvider;

    public String getModeName() {
        return this.modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public SampleProvider getSampleProvider() {
        return this.sampleProvider;
    }

    public void setSampleProvider(SampleProvider sampleProvider) {
        this.sampleProvider = sampleProvider;
    }

}
