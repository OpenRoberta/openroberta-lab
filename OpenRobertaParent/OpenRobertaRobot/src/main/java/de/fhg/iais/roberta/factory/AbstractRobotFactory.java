package de.fhg.iais.roberta.factory;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);
    protected final PluginProperties pluginProperties;
    protected final BlocklyDropdownFactory blocklyDropdown2EnumFactory;

    public AbstractRobotFactory(PluginProperties pluginProperties) {

        this.pluginProperties = pluginProperties;
        this.blocklyDropdown2EnumFactory = new BlocklyDropdownFactory(this.pluginProperties);

        Properties generalpluginProperties = Util1.loadProperties("classpath:Robot.properties");
        addBlockTypesFromProperties(generalpluginProperties);
        addBlockTypesFromProperties(pluginProperties.getPluginProperties());
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

    /**
     * should be called only from subclasses. Made public and static for tests (see call hierarchy). Another example, that shows, that singletons are bad.<br>
     * TODO: refactor to avoid the singleton
     *
     * @param properties
     */
    public static void addBlockTypesFromProperties(Properties properties) {
        for ( Entry<Object, Object> property : properties.entrySet() ) {
            String propertyKey = (String) property.getKey();
            if ( propertyKey.startsWith("blockType.") ) {
                String name = propertyKey.substring(10);
                String propertyValue = (String) property.getValue();
                String[] attributes = propertyValue.split(",");
                Assert.isTrue(attributes.length >= 3, "Invalid block type property with key: %s", propertyKey);
                String astClassName = attributes[1];
                Category category = Category.valueOf(attributes[0]); // does the category exist?
                Assert.notNull(category);
                Class<?> astClass = null;
                try {
                    astClass = AbstractRobotFactory.class.getClassLoader().loadClass(astClassName); // does the class exist?
                } catch ( Exception e ) {
                    //                    LOG.error("AstClass \"{}\" of block type with key \"{}\" could not be loaded", astClassName, propertyKey);
                    //                    throw new DbcException("Class not found", e)
                }
                String[] blocklyNames = Arrays.copyOfRange(attributes, 2, attributes.length);
                BlockTypeContainer.add(name, category, astClass, blocklyNames);
            }
        }
    }
}
