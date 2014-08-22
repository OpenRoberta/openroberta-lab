package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class TouchSensorTest {

    @Test
    public void sensorTouch() throws Exception {
        String a = "BlockAST [project=[[TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Touch.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/sensors/sensor_Touch.xml");

        TouchSensor cs = (TouchSensor) transformer.getTree().get(0);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
    }
}
