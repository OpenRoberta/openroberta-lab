package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.test.ardu.HelperMBotForXmlTest;

public class MbotActorTest {
    HelperMBotForXmlTest mbotHelper = new HelperMBotForXmlTest();

    private final Configuration standardMbotConfiguration = makeMbotStandardConfiguration();

    private Configuration makeMbotStandardConfiguration() {
        Map<String, String> motorRightConfig = HelperMBotForXmlTest.createMap("MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorRight = new ConfigurationComponent("GEARED_MOTOR", true, "M2", BlocklyConstants.NO_SLOT, "2", motorRightConfig);
        Map<String, String> motorLeftConfig = HelperMBotForXmlTest.createMap("MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorleft = new ConfigurationComponent("GEARED_MOTOR", true, "M1", BlocklyConstants.NO_SLOT, "1", motorLeftConfig);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorleft, motorRight));
        return builder.build();
    }

    @Test
    public void mbotPlayNoteFrequencyTest() throws Exception {
        this.mbotHelper.compareExistingAndGeneratedSource(
            "/ast/actions/mbot_play_note_frequency_test.ino",
            "/ast/actions/mbot_play_note_frequency_test.xml",
            this.standardMbotConfiguration);
    }

    @Test
    public void mbotMotorsTest() throws Exception {
        this.mbotHelper.compareExistingAndGeneratedSource(
            "/ast/actions/mbot_motor_related_actions_test.ino",
            "/ast/actions/mbot_motor_related_actions_test.xml",
            this.standardMbotConfiguration);
    }

    @Test
    public void mbotSerialAndLedTest() throws Exception {
        this.mbotHelper.compareExistingAndGeneratedSource(
            "/ast/actions/mbot_serial_and_led_test.ino",
            "/ast/actions/mbot_serial_and_led_test.xml",
            this.standardMbotConfiguration);
    }

    @Test
    public void mbotSendReceiveMessageTest() throws Exception {
        this.mbotHelper.compareExistingAndGeneratedSource(
            "/ast/actions/mbot_send_receive_message_test.ino",
            "/ast/actions/mbot_send_receive_message_test.xml",
            this.standardMbotConfiguration);
    }
}
