package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class MicrobitConfiguration extends Configuration {
    private static final Configuration configuration;
    static {
        ConfigurationComponent pin0 = new ConfigurationComponent("pin0", false, "0", "NO_SLOT", "0", Collections.emptyMap());
        ConfigurationComponent pin1 = new ConfigurationComponent("pin1", false, "1", "NO_SLOT", "1", Collections.emptyMap());
        ConfigurationComponent pin2 = new ConfigurationComponent("pin2", false, "2", "NO_SLOT", "2", Collections.emptyMap());
        ConfigurationComponent pin3 = new ConfigurationComponent("pin3", false, "3", "NO_SLOT", "3", Collections.emptyMap());
        ConfigurationComponent button_a = new ConfigurationComponent("button_a", false, "button_a", "NO_SLOT", "button_a", Collections.emptyMap());
        ConfigurationComponent button_b = new ConfigurationComponent("button_b", false, "button_b", "NO_SLOT", "button_b", Collections.emptyMap());
        ConfigurationComponent X = new ConfigurationComponent("X", false, "x", "NO_SLOT", "X", Collections.emptyMap());
        ConfigurationComponent Y = new ConfigurationComponent("Y", false, "y", "NO_SLOT", "Y", Collections.emptyMap());
        ConfigurationComponent Z = new ConfigurationComponent("Z", false, "z", "NO_SLOT", "Z", Collections.emptyMap());
        ConfigurationComponent STRENGTH = new ConfigurationComponent("STRENGTH", false, "STRENGTH", "NO_SLOT", "STRENGTH", Collections.emptyMap());
        ConfigurationComponent NO_PORT = new ConfigurationComponent("NO_PORT", false, "NO_PORT", "NO_SLOT", "NO_PORT", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components =
            Lists
                .newArrayList(
                    pin0,
                    pin1,
                    pin2,

                    pin3,

                    X,
                    Y,
                    Z,
                    STRENGTH,
                    NO_PORT,
                    button_a,
                    button_b);
        configuration = new Configuration.Builder().addComponents(components).build();
    }

    public MicrobitConfiguration(Collection<ConfigurationComponent> configurationComponents, float wheelDiameterCM, float trackWidthCM) {
        super(configurationComponents, wheelDiameterCM, trackWidthCM);

    }

    public static class Builder extends Configuration.Builder {

        @Override
        public Builder addComponents(List<ConfigurationComponent> components) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Builder setWheelDiameter(float wheelDiameter) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Builder setTrackWidth(float trackWidth) {
            throw new DbcException("Unsupported operation!");
        }

        @Override
        public Configuration build() {
            return configuration;
        }
    }

}
