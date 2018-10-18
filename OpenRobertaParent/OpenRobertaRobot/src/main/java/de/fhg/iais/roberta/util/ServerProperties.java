package de.fhg.iais.roberta.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;

public class ServerProperties {
    private static final Logger LOG = LoggerFactory.getLogger(ServerProperties.class);
    public static final String NAME_OF_SIM = "sim";
    public static final String WHITE_LIST_KEY = "robot.whitelist";
    public static final String ROBOT_DEFAULT_PROPERTY_KEY = "robot.default";
    public static final String PLUGIN_TEMPDIR_PROPERTY_KEY = "plugin.tempdir";
    public static final String PLUGIN_RESOURCE_PROPERTY_KEY = "plugin.resourcedir";

    private final Properties serverProperties;
    private final String defaultRobot;
    private final List<String> robotsOnWhiteList;
    private final String resourceDir;
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
    public ServerProperties(Properties properties) {
        Assert.notNull(properties);
        this.serverProperties = properties;

        // process the white list and get the default robot
        String whiteList = getStringProperty(WHITE_LIST_KEY);
        Assert.notNull(whiteList, "Property \"" + WHITE_LIST_KEY + "\" not found");
        String[] whiteListItems = whiteList.split("\\s*,\\s*");
        Assert.isTrue(whiteListItems.length >= 1, "Property \"" + WHITE_LIST_KEY + "\" must contain at least one real robot");
        String defaultRobot = getStringProperty(ROBOT_DEFAULT_PROPERTY_KEY);
        if ( defaultRobot != null ) {
            this.defaultRobot = defaultRobot;
        } else if ( whiteListItems[0].equals(NAME_OF_SIM) ) {
            if ( whiteListItems.length < 2 ) {
                String errorMsg = "Property \"" + WHITE_LIST_KEY + "\" must contain at least one robot different from \"" + NAME_OF_SIM + "\"";
                Assert.fail(errorMsg);
            }
            this.defaultRobot = whiteListItems[1];
        } else {
            this.defaultRobot = whiteListItems[0];
        }
        this.robotsOnWhiteList = Collections.unmodifiableList(Arrays.asList(whiteListItems));
        this.serverProperties.put(ROBOT_DEFAULT_PROPERTY_KEY, this.defaultRobot);

        // made a robust choice about the plugin resource directory
        String resourceDir = getStringProperty(PLUGIN_RESOURCE_PROPERTY_KEY);
        String suffix = "";
        if ( resourceDir == null ) {
            suffix = "OpenRobertaParent/";
            resourceDir = System.getProperty("user.dir");
            Assert.notNull(resourceDir, "could not get the 'user.dir' property");
        }
        if ( !(resourceDir.endsWith("/") || resourceDir.endsWith("\\")) ) {
            resourceDir = resourceDir + "/";
        }
        this.resourceDir = resourceDir + suffix;
        this.serverProperties.put(PLUGIN_RESOURCE_PROPERTY_KEY, this.resourceDir);
        LOG.info("As resource directory " + this.resourceDir + " will be used");

        // made a robust choice about the plugin temporary directory
        String tempTempDir = getStringProperty(PLUGIN_TEMPDIR_PROPERTY_KEY);
        if ( tempTempDir == null ) {
            tempTempDir = System.getProperty("java.io.tmpdir");
            Assert.notNull(tempTempDir, "could not allocate a temporary directory");
        }
        if ( !(tempTempDir.endsWith("/") || tempTempDir.endsWith("\\")) ) {
            this.tempDir = tempTempDir + "/";
        } else {
            this.tempDir = tempTempDir;
        }
        this.serverProperties.put(PLUGIN_TEMPDIR_PROPERTY_KEY, this.tempDir);
        LOG.info("As temporary directory " + this.tempDir + " will be used");
    }

    public Properties getserverProperties() {
        Assert.notNull(this.serverProperties);
        return this.serverProperties;
    }

    public String getStringProperty(String propertyName) {
        Assert.notNull(this.serverProperties);
        return this.serverProperties.getProperty(propertyName);
    }

    public int getIntProperty(String propertyName) {
        Assert.notNull(this.serverProperties);
        String property = this.serverProperties.getProperty(propertyName);
        return Integer.parseInt(property);
    }

    public int getIntProperty(String propertyName, int defaultValue) {
        Assert.notNull(this.serverProperties);
        String property = this.serverProperties.getProperty(propertyName);
        if ( property == null ) {
            return defaultValue;
        } else {
            return Integer.parseInt(property);
        }
    }

    public boolean getBooleanProperty(String propertyName) {
        Assert.notNull(this.serverProperties);
        String property = this.serverProperties.getProperty(propertyName);
        return Boolean.parseBoolean(property);
    }

    public List<String> getRobotWhitelist() {
        return this.robotsOnWhiteList;
    }

    public String getDefaultRobot() {
        return this.defaultRobot;
    }

    public String getResourceDir() {
        return this.resourceDir;
    }

    public String getTempDir() {
        return this.tempDir;
    }

    public String getTempDirForUserProjects() {
        return this.tempDir + "userProjects/";
    }
}