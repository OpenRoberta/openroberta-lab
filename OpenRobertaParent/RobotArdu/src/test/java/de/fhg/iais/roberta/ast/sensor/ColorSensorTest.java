package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ColorSensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void sensorSetColor() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [3, COLOUR, NO_SLOT]], "
                + "[Location [x=-13, y=147], ColorSensor [1, LIGHT, NO_SLOT]], "
                + "[Location [x=-11, y=187], ColorSensor [2, RGB, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        final Jaxb2ProgramAst<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SC.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        final Jaxb2ProgramAst<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        final ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        final ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);

        Assert.assertEquals("3", cs.getPort());
        Assert.assertEquals("1", cs1.getPort());
        Assert.assertEquals("2", cs2.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }
}
