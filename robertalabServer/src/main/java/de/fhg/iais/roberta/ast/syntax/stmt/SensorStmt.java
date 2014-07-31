package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.sensor.Sensor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Stmt} in statements.
 */
public class SensorStmt extends Stmt {
    private final Sensor sensor;

    private SensorStmt(Sensor sensor) {
        super(Phrase.Kind.SENSOR_STMT);
        Assert.isTrue(sensor != null && sensor.isReadOnly());
        this.sensor = sensor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SensorStmt}.
     * 
     * @param sensor that we want to wrap
     * @return statement with wrapped sensor inside
     */
    public static SensorStmt make(Sensor sensor) {
        return new SensorStmt(sensor);
    }

    /**
     * @return sensor that is wrapped in the statement
     */
    public Sensor getSensor() {
        return this.sensor;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("SensorStmt ").append(this.sensor);
    }

    @Override
    public String toString() {
        return "\nSensorStmt " + this.sensor;
    }

}
