package de.fhg.iais.roberta.components;

public class CalliopeConfiguration extends Configuration {

    public CalliopeConfiguration() {
        super(null, null, 0, 0);
    }

    /**
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        StringBuilder sb = new StringBuilder();
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
