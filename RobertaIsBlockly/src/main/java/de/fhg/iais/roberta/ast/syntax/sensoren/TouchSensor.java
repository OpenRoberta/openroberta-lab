package de.fhg.iais.roberta.ast.syntax.sensoren;


public class TouchSensor extends Sensor {
    private final boolean state;

    private TouchSensor(boolean state) {
        this.state = state;
        setReadOnly();
    }

    public static TouchSensor make(boolean state) {
        return new TouchSensor(state);
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public Kind getKind() {
        return Kind.TouchSensor;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "TouchSensor [" + this.state + "]";
    }

}
