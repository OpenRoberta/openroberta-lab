package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class SampleSensorTest {

    @Test
    public void sensorGetSample1() throws Exception {
        String a = "BlockAST [project=[[TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleSensor.xml"));
    }
}
