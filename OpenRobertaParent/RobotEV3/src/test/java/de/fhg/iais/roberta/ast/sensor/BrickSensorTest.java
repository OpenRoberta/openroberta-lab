package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.ev3.BrickKey;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class BrickSensorTest {
    Helper h = new Helper();

    @Test
    public void main() throws Exception {
        String a = "BlockAST [project=[[Location [x=-19, y=1], BrickSensor [key=ENTER, mode=IS_PRESSED]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_brick1.xml"));
    }

    @Test
    public void getKey() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(BrickKey.ENTER, bs.getKey());
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(BrickSensor.Mode.IS_PRESSED, bs.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            BrickSensor<Void> va = BrickSensor.make(BrickSensor.Mode.valueOf("invalid"), null, null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor.Mode.invalid", e.getMessage());
        }

    }

    @Test
    public void sensorBrick() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [DEFAULT, S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [BrickSensor [key=ENTER, mode=IS_PRESSED]]\n\n"
                + "]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_brick.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_brick1.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_brick.xml");
    }
}
