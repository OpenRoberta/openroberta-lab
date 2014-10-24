package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class InfraredSensorTest {

    @Test
    public void sensorSetInfrared() throws Exception {
        String a = "BlockAST [project=[[Location [x=-47, y=1], InfraredSensor [mode=DISTANCE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(InfraredSensorMode.DISTANCE, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
    }

    @Test
    public void sensorGetModeInfrared() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=55], InfraredSensor [mode=GET_MODE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getModeInfrared.xml"));
    }

    @Test
    public void sensorGetSampleInfrared() throws Exception {
        String a = "BlockAST [project=[[Location [x=-11, y=105], InfraredSensor [mode=GET_SAMPLE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleInfrared.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setInfrared.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getModeInfrared.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_getSampleInfrared.xml");
    }
}
