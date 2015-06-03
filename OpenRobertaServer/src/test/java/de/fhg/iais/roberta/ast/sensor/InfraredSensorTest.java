package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.codegen.lejos.Helper;

public class InfraredSensorTest {

    @Test
    public void sensorSetInfrared() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-23, y=157], InfraredSensor [mode=DISTANCE, port=S4]], "
                + "[Location [x=-19, y=199], InfraredSensor [mode=SEEK, port=S3]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(InfraredSensorMode.DISTANCE, cs.getMode());
        Assert.assertEquals(InfraredSensorMode.SEEK, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) transformer.getTree().get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.S4, cs.getPort());
        Assert.assertEquals(SensorPort.S3, cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setInfrared.xml");
    }
}
