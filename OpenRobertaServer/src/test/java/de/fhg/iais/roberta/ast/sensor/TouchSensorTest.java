package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class TouchSensorTest {

    @Test
    public void sensorTouch() throws Exception {
        String a = "BlockAST [project=[[Location [x=-86, y=1], TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Touch.xml"));
    }

    @Test
    public void getPort() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_Touch.xml");

        TouchSensor<Void> cs = (TouchSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
    }
}
