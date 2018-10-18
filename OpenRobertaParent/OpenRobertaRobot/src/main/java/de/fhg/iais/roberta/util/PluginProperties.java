package de.fhg.iais.roberta.util;

import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;

public class PluginProperties {
    private static final Logger LOG = LoggerFactory.getLogger(PluginProperties.class);

    private final String robotName;
    private final String resourceDir;
    private final String tempDir;
    private final String compilerDir;
    private final Properties pluginProperties;

    /**
     * store the final set of properties, that control a single plugin of the OpenRoberta system.<br>
     * <br>
     * After the ServerStarter has loaded the properties, merged with optional runtime properties, it calls this method with the (final) set of properties. They
     * are stored in this class. From here they can be retrieved and used by exactly one plugin (b.t.w. the plugin's name is a field).<br>
     *
     * @param robotName the name of the robot, unique, lower case
     * @param resourceDir the directory, in which plugin specific resources are stored. Last element is always the plugin's name
     * @param tempDir the directory, into which temporary data can be stored. Usually for cross compiler
     * @param properties all properties from the file "<robotName>.properties". Plugin-specific. Accessed by their plugin-specific names
     */
    public PluginProperties(String robotName, String resourceDir, String tempDir, Properties properties) {
        Assert.notNull(robotName);
        Assert.notNull(resourceDir);
        Assert.notNull(tempDir);
        Assert.notNull(properties);
        this.robotName = robotName;
        String tempResourceDir = resourceDir + properties.getProperty("robot.plugin.compiler.resources.dir");
        if ( !(tempResourceDir.endsWith("/") || tempResourceDir.endsWith("\\")) ) {
            tempResourceDir = tempResourceDir + "/";
        }
        this.resourceDir = tempResourceDir;
        this.tempDir = tempDir;
        String tempCompilerDir = properties.getProperty("robot.plugin.compiler." + getOs() + ".dir");
        this.compilerDir = tempCompilerDir == null ? this.resourceDir : tempCompilerDir;
        this.pluginProperties = properties;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public String getCompilerResourceDir() {
        return this.resourceDir;
    }

    public String getCompilerBinDir() {
        return this.compilerDir;
    }

    public String getTempDir() {
        return this.tempDir;
    }

    public Properties getPluginProperties() {
        Assert.notNull(this.pluginProperties);
        return this.pluginProperties;
    }

    public String getStringProperty(String propertyName) {
        Assert.notNull(this.pluginProperties);
        return this.pluginProperties.getProperty(propertyName);
    }

    public int getIntProperty(String propertyName) {
        Assert.notNull(this.pluginProperties);
        String property = this.pluginProperties.getProperty(propertyName);
        return Integer.parseInt(property);
    }

    public int getIntProperty(String propertyName, int defaultValue) {
        Assert.notNull(this.pluginProperties);
        String property = this.pluginProperties.getProperty(propertyName);
        if ( property == null ) {
            return defaultValue;
        } else {
            return Integer.parseInt(property);
        }
    }

    public boolean getBooleanProperty(String propertyName) {
        Assert.notNull(this.pluginProperties);
        String property = this.pluginProperties.getProperty(propertyName);
        return Boolean.parseBoolean(property);
    }

    private String getOs() {
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        return os;
    }
}