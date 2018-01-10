package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.arduino.botnroll.Factory;
import de.fhg.iais.roberta.mode.sensor.BrickKey;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class BrickSensorTest {
    HelperBotNroll h = new HelperBotNroll();
    Factory robotFactory = new Factory();

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void main() throws Exception {
        String a = "BlockAST [project=[[Location [x=-19, y=1], BrickSensor [ENTER, PRESSED, EMPTY_SLOT]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_brick1.xml"));
    }

    @Test
    public void getKey() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(BrickKey.ENTER, bs.getPort());
    }

    @Test
    public void sensorBrick() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [S1, DEFAULT, EMPTY_SLOT]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [BrickSensor [ENTER, PRESSED, EMPTY_SLOT]]\n\n"
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
