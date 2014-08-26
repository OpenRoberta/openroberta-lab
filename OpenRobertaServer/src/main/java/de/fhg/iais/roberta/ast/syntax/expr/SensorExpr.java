package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.sensor.Sensor;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Expr} in expressions.
 * 
 * @author kcvejoski
 */
public class SensorExpr<V> extends Expr<V> {
    private final Sensor<V> sensor;

    private SensorExpr(Sensor<V> sens) {
        super(Phrase.Kind.SENSOR_EXPR);
        Assert.isTrue(sens.isReadOnly());
        this.sensor = sens;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SensorExpr}.
     * 
     * @param sensor that we want to wrap
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
    public String toString() {
        return "SensorExpr [" + this.sensor + "]";
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitSensorExpr(this);
    }
}
