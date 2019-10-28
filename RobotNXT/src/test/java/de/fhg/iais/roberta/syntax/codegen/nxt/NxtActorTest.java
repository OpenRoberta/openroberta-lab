package de.fhg.iais.roberta.syntax.codegen.nxt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NxtActorTest extends NxtAstTest {

    private static ConfigurationAst makeOtherConsumerConfiguration() {
        Map<String, String> otherPowerConsumerproperties = Util.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent otherPowerConsumer = new ConfigurationComponent("LARGE", true, "A", "A", otherPowerConsumerproperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(12f).setWheelDiameter(5.6f).addComponents(Arrays.asList(otherPowerConsumer));
        return builder.build();
    }

    public static ConfigurationAst makeDisplayConfiguration() {
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent soundSensor = new ConfigurationComponent("SOUND", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(12f).setWheelDiameter(5.6f).addComponents(Arrays.asList(touchSensor, soundSensor, colorSensor));
        return builder.build();
    }

    @Test
    public void nxtOtherPowerConsumerTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/nxt_other_consumer_test.nxc",
                "/ast/actions/nxt_other_consumer_test.xml",
                makeOtherConsumerConfiguration());
    }

    @Test
    public void nxtDisplayTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/nxt_display_all_datatypes_test.nxc",
                "/ast/actions/nxt_display_all_datatypes_test.xml",
                makeDisplayConfiguration());
    }
}
