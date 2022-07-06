package de.fhg.iais.roberta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.Lists;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.ArduinoValidatorAndCollectorVisitor;

public class ArduinoUsedHardwareCollectorVisitorTest {
    private static RobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("uno", "", "", Util.loadProperties("classpath:/uno.properties")));
    }

    public static ConfigurationAst makeValidConfig() {
        ConfigurationComponent pinS = new ConfigurationComponent("PIN", false, "S", "S", Collections.emptyMap());
        ConfigurationComponent pinS2 = new ConfigurationComponent("PIN", false, "S2", "S2", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components = Lists.newArrayList(pinS, pinS2);

        return new ConfigurationAst.Builder().setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(components).build();
    }

    @Test
    public void testNormal() {
        List<List<Phrase>> phrases = UnitTestHelper.getProgramAst(testFactory, "/collector/all_helper_methods.xml");

        ConfigurationAst nanoConfig = makeValidConfig();
        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        ErrorAndWarningBean.Builder errorAndWarningBean = new ErrorAndWarningBean.Builder();
        NNBean.Builder nnBean = new NNBean.Builder();
        ImmutableClassToInstanceMap<IProjectBean.IBuilder> beanBuilders = ImmutableClassToInstanceMap.<IProjectBean.IBuilder> builder()
            .put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder)
            .put(UsedMethodBean.Builder.class, usedMethodBeanBuilder)
            .put(ErrorAndWarningBean.Builder.class, errorAndWarningBean)
            .put(NNBean.Builder.class, nnBean)
            .build();
        ArduinoValidatorAndCollectorVisitor visitor = new ArduinoValidatorAndCollectorVisitor(nanoConfig, beanBuilders);

        phrases.stream().flatMap(Collection::stream).forEach(voidPhrase -> voidPhrase.accept(visitor));

        Set<UsedSensor> usedSensors = usedHardwareBeanBuilder.build().getUsedSensors();
        Assertions.assertThat(usedSensors).hasSize(2);
    }

}