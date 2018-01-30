package de.fhg.iais.roberta.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RobertaProperties {
    private static final Logger LOG = LoggerFactory.getLogger(RobertaProperties.class);
    public static final String NAME_OF_SIM = "sim";
    public static final String ROBOT_WHITE_LIST_PROPERTY_KEY = "robot.whitelist";
    public static final String ROBOT_DEFAULT_PROPERTY_KEY = "robot.default";
    public static final String PLUGIN_TEMPDIR_PROPERTY_KEY = "plugin.tempdir";

    private final Properties robertaProperties;
    private final String defaultRobot;
    private final List<String> robotsOnWhiteList;
    private final String tempDir;

    /**
     * store the final set of properties, that control the OpenRoberta system.<br>
     * <br>
     * After the ServerStarter has loaded the properties, merged with optional runtime properties, it calls this method with the (final) set of properties. They
     * are stored in this class. From here they can be retrieved and used elsewhere.<br>
     * <b>But note:</b> Getting properties from <b><i>Guice</i></b> is <b>preferred</b>
     *
     * @param properties
     */
    public RobertaProperties(Properties properties) {
        Assert.notNull(properties);
        robertaProperties = properties;

        // process the white list and get the default robot
        String whiteList = getStringProperty(ROBOT_WHITE_LIST_PROPERTY_KEY);
        Assert.notNull(whiteList, "Property \"" + ROBOT_WHITE_LIST_PROPERTY_KEY + "\" not found");
        String[] whiteListItems = whiteList.split("\\s*,\\s*");
        Assert.isTrue(whiteListItems.length >= 1, "Property \"" + ROBOT_WHITE_LIST_PROPERTY_KEY + "\" must contain at least one real robot");
        if ( whiteListItems[0].equals(NAME_OF_SIM) ) {
            Assert.isTrue(
                whiteListItems.length >= 2,
                "Property \"" + ROBOT_WHITE_LIST_PROPERTY_KEY + "\" must contain at least one robot different from \"" + NAME_OF_SIM + "\"");
            defaultRobot = whiteListItems[1];
        } else {
            defaultRobot = whiteListItems[0];
        }
        robotsOnWhiteList = Collections.unmodifiableList(Arrays.asList(whiteListItems));
        robertaProperties.put(ROBOT_DEFAULT_PROPERTY_KEY, defaultRobot);

        // made a robust choice about the temporary directory
        String tempTempDir = getStringProperty(PLUGIN_TEMPDIR_PROPERTY_KEY);
        if ( tempTempDir == null ) {
            tempDir = System.getProperty("java.io.tmpdir");
            Assert.notNull(tempDir, "could not allocate a temporary directory");
        } else if ( !(tempTempDir.endsWith("/") || tempTempDir.endsWith("\\")) ) {
            tempDir = tempTempDir + "/";
        } else {
            tempDir = tempTempDir;
        }
        robertaProperties.put(PLUGIN_TEMPDIR_PROPERTY_KEY, tempDir);
        LOG.info("As temporary directory " + tempDir + " will be used");
    }

    public Properties getRobertaProperties() {
        Assert.notNull(robertaProperties);
        return robertaProperties;
    }

    public String getStringProperty(String propertyName) {
        Assert.notNull(robertaProperties);
        return robertaProperties.getProperty(propertyName);
    }

    public int getIntProperty(String propertyName) {
        Assert.notNull(robertaProperties);
        String property = robertaProperties.getProperty(propertyName);
        return Integer.parseInt(property);
    }

    public boolean getBooleanProperty(String propertyName) {
        Assert.notNull(robertaProperties);
        String property = robertaProperties.getProperty(propertyName);
        return Boolean.parseBoolean(property);
    }

    public int getRobotNumberFromProperty(String robotName) {
        Assert.notNull(robertaProperties);
        for ( int i = 1; i < 1000; i++ ) {
            String value = robertaProperties.getProperty("robot.plugin." + i + ".name");
            if ( value == null ) {
                throw new DbcException("Robot with name: " + robotName + " not found!");
            }
            if ( value.equals(robotName) ) {
                return i;
            }
        }
        throw new DbcException("Only 999 robots supported!");
    }

    public List<String> getRobotWhitelist() {
        return robotsOnWhiteList;
    }

    public String getDefaultRobot() {
        return defaultRobot;
    }

    public String getTempDir() {
        return tempDir;
    }

    public String getTempDirFor(String kindOfTempDir) {
        return tempDir + kindOfTempDir + "/";
    }

    public String getTempDirForUserProjects() {
        return getTempDirFor("userProjects");
    }

}