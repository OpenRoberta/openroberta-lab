package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MbotSensorTest extends MbotAstTest {

    private final ConfigurationAst standardMbotConfiguration = makeMbotStandardConfiguration();

    private ConfigurationAst makeMbotStandardConfiguration() {
        Map<String, String> motorRightConfig = createMap("MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorRight = new ConfigurationComponent("GEARED_MOTOR", true, "M2", "2", motorRightConfig);
        Map<String, String> motorLeftConfig = createMap("MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorleft = new ConfigurationComponent("GEARED_MOTOR", true, "M1", "1", motorLeftConfig);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorleft, motorRight));
        return builder.build();
    }

    @Test
    public void mbotSensorsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/mbot_sensors_test.ino",
                "/ast/sensors/mbot_sensors_test.xml",
                this.standardMbotConfiguration);
    }
}
