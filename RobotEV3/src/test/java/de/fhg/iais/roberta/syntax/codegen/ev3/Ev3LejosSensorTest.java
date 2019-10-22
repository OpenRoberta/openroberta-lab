package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Ev3LejosSensorTest extends Ev3LejosAstTest {

    ConfigurationAst configuration = makeConfigurationWithHTSensors();

    public static ConfigurationAst makeConfigurationWithHTSensors() {
        ConfigurationComponent htCompasss = new ConfigurationComponent("COMPASS", false, "S1", "1", Collections.emptyMap());

        ConfigurationComponent htInfrared = new ConfigurationComponent("IRSEEKER", false, "S2", "2", Collections.emptyMap());

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(htCompasss, htInfrared));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3lejosV1");
        return configuration;
    }

    @Test
    public void ev3HtSensorTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/ev3_htsensors_test.java",
                "/ast/sensors/ev3_htsensors_test.xml",
                this.configuration);
    }
}
