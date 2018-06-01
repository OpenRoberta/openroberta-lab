package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ColorSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void sensorSetColor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [3, COLOUR, NO_SLOT]], "
                + "[Location [x=-13, y=147], ColorSensor [1, LIGHT, NO_SLOT]], "
                + "[Location [x=-11, y=187], ColorSensor [2, RGB, NO_SLOT]], "
                + "[Location [x=-11, y=224], ColorSensor [4, AMBIENTLIGHT, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);
        ColorSensor<Void> cs3 = (ColorSensor<Void>) transformer.getTree().get(3).get(1);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
        Assert.assertEquals(ColorSensorMode.LIGHT, cs1.getMode());
        Assert.assertEquals(ColorSensorMode.RGB, cs2.getMode());
        Assert.assertEquals(ColorSensorMode.AMBIENTLIGHT, cs3.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);
        ColorSensor<Void> cs3 = (ColorSensor<Void>) transformer.getTree().get(3).get(1);

        Assert.assertEquals("S3", cs.getPort().getCodeName());
        Assert.assertEquals("S1", cs1.getPort().getCodeName());
        Assert.assertEquals("S2", cs2.getPort().getCodeName());
        Assert.assertEquals("S4", cs3.getPort().getCodeName());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }
}
