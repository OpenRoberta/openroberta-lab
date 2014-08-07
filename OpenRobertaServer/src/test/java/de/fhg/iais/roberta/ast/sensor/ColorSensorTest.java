package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class ColorSensorTest {

    @Test
    public void sensorSetColor() throws Exception {
        String a = "BlockAST [project=[[ColorSensor [mode=COLOUR, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setColor.xml");

        ColorSensor cs = (ColorSensor) transformer.getTree().get(0);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setColor.xml");

        ColorSensor cs = (ColorSensor) transformer.getTree().get(0);

        Assert.assertEquals(SensorPort.S3, cs.getPort());
    }

    @Test
    public void sensorGetModeColor() throws Exception {
        String a = "BlockAST [project=[[ColorSensor [mode=GET_MODE, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getModeColor.xml"));
    }

    @Test
    public void sensorGetSampleColor() throws Exception {
        String a = "BlockAST [project=[[ColorSensor [mode=GET_SAMPLE, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getSampleColor.xml"));
    }
}
