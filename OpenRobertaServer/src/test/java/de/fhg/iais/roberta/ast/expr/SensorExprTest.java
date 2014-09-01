package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;

public class SensorExprTest {

    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "SensorExpr [TouchSensor [port=S1]]";
        Assert.assertEquals(a, sensorExpr.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "TouchSensor [port=S1]";
        Assert.assertEquals(a, sensorExpr.getSens().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(999, sensorExpr.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(Assoc.NONE, sensorExpr.getAssoc());
    }
}
