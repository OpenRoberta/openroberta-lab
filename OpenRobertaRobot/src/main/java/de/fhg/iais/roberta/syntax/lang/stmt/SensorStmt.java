package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Stmt} in statements.
 */
@NepoBasic(name = "SENSOR_STMT", category = "STMT", blocklyNames = {})
public final class SensorStmt extends Stmt {
    public final Sensor sensor;

    public SensorStmt(Sensor sensor) {
        super(sensor.getProperty());
        Assert.isTrue(sensor != null && sensor.isReadOnly());
        this.sensor = sensor;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "\nSensorStmt " + this.sensor;
    }

    @Override
    public Block ast2xml() {
        return this.sensor.ast2xml();
    }

}
