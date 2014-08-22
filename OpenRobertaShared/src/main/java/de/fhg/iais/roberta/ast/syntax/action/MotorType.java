package de.fhg.iais.roberta.ast.syntax.action;

/**
 * This enum contains all the possible types of motor that brick can have.
 * 
 * @author kcvejoski
 */
public enum MotorType {
    MIDDLE, BIG, OTHER;

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}
