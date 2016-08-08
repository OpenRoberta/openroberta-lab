package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.nxt.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ColorSensorTest {

    @Test
    public void sensorSetColor() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [mode=COLOUR, port=IN_3]], "
                + "[Location [x=-13, y=147], ColorSensor [mode=RED, port=IN_1]], "
                + "[Location [x=-11, y=224], ColorSensor [mode=AMBIENTLIGHT, port=IN_4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        final ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        final ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);

        Assert.assertEquals(SensorPort.IN_3, cs.getPort());
        Assert.assertEquals(SensorPort.IN_1, cs1.getPort());
        Assert.assertEquals(SensorPort.IN_4, cs2.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }
}
