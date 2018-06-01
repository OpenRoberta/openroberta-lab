package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class GyroSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-30, y=210], GyroSensor [2, ANGLE, NO_SLOT]], [Location [x=-26, y=250], GyroSensor [4, RATE, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setGyro.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(GyroSensorMode.ANGLE, cs.getMode());
        Assert.assertEquals(GyroSensorMode.RATE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals("S2", cs.getPort().getCodeName());
        Assert.assertEquals("S4", cs1.getPort().getCodeName());
    }

    @Test
    public void sensorResetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-13, y=105], GyroSensor [2, RESET, NO_SLOT]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_resetGyro.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_resetGyro.xml");
    }

}
