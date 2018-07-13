package de.fhg.iais.roberta.components.wedo;

import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.util.Quadruplet;

public class WeDoConfiguration extends Configuration {

    protected final List<Quadruplet<ConfigurationBlock, String, List<String>, List<String>>> configurationBlocks;

    public WeDoConfiguration(List<Quadruplet<ConfigurationBlock, String, List<String>, List<String>>> configurationBlocks) {
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
                Quadruplet<ConfigurationBlock, String, List<String>, List<String>> block = this.configurationBlocks.get(i);
                sb.append("    ").append(block.getFirst()).append(", ").append("Name: ").append(block.getSecond());
                sb.append(", port list: ").append(block.getThird()).append(", pin list: ").append(block.getFourth()).append(";\n");
            }
            sb.append("  }\n");
        }
    }

    public List<Quadruplet<ConfigurationBlock, String, List<String>, List<String>>> getConfigurationBlocks() {
        return this.configurationBlocks;
    }

    public ConfigurationBlock getConfigurationBlock(Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock) {
        return configurationBlock.getFirst();
    }

    public ConfigurationBlockType getConfigurationBlockType(Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock) {
        return configurationBlock.getFirst().getType();
    }

    public String getBlockName(Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock) {
        return configurationBlock.getSecond();
    }

    public List<String> getPorts(Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock) {
        return configurationBlock.getThird();
    }

    public List<String> getPins(Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock) {
        return configurationBlock.getFourth();
    }

    public ConfigurationBlock getConfigurationBlockOnPort(String port) {
        for ( int i = 0; i < this.configurationBlocks.size(); i++ ) {
            if ( this.configurationBlocks.get(i).getSecond().toUpperCase().equals(port) ) {
                return this.configurationBlocks.get(i).getFirst();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BrickConfiguration [configuration blocks=" + this.configurationBlocks + "]";
    }

    public Configuration getConfiguration() {
        return new WeDoConfiguration(this.configurationBlocks);
    }
}
