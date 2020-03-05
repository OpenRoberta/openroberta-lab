package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class IRSeekerSensorTest extends AstTest {

    @Test
    public void sensorSetIRSeeker() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=258, y=138], IRSeekerSensor [1, MODULATED, NO_SLOT]], "
                + "[Location [x=262, y=196], IRSeekerSensor [1, UNMODULATED, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_getIRSeeker.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_getIRSeeker.xml");

        IRSeekerSensor<Void> cs = (IRSeekerSensor<Void>) forest.get(0).get(1);
        IRSeekerSensor<Void> cs1 = (IRSeekerSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.MODULATED, cs.getMode());
        Assert.assertEquals(SC.UNMODULATED, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_getIRSeeker.xml");

        IRSeekerSensor<Void> cs = (IRSeekerSensor<Void>) forest.get(0).get(1);
        IRSeekerSensor<Void> cs1 = (IRSeekerSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("1", cs.getPort());
        Assert.assertEquals("1", cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_getIRSeeker.xml");
    }
}
