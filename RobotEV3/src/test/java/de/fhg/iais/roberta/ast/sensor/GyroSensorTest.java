package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GyroSensorTest extends AstTest {

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-30, y=210], GyroSensor [2, ANGLE, NO_SLOT]], [Location [x=-26, y=250], GyroSensor [4, RATE, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) forest.get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.ANGLE, cs.getMode());
        Assert.assertEquals(SC.RATE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) forest.get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("2", cs.getPort());
        Assert.assertEquals("4", cs1.getPort());
    }

    @Test
    public void sensorResetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-13, y=105], GyroSensor [2, RESET, NO_SLOT]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_resetGyro.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_resetGyro.xml");
    }

}
