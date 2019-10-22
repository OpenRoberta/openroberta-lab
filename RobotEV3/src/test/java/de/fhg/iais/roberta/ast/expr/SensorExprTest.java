package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;

public class SensorExprTest {
    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "SensorExpr [TouchSensor [1, TOUCH, EMPTY_SLOT]]";
        Assert.assertEquals(a, sensorExpr.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "TouchSensor [1, TOUCH, EMPTY_SLOT]";
        Assert.assertEquals(a, sensorExpr.getSens().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(999, sensorExpr.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(Assoc.NONE, sensorExpr.getAssoc());
    }
}
