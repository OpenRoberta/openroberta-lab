package de.fhg.iais.roberta.syntax.check.hardware.nao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.collect.NaoUsedHardwareCollectorVisitor;

public class UsedHardwareCollectorVisitorTest extends NaoAstTest {

    private ConfigurationAst makeConfiguration() {
        return new ConfigurationAst.Builder().build();
    }

    @Test
    public void testLearnFace_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/learnface.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_FACE, null]]", bean.getUsedSensors().toString());

    }

    @Test
    public void testForgetFace_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/forgetface.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_FACE, null]]", bean.getUsedSensors().toString());

    }

    @Test
    public void testGetNaoMarkInfo_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/getnaomarkinfo.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, DETECT_MARK, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testDetectNaoMark_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/detectnaomark.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, DETECT_MARK, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testGetRecogniezdWordFromList_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/getrecognizedwordfromlist.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_SPEECH, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testAllMoveBlocks_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/moveblocks.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkDistance_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/walkdistance.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testTurnAction_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/turnaction.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToX_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/walktoXYTheta1.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToY_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/walktoXYTheta2.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));

        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToTheta_returnsListWithOneUsedSensor() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/hardwarecheck/walktoXYTheta3.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(phrases, makeConfiguration(), ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }
}
