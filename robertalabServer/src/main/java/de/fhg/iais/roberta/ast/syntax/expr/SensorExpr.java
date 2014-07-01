package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.sensoren.Sensor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Expr} in expressions.
 * 
 * @author kcvejoski
 */
public class SensorExpr extends Expr {
    private final Sensor sensor;

    private SensorExpr(Sensor sens) {
        super(Phrase.Kind.SensorExpr);
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
    public static SensorExpr make(Sensor sens) {
        return new SensorExpr(sens);
    }

    /**
     * @return sensor that is wrapped in the expression
     */
    public Sensor getSens() {
        return this.sensor;
    }

    @Override
    public String toString() {
        return "SensorExpr [" + this.sensor + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append(this.sensor);

    }
}
