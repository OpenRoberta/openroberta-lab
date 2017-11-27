package de.fhg.iais.roberta.components.mbed;

import java.util.HashMap;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public class CalliopeConfiguration extends Configuration {

    public CalliopeConfiguration() {
        super(null, new HashMap<ISensorPort, Sensor>(), 0, 0);
    }

    /**
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        final StringBuilder sb = new StringBuilder();
        sb.append("robot calliope ").append(name).append(" {\n");

        sb.append("}");
        return sb.toString();
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder extends Configuration.Builder<Builder> {

        @Override
        public Configuration build() {
            return new CalliopeConfiguration();
        }

        @Override
        public String toString() {
            return "Builder []";
        }

    }

}
