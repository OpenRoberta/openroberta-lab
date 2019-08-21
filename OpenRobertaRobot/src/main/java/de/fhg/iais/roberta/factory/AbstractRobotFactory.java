package de.fhg.iais.roberta.factory;

import org.json.JSONObject;

import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractRobotFactory implements IRobotFactory {

    private static final String DEFAULT_HELPER_FILE = "classpath:/helperMethodsCommon.yml"; // TODO is this nice?

    protected final PluginProperties pluginProperties;
    protected final BlocklyDropdownFactory blocklyDropdown2EnumFactory;
    protected final String beginnerToolbox;
    protected final String expertToolbox;
    protected final String programDefault;
    protected final String configurationToolbox;
    protected final String configurationDefault;
    protected final HelperMethodGenerator helperMethodGenerator;

    public AbstractRobotFactory(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
        this.blocklyDropdown2EnumFactory = new BlocklyDropdownFactory(this.pluginProperties);

        this.beginnerToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.beginner"));
        this.expertToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.expert"));
        this.programDefault = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.program.default"));
        this.configurationToolbox = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.toolbox"));
        this.configurationDefault = Util1.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.default"));

        JSONObject helperMethods = new JSONObject();
        String helperMethodFile = this.pluginProperties.getStringProperty("robot.helperMethods");
        helperMethodFile = helperMethodFile == null ? DEFAULT_HELPER_FILE : helperMethodFile;
        Util1.loadYAMLRecursive("", helperMethods, helperMethodFile, true);
        this.helperMethodGenerator = new HelperMethodGenerator(helperMethods, getLanguageFromFileExtension());
    }

    @Override
    public PluginProperties getPluginProperties() {
        return this.pluginProperties;
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
        return this.pluginProperties.getStringProperty("robot.sim").equals("true");
    }

    @Override
    public final Boolean hasMultipleSim() {
        return this.pluginProperties.getStringProperty("robot.multisim") != null && this.pluginProperties.getStringProperty("robot.multisim").equals("true");
    }

    @Override
    public final String getInfo() {
        return this.pluginProperties.getStringProperty("robot.info") != null ? this.pluginProperties.getStringProperty("robot.info") : "#";
    }

    @Override
    public final Boolean isBeta() {
        return this.pluginProperties.getStringProperty("robot.beta") != null;
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
        return this.pluginProperties.getStringProperty("robot.menu.version");
    }

    @Override
    public final Boolean hasWlanCredentials() {
        return this.pluginProperties.getStringProperty("robot.haswlan") != null;
    }

    @Override
    public HelperMethodGenerator getHelperMethodGenerator() {
        return this.helperMethodGenerator;
    }

    // TODO remove this in the future -> just add a method "getLanguage()" to IRobotFactory? this is less intrusive for now
    private HelperMethodGenerator.Language getLanguageFromFileExtension() {
        String ext = getFileExtension();

        switch ( ext ) {
            case "java":
                return HelperMethodGenerator.Language.JAVA;
            case "py":
                return HelperMethodGenerator.Language.PYTHON;
            case "cpp":
            case "ino":
            case "nxc":
                return HelperMethodGenerator.Language.C;
            case "json":
                return HelperMethodGenerator.Language.JSON;
            default:
                throw new DbcException("File extension not implemented!");
        }
    }
}
