package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.dbc.Assert;

public class ColorSensor extends Sensor {
    private final CoType measureType;

    private ColorSensor(CoType measureType) {
        Assert.isTrue(measureType != null);
        this.measureType = measureType;
        setReadOnly();
    }

    public static ColorSensor make(CoType measureType) {
        return new ColorSensor(measureType);
    }

    public CoType getCoType() {
        return this.measureType;
    }

    @Override
    public Kind getKind() {
        return Kind.ColorSensor;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "ColorSensor [" + this.measureType + "]";
    }

    public static enum CoType {
        color, light, ambientLight;
    }
}
