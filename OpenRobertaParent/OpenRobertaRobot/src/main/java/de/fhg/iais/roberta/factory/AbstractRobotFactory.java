package de.fhg.iais.roberta.factory;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);

    protected final String robotName;
    protected final Properties robotProperties;

    public AbstractRobotFactory(String robotName, Properties robotProperties) {
        this.robotName = robotName;
        this.robotProperties = robotProperties;

        Properties generalRobotProperties = Util1.loadProperties("classpath:Robot.properties");
        addBlockTypesFromProperties("Robot", generalRobotProperties);
        addBlockTypesFromProperties(robotName, this.robotProperties);
    }

    @Override
    public final String getGroup() {
        String group = this.robotProperties.getProperty("robot.plugin.group");
        return group != null ? group : this.robotName;
    }

    @Override
    public final String getProgramToolboxBeginner() {
        return this.robotProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public final String getProgramToolboxExpert() {
        return this.robotProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public final String getProgramDefault() {
        return this.robotProperties.getProperty("robot.program.default");
    }

    @Override
    public final String getConfigurationToolbox() {
        return this.robotProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public final String getConfigurationDefault() {
        return this.robotProperties.getProperty("robot.configuration.default");
    }

    @Override
    public final String getRealName() {
        return this.robotProperties.getProperty("robot.real.name");
    }

    @Override
    public final Boolean hasSim() {
        return this.robotProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public final String getInfo() {
        return this.robotProperties.getProperty("robot.info") != null ? this.robotProperties.getProperty("robot.info") : "#";
    }

    @Override
    public final Boolean isBeta() {
        return this.robotProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public final String getConnectionType() {
        return this.robotProperties.getProperty("robot.connection");
    }

    @Override
    public final String getVendorId() {
        return this.robotProperties.getProperty("robot.vendor");
    }

    @Override
    public final Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.robotProperties.getProperty("robot.configuration"));
    }

    @Override
    public final String getCommandline() {
        return this.robotProperties.getProperty("robot.connection.commandLine");
    }

    @Override
    public final String getSignature() {
        return this.robotProperties.getProperty("robot.connection.signature");
    }

    @Override
    public final String getMenuVersion() {
        return this.robotProperties.getProperty("robot.menu.verision");
    }

    /**
     * should be called only from subclasses. Made public and static for tests (see call hierarchy). Another example, that shows, that singletons are bad.<br>
     * TODO: rewrite the BlockTypeContainer map singleton and inject it with Guice
     *
     * @param robotName
     * @param properties
     */
    public static void addBlockTypesFromProperties(String robotName, Properties properties) {
        boolean alreadyLoaded = BlockTypeContainer.register(robotName);
        if ( alreadyLoaded ) {
            return;
        }
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
                Class<?> astClass;
                try {
                    astClass = AbstractRobotFactory.class.getClassLoader().loadClass(astClassName); // does the class exist?
                } catch ( Exception e ) {
                    LOG.error("AstClass \"{}\" of block type with key \"{}\" could not be loaded", astClassName, propertyKey);
                    throw new DbcException("Class not found", e);
                }
                String[] blocklyNames = Arrays.copyOfRange(attributes, 2, attributes.length);
                BlockTypeContainer.add(name, category, astClass, blocklyNames);
            }
        }
    }

}
