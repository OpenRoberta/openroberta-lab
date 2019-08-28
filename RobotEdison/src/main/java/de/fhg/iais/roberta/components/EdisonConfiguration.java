package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class EdisonConfiguration extends Configuration {

    private static final Configuration config;

    static {

        ConfigurationComponent leftMotor = new ConfigurationComponent("MOTOR", true, "LMOTOR", "LMOTOR", Collections.emptyMap());
        ConfigurationComponent rightMotor = new ConfigurationComponent("MOTOR", true, "RMOTOR", "RMOTOR", Collections.emptyMap());
        ConfigurationComponent leftLED = new ConfigurationComponent("LED", true, "LLED", "LLED", Collections.emptyMap());
        ConfigurationComponent rightLED = new ConfigurationComponent("LED", true, "RLED", "RLED", Collections.emptyMap());
        ConfigurationComponent irLED = new ConfigurationComponent("INFRARED", false, "IRLED", "IRLED", Collections.emptyMap());
        ConfigurationComponent obstacleDetector = new ConfigurationComponent("INFRARED", false, "OBSTACLEDETECTOR", "OBSTACLEDETECTOR", Collections.emptyMap());
        ConfigurationComponent lineTracker = new ConfigurationComponent("LIGHT", false, "LINETRACKER", "LINETRACKER", Collections.emptyMap());
        ConfigurationComponent leftLight = new ConfigurationComponent("LIGHT", false, "LLIGHT", "LLIGHT", Collections.emptyMap());
        ConfigurationComponent rightLight = new ConfigurationComponent("LIGHT", false, "RLIGHT", "RLIGHT", Collections.emptyMap());
        ConfigurationComponent sound = new ConfigurationComponent("SOUND", false, "TOP", "TOP", Collections.emptyMap());
        ConfigurationComponent playButton = new ConfigurationComponent("KEY", true, "PLAY", "PLAYKEY", Collections.emptyMap());
        ConfigurationComponent recordButton = new ConfigurationComponent("KEY", true, "REC", "RECKEY", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components =
            Lists
                .newArrayList(
                    leftMotor,
                    rightMotor,
                    leftLED,
                    rightLED,
                    irLED,
                    obstacleDetector,
                    lineTracker,
                    leftLight,
                    rightLight,
                    sound,
                    playButton,
                    recordButton);

        config = new Configuration.Builder().addComponents(components).build();
    }

    public EdisonConfiguration(Collection<ConfigurationComponent> configurationComponents, float wheelDiameterCM, float trackWidthCM) {
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
            return config;
        }
    }
}
