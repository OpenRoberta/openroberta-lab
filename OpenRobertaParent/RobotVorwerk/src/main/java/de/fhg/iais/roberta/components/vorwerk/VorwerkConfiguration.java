package de.fhg.iais.roberta.components.vorwerk;

import de.fhg.iais.roberta.components.Configuration;

public class VorwerkConfiguration extends Configuration {

    public VorwerkConfiguration() {
        super(null, null, 0, 0);
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder extends Configuration.Builder<Builder> {

        @Override
        public Configuration build() {
            return new VorwerkConfiguration();
        }

        @Override
        public String toString() {
            return "Builder []";
        }

    }

}
