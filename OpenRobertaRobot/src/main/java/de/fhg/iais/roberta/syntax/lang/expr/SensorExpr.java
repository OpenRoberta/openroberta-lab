package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Expr} in expressions.
 */
public class SensorExpr<V> extends Expr<V> {
    private final Sensor<V> sensor;

    private SensorExpr(Sensor<V> sens) {
        super(BlockTypeContainer.getByName("SENSOR_EXPR"), sens.getProperty(), sens.getComment());
        Assert.isTrue(sens.isReadOnly());
        this.sensor = sens;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SensorExpr}.
     *
     * @param sensor that we want to wrap,
     * @return expression with wrapped sensor inside
     */
    public static <V> SensorExpr<V> make(Sensor<V> sens) {
        return new SensorExpr<V>(sens);
    }

    /**
     * @return sensor that is wrapped in the expression
     */
    public Sensor<V> getSens() {
        return this.sensor;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        return "SensorExpr [" + this.sensor + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitSensorExpr(this);
    }

    @Override
    public Block astToBlock() {
        Phrase<V> p = getSens();
        return p.astToBlock();
    }
}
