package de.fhg.iais.roberta.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);
    protected final PluginProperties pluginProperties;
    protected final BlocklyDropdownFactory blocklyDropdown2EnumFactory;
    protected final String beginnerToolbox;
    protected final String expertToolbox;
    protected final String programDefault;
    protected final String configurationToolbox;
    protected final String configurationDefault;

    public AbstractRobotFactory(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
        this.blocklyDropdown2EnumFactory = new BlocklyDropdownFactory(this.pluginProperties);

        this.beginnerToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.beginner"));
        this.expertToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.expert"));
        this.programDefault = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.default"));
        this.configurationToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.toolbox"));
        this.configurationDefault = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.default"));
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
        return this.beginnerToolbox;
    }

    @Override
    public final String getProgramToolboxExpert() {
        return this.expertToolbox;
    }

    @Override
    public final String getProgramDefault() {
        return this.programDefault;
    }

    @Override
    public final String getConfigurationToolbox() {
        return this.configurationToolbox;
    }

    @Override
    public final String getConfigurationDefault() {
        return this.configurationDefault;
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

    public final void setConnectionType(String type) {
        this.pluginProperties.setStringProperty("robot.connection", type);
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
