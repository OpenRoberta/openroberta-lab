package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class InfraredSensorTest {

    @Test
    public void sensorSetInfrared() throws Exception {
        String a = "BlockAST [project=[[InfraredSensor [mode=DISTANCE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor cs = (InfraredSensor) transformer.getTree().get(0);

        Assert.assertEquals(InfraredSensorMode.DISTANCE, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor cs = (InfraredSensor) transformer.getTree().get(0);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
    }

    @Test
    public void sensorGetModeInfrared() throws Exception {
        String a = "BlockAST [project=[[InfraredSensor [mode=GET_MODE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getModeInfrared.xml"));
    }

    @Test
    public void sensorGetSampleInfrared() throws Exception {
        String a = "BlockAST [project=[[InfraredSensor [mode=GET_SAMPLE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleInfrared.xml"));
    }
}
