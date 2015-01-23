package de.fhg.iais.roberta.codegen.lejos;

import java.io.Serializable;

import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;

public class MotorPort implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    boolean isRegulated;
    RegulatedMotor regulatedMotor;
    EncoderMotor unRegulatedMotor;

    /**
     * @return the isRegulated
     */
    public boolean isRegulated() {
        return this.isRegulated;
    }

    /**
     * @param isRegulated the isRegulated to set
     */
    public void setRegulated(boolean isRegulated) {
        this.isRegulated = isRegulated;
    }

    /**
     * @return the regulatedMotor
     */
    public RegulatedMotor getRegulatedMotor() {
        return this.regulatedMotor;
    }

    /**
     * @param regulatedMotor the regulatedMotor to set
     */
    public void setRegulatedMotor(RegulatedMotor regulatedMotor) {
        this.regulatedMotor = regulatedMotor;
    }

    /**
     * @return the unRegulatedMotor
     */
    public EncoderMotor getUnRegulatedMotor() {
        return this.unRegulatedMotor;
    }

    /**
     * @param unRegulatedMotor the unRegulatedMotor to set
     */
    public void setUnRegulatedMotor(EncoderMotor unRegulatedMotor) {
        this.unRegulatedMotor = unRegulatedMotor;
    }

}
