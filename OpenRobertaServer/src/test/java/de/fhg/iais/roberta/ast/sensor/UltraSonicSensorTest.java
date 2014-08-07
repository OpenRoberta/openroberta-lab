package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class UltraSonicSensorTest {

    @Test
    public void sensorSetUltrasonic() throws Exception {
        String a = "BlockAST [project=[[UltraSSensor [mode=DISTANCE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_setUltrasonic.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor cs = (UltrasonicSensor) transformer.getTree().get(0);

        Assert.assertEquals(UltrasonicSensorMode.DISTANCE, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor cs = (UltrasonicSensor) transformer.getTree().get(0);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
    }

    @Test
    public void sensorGetModeUltrasonic() throws Exception {
        String a = "BlockAST [project=[[UltraSSensor [mode=GET_MODE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getModeUltrasonic.xml"));
    }

    @Test
    public void sensorGetSampleUltrasonic() throws Exception {
        String a = "BlockAST [project=[[UltraSSensor [mode=GET_SAMPLE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getSampleUltrasonic.xml"));
    }
}
