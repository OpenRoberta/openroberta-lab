package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class GyroSensorTest {

    @Test
    public void sensorSetGyro() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-30, y=210], GyroSensor [mode=ANGLE, port=S2]], [Location [x=-26, y=250], GyroSensor [mode=RATE, port=S4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setGyro.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(GyroSensorMode.ANGLE, cs.getMode());
        Assert.assertEquals(GyroSensorMode.RATE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.S2, cs.getPort());
        Assert.assertEquals(SensorPort.S4, cs1.getPort());
    }

    @Test
    public void sensorResetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-13, y=105], GyroSensor [mode=RESET, port=S2]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_resetGyro.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_resetGyro.xml");
    }

}
