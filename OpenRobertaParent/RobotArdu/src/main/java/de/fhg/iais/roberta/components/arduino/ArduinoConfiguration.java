package de.fhg.iais.roberta.components.arduino;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ArduinoConfiguration extends Configuration {

    protected final Map<String, ConfigurationBlock> configurationBlocks;

    public ArduinoConfiguration(Map<String, ConfigurationBlock> confBlocks) {
        super(null, null, -1, -1);
        this.configurationBlocks = confBlocks;
    }

    /**
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("board Arduino ").append(name).append(" {\n");
        generateConfigurationBlocks(sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * This class is a builder of {@link Configuration}
     * 
     * @param <IConfigurationType>
     */
    public static class Builder extends Configuration.Builder<Builder> {
        protected final Map<String, ConfigurationBlock> confBlocks = new HashMap<>();

        /**
         * Add configuration block to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param configuration block we want to connect
         * @return
         */
        public Builder addConfigurationBlock(ConfigurationBlockType type, String name, Map<String, String> confPorts) {
            this.confBlocks.put(name, new ConfigurationBlock(type, name, confPorts));
            return this;
        }

        @Override
        public Configuration build() {
            return new ArduinoConfiguration(this.confBlocks);
        }

        public Builder addConfigurationBlock(ConfigurationBlock configurationBlock) {
            this.confBlocks.put(configurationBlock.getConfName(),configurationBlock);
            return this;
        }
    }

    private void generateConfigurationBlocks(StringBuilder sb) {
        if ( this.configurationBlocks.size() > 1 ) {
            sb.append(" configuration blocks {\n");
            for ( int i = 1; i < this.configurationBlocks.size(); i++ ) {
                ConfigurationBlock block = this.configurationBlocks.get(i);
                //                sb.append("    ").append(block.getFirst()).append(", ").append("Name: ").append(block.getSecond());
                //                sb.append(", port list: ").append(block.getThird()).append(", pin list: ").append(block.getFourth()).append(";\n");
            }
            sb.append("  }\n");
        }
    }

    public Map<String, ConfigurationBlock> getConfigurationBlocks() {
        return this.configurationBlocks;
    }

    public ConfigurationBlockType getConfigurationBlockType(ConfigurationBlock configurationBlock) {
        return (ConfigurationBlockType) configurationBlock.getConfType();
    }

    public String getBlockName(ConfigurationBlock configurationBlock) {
        return configurationBlock.getConfName();
    }

    public Map getConfPorts(ConfigurationBlock configurationBlock) {
        return configurationBlock.getConfPorts();
    }

    public ConfigurationBlock getConfigurationBlockOnPort(String port) {
        if ( port == null || this.configurationBlocks.get(port) == null ) {
            throw new DbcException("No configuration block could be found with port name: " + port);
        }
        return this.configurationBlocks.get(port);
    }

    @Override
    public String toString() {
        return "BrickConfiguration [configuration blocks=" + this.configurationBlocks + "]";
    }

}
