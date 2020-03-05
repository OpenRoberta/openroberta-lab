package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CompassSensorTest extends AstTest {

    @Test
    public void sensorGetCompass() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=384, y=50], CompassSensor [2, ANGLE, NO_SLOT]], "
                + "[Location [x=384, y=100], CompassSensor [4, COMPASS, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_getCompass.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_getCompass.xml");

        CompassSensor<Void> cs = (CompassSensor<Void>) forest.get(0).get(1);
        CompassSensor<Void> cs1 = (CompassSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.ANGLE, cs.getMode());
        Assert.assertEquals(SC.COMPASS, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_getCompass.xml");

        CompassSensor<Void> cs = (CompassSensor<Void>) forest.get(0).get(1);
        CompassSensor<Void> cs1 = (CompassSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("2", cs.getPort());
        Assert.assertEquals("4", cs1.getPort());
    }

    @Test
    public void sensorCalibrateCompass() throws Exception {
        String a = "BlockAST [project=[[Location [x=384, y=50], CompassSensor [1, CALIBRATE, NO_SLOT]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_calibrateCompass.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_getCompass.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_calibrateCompass.xml");
    }

}
