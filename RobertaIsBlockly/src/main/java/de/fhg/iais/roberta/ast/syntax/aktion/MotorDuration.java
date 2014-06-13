package de.fhg.iais.roberta.ast.syntax.aktion;

public class MotorDuration {
    private Type type;
    private int value;

    private MotorDuration(Type type, int value) {
        super();
        this.setType(type);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        ROTATION, DEGREE;
    }
}
