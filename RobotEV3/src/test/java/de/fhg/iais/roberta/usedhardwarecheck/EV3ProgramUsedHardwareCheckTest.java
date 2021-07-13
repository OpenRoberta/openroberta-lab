package de.fhg.iais.roberta.usedhardwarecheck;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.Ev3ValidatorAndCollectorVisitor;

public class EV3ProgramUsedHardwareCheckTest extends Ev3LejosAstTest {

    private void runTest(String pathToXml, String sensorResult, String actorResult) throws Exception {
        List<List<Phrase<Void>>> phrasesOfPhrases = UnitTestHelper.getProgramAst(testFactory, pathToXml);
        UsedHardwareBean.Builder builder = new Builder();
        Ev3ValidatorAndCollectorVisitor checkVisitor =
            new Ev3ValidatorAndCollectorVisitor(
                makeLargeLargeMediumTouchGyroColorUltrasonic(),
                ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> phrases : phrasesOfPhrases ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals(sensorResult, bean.getUsedSensors().toString());
        Assert.assertEquals(actorResult, bean.getUsedActors().toString());
    }

    @Test
    public void testIt() throws Exception {
        runTest("/visitors/hardware_check.xml", "[]", "[]");
        runTest("/visitors/hardware_check1.xml", "[]", "[]");
        runTest("/visitors/hardware_check2.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [3, COLOR, COLOUR]]", "[UsedActor [B, LARGE]]");
        runTest("/visitors/hardware_check3.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [4, ULTRASONIC, DISTANCE]]", "[UsedActor [B, LARGE]]");
        runTest("/visitors/hardware_check5.xml", "[]", "[UsedActor [B, LARGE]]");
        runTest(
            "/visitors/hardware_check6.xml",
            "[UsedSensor [3, COLOR, COLOUR], UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE]]",
            "[]");
        runTest("/ast/method_return_3.xml", "[]", "[]");
        runTest("/visitors/hardware_check7.xml", "[UsedSensor [3, COLOR, COLOUR], UsedSensor [3, COLOR, AMBIENTLIGHT], UsedSensor [4, COLOR, LIGHT]]", "[]");
        runTest("/visitors/hardware_check8.xml", "[]", "[UsedActor [D, MEDIUM]]");
    }
}
