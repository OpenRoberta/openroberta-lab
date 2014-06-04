package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.sensoren.Sensor;
import de.fhg.iais.roberta.dbc.Assert;

public class SensorExpr extends Expr {
    private final Sensor sens;

    private SensorExpr(Sensor sens) {
        Assert.isTrue(sens.isReadOnly());
        this.sens = sens;
        setReadOnly();
    }

    public SensorExpr make(Sensor sens) {
        return new SensorExpr(sens);
    }

    public Sensor getSens() {
        return this.sens;
    }

    @Override
    public Kind getKind() {
        return Kind.SensorExpr;
    }

    @Override
    public String toString() {
        return "SensorExpr [" + this.sens + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

}
