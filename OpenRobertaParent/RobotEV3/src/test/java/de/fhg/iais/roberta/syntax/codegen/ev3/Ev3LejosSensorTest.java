package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class Ev3LejosSensorTest {
    private final HelperEv3ForXmlTest ev3lejosHelper = new HelperEv3ForXmlTest();
    Configuration configuration = makeConfigurationWithHTSensors();

    public static Configuration makeConfigurationWithHTSensors() {
        ConfigurationComponent htCompasss = new ConfigurationComponent("COMPASS", false, "S1", BlocklyConstants.NO_SLOT, "1", Collections.emptyMap());

        ConfigurationComponent htInfrared = new ConfigurationComponent("IRSEEKER", false, "S2", BlocklyConstants.NO_SLOT, "2", Collections.emptyMap());

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(htCompasss, htInfrared));
        Configuration configuration = builder.build();
        configuration.setRobotName("ev3lejosV1");
        return configuration;
    }

    @Test
    public void ev3HtSensorTest() throws Exception {
        this.ev3lejosHelper
            .compareExistingAndGeneratedJavaSource("ast/sensors/ev3_htsensors_test.java", "/ast/sensors/ev3_htsensors_test.xml", this.configuration);
    }
}
