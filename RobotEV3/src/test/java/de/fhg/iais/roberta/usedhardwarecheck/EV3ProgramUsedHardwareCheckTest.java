package de.fhg.iais.roberta.usedhardwarecheck;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.Ev3ValidatorAndCollectorVisitor;

@RunWith(Parameterized.class)
public class EV3ProgramUsedHardwareCheckTest extends Ev3LejosAstTest {

    private final String file;
    private final String expectedActors;
    private final String expectedSensors;

    public EV3ProgramUsedHardwareCheckTest(String file, String expectedSensors, String expectedActors) {
        this.file = file;
        this.expectedSensors = expectedSensors;
        this.expectedActors = expectedActors;
    }

    @Parameterized.Parameters(name = "file: {0}, expected sensors: {1}, expected actors: {2}")
    public static Collection<Object[]> data() throws IOException {
        return Arrays.asList(
            new Object[] {"/visitors/hardware_check.xml", "[]", "[UsedActor [- EMPTY_PORT -, DISPLAY]]"},
            new Object[] {"/visitors/hardware_check1.xml", "[]", "[UsedActor [- EMPTY_PORT -, DISPLAY]]"},
            new Object[] {"/visitors/hardware_check2.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [3, COLOR, COLOUR]]", "[UsedActor [- EMPTY_PORT -, LIGHT], UsedActor [B, LARGE], UsedActor [- EMPTY_PORT -, SOUND]]"},
            new Object[] {"/visitors/hardware_check3.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [4, ULTRASONIC, DISTANCE]]", "[UsedActor [- EMPTY_PORT -, LIGHT], UsedActor [B, LARGE]]"},
            new Object[] {"/visitors/hardware_check5.xml", "[]", "[UsedActor [B, LARGE]]"},
            new Object[] {"/visitors/hardware_check6.xml", "[UsedSensor [3, COLOR, COLOUR], UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE]]", "[UsedActor [- EMPTY_PORT -, DISPLAY]]"},
            new Object[] {"/ast/method_return_3.xml", "[]", "[]"},
            new Object[] {"/visitors/hardware_check7.xml", "[UsedSensor [3, COLOR, COLOUR], UsedSensor [3, COLOR, AMBIENTLIGHT], UsedSensor [4, COLOR, LIGHT]]", "[]"},
            new Object[] {"/visitors/hardware_check8.xml", "[]", "[UsedActor [D, MEDIUM]]"}
        );
    }

    @Test
    public void testCollector() {
        List<List<Phrase>> phrasesOfPhrases = UnitTestHelper.getProgramAst(testFactory, file);
        UsedHardwareBean.Builder usedHardwareBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBuilder = new UsedMethodBean.Builder();
        ErrorAndWarningBean.Builder errorAndWarningBuilder = new ErrorAndWarningBean.Builder();
        NNBean.Builder nnBuilder = new NNBean.Builder();

        ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder> map = new ImmutableClassToInstanceMap.Builder<>();
        map.put(UsedMethodBean.Builder.class, usedMethodBuilder);
        map.put(UsedHardwareBean.Builder.class, usedHardwareBuilder);
        map.put(ErrorAndWarningBean.Builder.class, errorAndWarningBuilder);
        map.put(NNBean.Builder.class, nnBuilder);

        Ev3ValidatorAndCollectorVisitor checkVisitor =
            new Ev3ValidatorAndCollectorVisitor(
                makeLargeLargeMediumTouchGyroColorUltrasonic(),
                map.build(),
                false);
        for ( List<Phrase> phrases : phrasesOfPhrases ) {
            for ( Phrase phrase : phrases ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = usedHardwareBuilder.build();
        Assert.assertEquals(expectedSensors, bean.getUsedSensors().toString());
        Assert.assertEquals(expectedActors, bean.getUsedActors().toString());
    }
}
