package de.fhg.iais.roberta.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;

public class ServerProperties {
    private static Logger LOG = LoggerFactory.getLogger(ServerProperties.class);
    public static final String NAME_OF_SIM = "sim";
    public static final String WHITE_LIST_KEY = "robot.whitelist";
    public static final String ROBOT_DEFAULT_PROPERTY_KEY = "robot.default";
    public static final String PLUGIN_TEMPDIR_PROPERTY_KEY = "plugin.tempdir";
    public static final String CROSSCOMPILER_RESOURCE_BASE = "robot.crosscompiler.resourcebase";

    private final Properties serverProperties;
    private final String defaultRobot;
    private final List<String> robotsOnWhiteList;
    private final String crosscompilerResourceDir;
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

        // made a robust choice about the crosscompiler resource directory
        String crosscompilerResourceDir = getStringProperty(CROSSCOMPILER_RESOURCE_BASE);
        if ( crosscompilerResourceDir == null || crosscompilerResourceDir.trim().isEmpty() ) {
            crosscompilerResourceDir = System.getenv(CROSSCOMPILER_RESOURCE_BASE.replace('.', '_'));
            if ( crosscompilerResourceDir == null ) {
                LOG.warn("could not allocate the crosscompiler resource directory, using '.' as default. This will NOT work, if the directory is needed.");
                crosscompilerResourceDir = "./";
            }
        }
        if ( !(crosscompilerResourceDir.endsWith("/") || crosscompilerResourceDir.endsWith("\\")) ) {
            crosscompilerResourceDir = crosscompilerResourceDir + "/";
        }
        this.crosscompilerResourceDir = crosscompilerResourceDir;
        LOG.info("As crosscompiler resource directory " + this.crosscompilerResourceDir + " will be used");

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

    public String getCrosscompilerResourceDir() {
        return this.crosscompilerResourceDir;
    }

    public String getTempDir() {
        return this.tempDir;
    }
}