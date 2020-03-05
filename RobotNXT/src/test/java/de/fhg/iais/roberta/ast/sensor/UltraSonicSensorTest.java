package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltraSonicSensorTest extends NxtAstTest {

    @Test
    public void sensorSetUltrasonic() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=57], UltrasonicSensor [4, DISTANCE, NO_SLOT]], [Location [x=1, y=98], UltrasonicSensor [2, PRESENCE, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setUltrasonic.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) forest.get(0).get(1);
        UltrasonicSensor<Void> cs1 = (UltrasonicSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.DISTANCE, cs.getMode());
        Assert.assertEquals(SC.PRESENCE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setUltrasonic.xml");

        UltrasonicSensor<Void> cs = (UltrasonicSensor<Void>) forest.get(0).get(1);
        UltrasonicSensor<Void> cs1 = (UltrasonicSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("4", cs.getPort());
        Assert.assertEquals("2", cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setUltrasonic.xml");
    }

}
