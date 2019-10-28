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

public class NxcListsTest extends NxtAstTest {

    public static ConfigurationAst makeConfiguration() {
        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor2 = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorC, ultrasonicSensor, ultrasonicSensor2));
        return builder.build();
    }

    @Test
    public void nxtListsGetSetFindTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/nxc_arrays_find_get_set_test.nxc",
                "/ast/lists/nxc_arrays_find_get_set_test.xml",
                makeConfiguration());
    }

    @Test
    public void nxtListsGetSetAllIndiciesTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/nxc_arrays_get_set_all_indices.nxc",
                "/ast/lists/nxc_arrays_get_set_all_indices.xml",
                makeConfiguration());
    }

}
