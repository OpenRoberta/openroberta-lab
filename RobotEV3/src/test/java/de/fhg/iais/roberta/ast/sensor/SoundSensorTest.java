package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SoundSensorTest extends AstTest {

    @Test
    public void sensorSound() throws Exception {
        String a = "BlockAST [project=[[Location [x=460, y=156], SoundSensor [1, DEFAULT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_getSampleSound.xml");
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_getSampleSound.xml");

        SoundSensor<Void> cs = (SoundSensor<Void>) forest.get(0).get(1);

        Assert.assertEquals("1", cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_getSampleSound.xml");
    }
}
