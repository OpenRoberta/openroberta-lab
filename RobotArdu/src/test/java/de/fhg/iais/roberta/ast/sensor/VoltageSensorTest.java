package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.util.test.Helper;

public class VoltageSensorTest {
    Helper h = new Helper();
    ArduFactory robotFactory = new ArduFactory();

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=38, y=238], VoltageSensor []]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_Voltage.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_Voltage.xml");
    }

}
