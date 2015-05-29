package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.ev3.ColorSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ColorSensorTest {

    @Test
    public void sensorSetColor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [mode=COLOUR, port=S3]], "
                + "[Location [x=-13, y=147], ColorSensor [mode=RED, port=S1]], "
                + "[Location [x=-11, y=187], ColorSensor [mode=RGB, port=S2]], "
                + "[Location [x=-11, y=224], ColorSensor [mode=AMBIENTLIGHT, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);
        ColorSensor<Void> cs3 = (ColorSensor<Void>) transformer.getTree().get(3).get(1);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
        Assert.assertEquals(ColorSensorMode.RED, cs1.getMode());
        Assert.assertEquals(ColorSensorMode.RGB, cs2.getMode());
        Assert.assertEquals(ColorSensorMode.AMBIENTLIGHT, cs3.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);
        ColorSensor<Void> cs3 = (ColorSensor<Void>) transformer.getTree().get(3).get(1);

        Assert.assertEquals(SensorPort.S3, cs.getPort());
        Assert.assertEquals(SensorPort.S1, cs1.getPort());
        Assert.assertEquals(SensorPort.S2, cs2.getPort());
        Assert.assertEquals(SensorPort.S4, cs3.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }
}
