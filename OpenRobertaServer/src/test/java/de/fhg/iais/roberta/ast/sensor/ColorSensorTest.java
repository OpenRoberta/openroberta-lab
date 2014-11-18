package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class ColorSensorTest {

    @Test
    public void sensorSetColor() throws Exception {
        String a = "BlockAST [project=[[Location [x=-59, y=1], ColorSensor [mode=COLOUR, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setColor.xml");

        ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(SensorPort.S3, cs.getPort());
    }

    @Test
    public void sensorGetModeColor() throws Exception {
        String a = "BlockAST [project=[[Location [x=-59, y=55], ColorSensor [mode=GET_MODE, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getModeColor.xml"));
    }

    @Test
    public void sensorGetSampleColor() throws Exception {
        String a = "BlockAST [project=[[Location [x=-65, y=105], ColorSensor [mode=GET_SAMPLE, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleColor.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getModeColor.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getSampleColor.xml");
    }
}
