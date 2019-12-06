package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MbotSensorTest extends MbotAstTest {

    private final ConfigurationAst standardMbotConfiguration = makeMbotStandardConfiguration();

    private ConfigurationAst makeMbotStandardConfiguration() {
        Map<String, String> motorRightConfig = Util.createMap("MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorRight = new ConfigurationComponent("GEARED_MOTOR", true, "M2", "2", motorRightConfig);
        Map<String, String> motorLeftConfig = Util.createMap("MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorleft = new ConfigurationComponent("GEARED_MOTOR", true, "M1", "1", motorLeftConfig);
        Map<String, String> a = Util.createMap("PORT_2", "PORT_2");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", false, "PORT_2", "ORT_2", a);
        Map<String, String> b = Util.createMap("PORT_3", "PORT_3");
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", false, "PORT_3", "ORT_3", b);             
        Map<String, String> c = Util.createMap("PORT_1", "PORT_1");
        ConfigurationComponent ledMatrix = new ConfigurationComponent("LED_MATRIX", false, "PORT_1", "ORT_1", c);             
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorleft, motorRight, infrared, ultrasonic, ledMatrix));
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
