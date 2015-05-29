package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.shared.sensor.ev3.UltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.sensor.ev3.UltrasonicSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class UltraSonicSensorTest {

    @Test
    public void sensorSetUltrasonic() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=57], UltraSSensor [mode=DISTANCE, port=S4]], [Location [x=1, y=98], UltraSSensor [mode=PRESENCE, port=S2]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setUltrasonic.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) transformer.getTree().get(0).get(1);
        UltrasonicSensor<Void> cs1 = (UltrasonicSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(UltrasonicSensorMode.DISTANCE, cs.getMode());
        Assert.assertEquals(UltrasonicSensorMode.PRESENCE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) transformer.getTree().get(0).get(1);
        UltrasonicSensor<Void> cs1 = (UltrasonicSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
        Assert.assertEquals(SensorPort.S2, cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setUltrasonic.xml");
    }

}
