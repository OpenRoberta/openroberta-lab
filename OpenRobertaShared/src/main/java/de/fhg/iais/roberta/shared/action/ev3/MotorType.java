package de.fhg.iais.roberta.shared.action.ev3;

/**
 * This enum contains all the possible types of motor that brick can have.
 */
public enum MotorType {
    MIDDLE, BIG, OTHER;
    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}
