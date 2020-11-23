package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;

public class ArduTestHelper {
    private ArduTestHelper() {
        // only static helpers
    }

    public static ConfigurationAst mkConfigurationAst(ConfigurationComponent... components) {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(components));
        final ConfigurationAst configuration = builder.build();
        configuration.setRobotName("nano");
        return configuration;
    }

}
