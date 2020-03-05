package de.fhg.iais.roberta.syntax.check.hardware.edison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.Lists;

import de.fhg.iais.roberta.ast.EdisonAstTest;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;

public class UsedHardwareCollectorVisitorTest extends EdisonAstTest {

    public static ConfigurationAst makeConfig() {
        ConfigurationComponent leftMotor = new ConfigurationComponent("MOTOR", true, "LMOTOR", "LMOTOR", Collections.emptyMap());
        ConfigurationComponent rightMotor = new ConfigurationComponent("MOTOR", true, "RMOTOR", "RMOTOR", Collections.emptyMap());
        ConfigurationComponent leftLED = new ConfigurationComponent("LED", true, "LLED", "LLED", Collections.emptyMap());
        ConfigurationComponent rightLED = new ConfigurationComponent("LED", true, "RLED", "RLED", Collections.emptyMap());
        ConfigurationComponent irLED = new ConfigurationComponent("INFRARED", false, "IRLED", "IRLED", Collections.emptyMap());
        ConfigurationComponent obstacleDetector = new ConfigurationComponent("INFRARED", false, "OBSTACLEDETECTOR", "OBSTACLEDETECTOR", Collections.emptyMap());
        ConfigurationComponent lineTracker = new ConfigurationComponent("LIGHT", false, "LINETRACKER", "LINETRACKER", Collections.emptyMap());
        ConfigurationComponent leftLight = new ConfigurationComponent("LIGHT", false, "LLIGHT", "LLIGHT", Collections.emptyMap());
        ConfigurationComponent rightLight = new ConfigurationComponent("LIGHT", false, "RLIGHT", "RLIGHT", Collections.emptyMap());
        ConfigurationComponent sound = new ConfigurationComponent("SOUND", true, "SOUND", "SOUND", Collections.emptyMap());
        ConfigurationComponent playButton = new ConfigurationComponent("KEY", true, "PLAY", "PLAYKEY", Collections.emptyMap());
        ConfigurationComponent recordButton = new ConfigurationComponent("KEY", true, "REC", "RECKEY", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components =
            Lists
                .newArrayList(
                    leftMotor,
                    rightMotor,
                    leftLED,
                    rightLED,
                    irLED,
                    obstacleDetector,
                    lineTracker,
                    leftLight,
                    rightLight,
                    sound,
                    playButton,
                    recordButton);

        return new ConfigurationAst.Builder().setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(components).build();
    }

    @Test
    // TODO this test does nothing
    public void TestAllHelperMethods() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/collector/all_helper_methods.xml");
        ConfigurationAst edisonConfig = makeConfig();
        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        ImmutableClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders =
            ImmutableClassToInstanceMap.<IProjectBean
                .IBuilder<?>> builder().put(Builder.class, usedHardwareBeanBuilder).put(UsedMethodBean.Builder.class, usedMethodBeanBuilder).build();
        EdisonUsedHardwareCollectorVisitor checker = new EdisonUsedHardwareCollectorVisitor(edisonConfig, beanBuilders);
    }
}