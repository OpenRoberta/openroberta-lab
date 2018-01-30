package de.fhg.iais.roberta.factory;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);
    protected final RobertaProperties robertaProperties;
    private final Properties robotProperties;

    public AbstractRobotFactory(RobertaProperties robertaProperties) {
        this.robertaProperties = robertaProperties;
        this.robotProperties = Util1.loadProperties("classpath:Robot.properties");
        addBlockTypesFromProperties("Robot.properties", this.robotProperties);
    }

    /**
     * should be called only from subclasses. Made public and static for tests (see call hierarchy). Another example, that shows, that static variables
     * (singletons) are bad. TODO: rewrite the BlockTypeContainer map singleton and inject
     *
     * @param propertyFileName
     * @param properties
     */
    public static void addBlockTypesFromProperties(String propertyFileName, Properties properties) {
        boolean alreadyLoaded = BlockTypeContainer.register(propertyFileName);
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
