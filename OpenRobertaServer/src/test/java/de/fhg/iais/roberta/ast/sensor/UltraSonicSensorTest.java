package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class UltraSonicSensorTest {

    @Test
    public void sensorSetUltrasonic() throws Exception {
        String a = "BlockAST [project=[[Location [x=-72, y=1], UltraSSensor [mode=DISTANCE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setUltrasonic.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(UltrasonicSensorMode.DISTANCE, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
    }

    @Test
    public void sensorGetModeUltrasonic() throws Exception {
        String a = "BlockAST [project=[[Location [x=-72, y=55], UltraSSensor [mode=GET_MODE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getModeUltrasonic.xml"));
    }

    @Test
    public void sensorGetSampleUltrasonic() throws Exception {
        String a = "BlockAST [project=[[Location [x=-79, y=105], UltraSSensor [mode=GET_SAMPLE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleUltrasonic.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setUltrasonic.xml");
    }

    @Ignore
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getModeUltrasonic.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getSampleUltrasonic.xml");
    }

}
