package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class InfraredSensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void sensorSetInfrared() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-23, y=157], InfraredSensor [4, OBSTACLE, NO_SLOT]], "
                + "[Location [x=-19, y=199], InfraredSensor [3, PRESENCE, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(InfraredSensorMode.OBSTACLE, cs.getMode());
        Assert.assertEquals(InfraredSensorMode.PRESENCE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals("S4", cs.getPort().getCodeName());
        Assert.assertEquals("S3", cs1.getPort().getCodeName());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setInfrared.xml");
    }
}
