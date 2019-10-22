package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Stmt} in statements.
 */
public class SensorStmt<V> extends Stmt<V> {
    private final Sensor<V> sensor;

    private SensorStmt(Sensor<V> sensor) {
        super(BlockTypeContainer.getByName("SENSOR_STMT"), sensor.getProperty(), sensor.getComment());
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
    public static <V> SensorStmt<V> make(Sensor<V> sensor) {
        return new SensorStmt<V>(sensor);
    }

    /**
     * @return sensor that is wrapped in the statement
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    @Override
    public String toString() {
        return "\nSensorStmt " + this.sensor;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitSensorStmt(this);
    }

    @Override
    public Block astToBlock() {
        return getSensor().astToBlock();
    }

}
