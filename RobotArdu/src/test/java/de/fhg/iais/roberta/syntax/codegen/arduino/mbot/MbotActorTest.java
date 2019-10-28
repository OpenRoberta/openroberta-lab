package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MbotActorTest extends MbotAstTest {

    private final ConfigurationAst standardMbotConfiguration = makeMbotStandardConfiguration();

    private ConfigurationAst makeMbotStandardConfiguration() {
        Map<String, String> motorRightConfig = Util.createMap("MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorRight = new ConfigurationComponent("GEARED_MOTOR", true, "M2", "2", motorRightConfig);
        Map<String, String> motorLeftConfig = Util.createMap("MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorleft = new ConfigurationComponent("GEARED_MOTOR", true, "M1", "1", motorLeftConfig);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorleft, motorRight));
        return builder.build();
    }

    @Test
    public void mbotPlayNoteFrequencyTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/mbot_play_note_frequency_test.ino",
                "/ast/actions/mbot_play_note_frequency_test.xml",
                this.standardMbotConfiguration);
    }

    @Test
    public void mbotMotorsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/mbot_motor_related_actions_test.ino",
                "/ast/actions/mbot_motor_related_actions_test.xml",
                this.standardMbotConfiguration);
    }

    @Test
    public void mbotSerialAndLedTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/mbot_serial_and_led_test.ino",
                "/ast/actions/mbot_serial_and_led_test.xml",
                this.standardMbotConfiguration);
    }

    @Test
    public void mbotSendReceiveMessageTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/mbot_send_receive_message_test.ino",
                "/ast/actions/mbot_send_receive_message_test.xml",
                this.standardMbotConfiguration);
    }
}
