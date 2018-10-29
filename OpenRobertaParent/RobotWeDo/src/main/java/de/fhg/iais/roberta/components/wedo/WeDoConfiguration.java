package de.fhg.iais.roberta.components.wedo;

import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class WeDoConfiguration extends Configuration {

    protected final Map<String,ConfigurationBlock> configurationBlocks;

    public WeDoConfiguration(Map<String,ConfigurationBlock> configurationBlocks) {
        super(null, null, -1, -1);
        this.configurationBlocks = configurationBlocks;
    }

    /**
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("board WeDo ").append(name).append(" {\n");
        generateConfigurationBlocks(sb);
        sb.append("}");
        return sb.toString();
    }

    private void generateConfigurationBlocks(StringBuilder sb) {
        if ( this.configurationBlocks.size() > 1 ) {
            sb.append(" configuration blocks {\n");
            for ( int i = 0; i < this.configurationBlocks.size(); i++ ) {
                ConfigurationBlock block = this.configurationBlocks.get(i);
                //                sb.append("    ").append(block.getFirst()).append(", ").append("Name: ").append(block.getSecond());
                //                sb.append(", port list: ").append(block.getThird()).append(", pin list: ").append(block.getFourth()).append(";\n");
            }
            sb.append("  }\n");
        }
    }

    public Map<String,ConfigurationBlock> getConfigurationBlocks() {
        return this.configurationBlocks;
    }

    public ConfigurationBlockType getConfigurationBlockType(ConfigurationBlock configurationBlock) {
        return (ConfigurationBlockType) configurationBlock.getConfType();
    }

    public String getBlockName(ConfigurationBlock configurationBlock) {
        return configurationBlock.getConfName();
    }

    public Map<String, String> getConfPorts(ConfigurationBlock configurationBlock) {
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

    public Configuration getConfiguration() {
        return new WeDoConfiguration(this.configurationBlocks);
    }
}
