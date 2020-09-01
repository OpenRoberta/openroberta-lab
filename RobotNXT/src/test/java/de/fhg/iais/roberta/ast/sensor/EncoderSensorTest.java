package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class EncoderSensorTest extends NxtAstTest {

    @Test
    public void sensorSetEncoder() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-20, y=94], EncoderSensor [A, ROTATION, NO_SLOT]], "
                + "[Location [x=-15, y=129], EncoderSensor [C, DEGREE, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) forest.get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.ROTATION, cs.getMode());
        Assert.assertEquals(SC.DEGREE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) forest.get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("A", cs.getPort());
        Assert.assertEquals("C", cs1.getPort());
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-40, y=105], EncoderSensor [A, RESET, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_resetEncoder.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_resetEncoder.xml");
    }
}
