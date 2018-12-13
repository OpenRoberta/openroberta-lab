package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.test.ardu.HelperArduinoForXmlTest;
import de.fhg.iais.roberta.util.test.ardu.HelperMBotForXmlTest;

public class MbotSensorTest {
    HelperMBotForXmlTest mbotHelper = new HelperMBotForXmlTest();

    private final Configuration standardMbotConfiguration = makeMbotStandardConfiguration();

    private Configuration makeMbotStandardConfiguration() {
        Map<String, String> motorRightConfig = HelperArduinoForXmlTest.createMap("MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorRight = new ConfigurationComponent("GEARED_MOTOR", true, "M2", BlocklyConstants.NO_SLOT, "2", motorRightConfig);
        Map<String, String> motorLeftConfig = HelperArduinoForXmlTest.createMap("MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorleft = new ConfigurationComponent("GEARED_MOTOR", true, "M1", BlocklyConstants.NO_SLOT, "1", motorLeftConfig);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorleft, motorRight));
        return builder.build();
    }

    @Test
    public void mbotSensorsTest() throws Exception {
        this.mbotHelper
            .compareExistingAndGeneratedSource("ast/sensors/mbot_sensors_test.ino", "/ast/sensors/mbot_sensors_test.xml", this.standardMbotConfiguration);
    }
}
