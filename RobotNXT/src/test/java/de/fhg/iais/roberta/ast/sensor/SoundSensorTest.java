package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SoundSensorTest extends NxtAstTest {

    @Test
    public void sensorSound() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=338], SoundSensor [2, DEFAULT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_Sound.xml");
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_Sound.xml");

        SoundSensor<Void> cs = (SoundSensor<Void>) forest.get(0).get(1);

        Assert.assertEquals("2", cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_Sound.xml");
    }
}
