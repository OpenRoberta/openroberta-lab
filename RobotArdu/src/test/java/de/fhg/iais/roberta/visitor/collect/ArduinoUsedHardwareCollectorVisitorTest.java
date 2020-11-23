package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.Lists;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;

public class ArduinoUsedHardwareCollectorVisitorTest extends ArduinoAstTest {

    public static final String COLLECTOR_ALL_HELPER_METHODS_XML = "/collector/all_helper_methods.xml";

    public static ConfigurationAst makeInvalidConfig() {
        ConfigurationComponent pinS2 = new ConfigurationComponent("PIN", false, "S2", "S2", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components = Lists.newArrayList(pinS2);

        return new ConfigurationAst.Builder().setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(components).build();
    }

    public static ConfigurationAst makeValidConfig() {
        ConfigurationComponent pinS = new ConfigurationComponent("PIN", false, "S", "S", Collections.emptyMap());
        ConfigurationComponent pinS2 = new ConfigurationComponent("PIN", false, "S2", "S2", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components = Lists.newArrayList(pinS, pinS2);

        return new ConfigurationAst.Builder().setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(components).build();
    }

    @Test
    public void testInconsistency() {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, COLLECTOR_ALL_HELPER_METHODS_XML);

        ConfigurationAst nanoConfig = makeInvalidConfig();
        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        ImmutableClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders =
            ImmutableClassToInstanceMap.<IProjectBean
                .IBuilder<?>> builder().put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder).put(UsedMethodBean.Builder.class, usedMethodBeanBuilder).build();
        ArduinoUsedHardwareCollectorVisitor visitor = new ArduinoUsedHardwareCollectorVisitor(nanoConfig, beanBuilders);

        Assertions.assertThatThrownBy(() -> {
            phrases.stream()
                .flatMap(Collection::stream)
                .forEach(voidPhrase -> voidPhrase.accept(visitor));
        })
            .isInstanceOf(DbcException.class)
            .hasMessageContaining("Inconsistent");
    }


    @Test
    public void testNormal() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/collector/all_helper_methods.xml");

        ConfigurationAst nanoConfig = makeValidConfig();
        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        ImmutableClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders =
            ImmutableClassToInstanceMap.<IProjectBean
                .IBuilder<?>> builder().put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder).put(UsedMethodBean.Builder.class, usedMethodBeanBuilder).build();
        ArduinoUsedHardwareCollectorVisitor visitor = new ArduinoUsedHardwareCollectorVisitor(nanoConfig, beanBuilders);

        phrases.stream()
            .flatMap(Collection::stream)
            .forEach(voidPhrase -> voidPhrase.accept(visitor));

        Set<UsedSensor> usedSensors = usedHardwareBeanBuilder.build().getUsedSensors();
        Assertions.assertThat(usedSensors)
            .hasSize(2);
    }

}