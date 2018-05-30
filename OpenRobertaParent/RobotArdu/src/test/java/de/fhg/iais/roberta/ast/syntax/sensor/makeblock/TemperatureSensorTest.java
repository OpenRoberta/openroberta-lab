package de.fhg.iais.roberta.ast.syntax.sensor.makeblock;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperMBotForXmlTest;

public class TemperatureSensorTest {
    private final HelperMBotForXmlTest h = new HelperMBotForXmlTest();

    @Test
    public void jaxbToAst_byDefault_temperatureSensorOnPort4() throws Exception {
        String a = "BlockAST [project=[[Location [x=113, y=88], TemperatureSensor [4, DEFAULT, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_temperature.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_temperature.xml");

        TemperatureSensor<Void> cs = (TemperatureSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals("PORT_4", cs.getPort().getCodeName());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_temperature.xml");
    }
}
