package de.fhg.iais.roberta.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.PluginProperties;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);
    protected final PluginProperties pluginProperties;
    protected final BlocklyDropdownFactory blocklyDropdown2EnumFactory;

    public AbstractRobotFactory(PluginProperties pluginProperties) {

        this.pluginProperties = pluginProperties;
        this.blocklyDropdown2EnumFactory = new BlocklyDropdownFactory(this.pluginProperties);
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdown2EnumFactory;
    }

    @Override
    public final String getGroup() {
        String group = this.pluginProperties.getStringProperty("robot.plugin.group");
        return group != null ? group : this.pluginProperties.getRobotName();
    }

    @Override
    public final String getProgramToolboxBeginner() {
        return this.pluginProperties.getStringProperty("robot.program.toolbox.beginner");
    }

    @Override
    public final String getProgramToolboxExpert() {
        return this.pluginProperties.getStringProperty("robot.program.toolbox.expert");
    }

    @Override
    public final String getProgramDefault() {
        return this.pluginProperties.getStringProperty("robot.program.default");
    }

    @Override
    public final String getConfigurationToolbox() {
        return this.pluginProperties.getStringProperty("robot.configuration.toolbox");
    }

    @Override
    public final String getConfigurationDefault() {
        return this.pluginProperties.getStringProperty("robot.configuration.default");
    }

    @Override
    public final String getRealName() {
        return this.pluginProperties.getStringProperty("robot.real.name");
    }

    @Override
    public final Boolean hasSim() {
        return this.pluginProperties.getStringProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public final Boolean hasMultipleSim() {
        return this.pluginProperties.getStringProperty("robot.multisim") != null
            ? this.pluginProperties.getStringProperty("robot.multisim").equals("true")
            : false;
    }

    @Override
    public final String getInfo() {
        return this.pluginProperties.getStringProperty("robot.info") != null ? this.pluginProperties.getStringProperty("robot.info") : "#";
    }

    @Override
    public final Boolean isBeta() {
        return this.pluginProperties.getStringProperty("robot.beta") != null ? true : false; // TODO: a bit strange - consider robot.beta = false :-)
    }

    @Override
    public final String getConnectionType() {
        return this.pluginProperties.getStringProperty("robot.connection");
    }

    @Override
    public final String getVendorId() {
        return this.pluginProperties.getStringProperty("robot.vendor");
    }

    @Override
    public final Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.pluginProperties.getStringProperty("robot.configuration"));
    }

    @Override
    public final String getCommandline() {
        return this.pluginProperties.getStringProperty("robot.connection.commandLine");
    }

    @Override
    public final String getSignature() {
        return this.pluginProperties.getStringProperty("robot.connection.signature");
    }

    @Override
    public final String getMenuVersion() {
        return this.pluginProperties.getStringProperty("robot.menu.verision");
    }
}
