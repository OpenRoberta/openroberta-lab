package de.fhg.iais.roberta.syntax.check.hardware.nao;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.NaoValidatorAndCollectorVisitor;

@RunWith(Parameterized.class)
public class UsedHardwareCollectorVisitorTest extends NaoAstTest {

    private final String file;
    private final String expectedActors;
    private final String expectedSensors;

    public UsedHardwareCollectorVisitorTest(String file, String expectedSensors, String expectedActors) {
        this.file = file;
        this.expectedSensors = expectedSensors;
        this.expectedActors = expectedActors;
    }

    @Parameterized.Parameters(name = "file: {0}, expected sensors: {1}, expected actors: {0}")
    public static Collection<Object[]> data() throws IOException {
        return Arrays.asList(
            new Object[] {"/hardwarecheck/learnface.xml", "[UsedSensor [null, NAO_FACE, null]]", "[]"},
            new Object[] {"/hardwarecheck/forgetface.xml", "[UsedSensor [null, NAO_FACE, null]]", "[]"},
            new Object[] {"/hardwarecheck/getnaomarkinfo.xml", "[UsedSensor [null, DETECT_MARK, null]]", "[]"},
            new Object[] {"/hardwarecheck/detectnaomark.xml", "[UsedSensor [null, DETECT_MARK, null]]", "[]"},
            new Object[] {"/hardwarecheck/moveblocks.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/walkdistance.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/turnaction.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/walktoXYTheta1.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/walktoXYTheta2.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/walktoXYTheta3.xml", "[UsedSensor [- EMPTY_PORT -, ULTRASONIC, DISTANCE]]", "[]"},
            new Object[] {"/hardwarecheck/speech.xml", "[UsedSensor [null, NAO_SPEECH, null]]", "[]"}
        );
    }

    @Test
    public void testCollector() {
        List<List<Phrase<Void>>> phrasesOfPhrases = UnitTestHelper.getProgramAst(testFactory, file);
        UsedHardwareBean.Builder usedHardwareBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBuilder = new UsedMethodBean.Builder();
        ErrorAndWarningBean.Builder errorAndWarningBuilder = new ErrorAndWarningBean.Builder();

        ImmutableClassToInstanceMap.Builder<IProjectBean.IBuilder<?>> map = new ImmutableClassToInstanceMap.Builder<>();
        map.put(UsedMethodBean.Builder.class, usedMethodBuilder);
        map.put(UsedHardwareBean.Builder.class, usedHardwareBuilder);
        map.put(ErrorAndWarningBean.Builder.class, errorAndWarningBuilder);

        NaoValidatorAndCollectorVisitor checkVisitor =
            new NaoValidatorAndCollectorVisitor(
                makeStandard(),
                map.build());
        for ( List<Phrase<Void>> phrases : phrasesOfPhrases ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = usedHardwareBuilder.build();
        Assert.assertEquals(expectedSensors, bean.getUsedSensors().toString());
        Assert.assertEquals(expectedActors, bean.getUsedActors().toString());
    }
}
