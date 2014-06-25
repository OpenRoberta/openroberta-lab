package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.sensoren.Sensor;
import de.fhg.iais.roberta.dbc.Assert;

public class SensorStmt extends Stmt {
    private final Sensor sensor;

    private SensorStmt(Sensor sensor) {
        super(Phrase.Kind.SensorStmt);
        Assert.isTrue(sensor != null && sensor.isReadOnly());
        this.sensor = sensor;
        setReadOnly();
    }

    public static SensorStmt make(Sensor sensor) {
        return new SensorStmt(sensor);
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("SensorStmt ").append(this.sensor);
    }

    @Override
    public String toString() {
        return "SensorStmt [sensor=" + this.sensor + "]";
    }

}
