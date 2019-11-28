package de.fhg.iais.roberta.util;

import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class PluginProperties {

    private final String robotName;
    private final String resourceDir;
    private final String tempDir;
    private final String compilerDir;
    private final String updateDir;
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
        this.resourceDir = resourceDirPath(resourceDir, properties.getProperty("robot.plugin.compiler.resources.dir"));
        this.updateDir = resourceDirPath(resourceDir, properties.getProperty("robot.plugin.update.dir"));
        this.tempDir = tempDir;
        this.compilerDir = (String) properties.getOrDefault("robot.plugin.compiler." + getOs() + ".dir", "");
        this.pluginProperties = properties;
    }

    private String resourceDirPath(String baseDir, String pluginExtensionDir) {
        String tempResourceDir;
        if ( pluginExtensionDir != null && pluginExtensionDir.length() > 0 && pluginExtensionDir.startsWith("/") ) {
            tempResourceDir = pluginExtensionDir; // because plugin resource dir is an absolute path
        } else {
            tempResourceDir = baseDir + pluginExtensionDir; // because plugin resource dir is a relative path
        }
        if ( !(tempResourceDir.endsWith("/") || tempResourceDir.endsWith("\\")) ) {
            tempResourceDir = tempResourceDir + "/";
        }
        return tempResourceDir;
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

    public String getUpdateDir() {
        return this.updateDir;
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

    public void setStringProperty(String propertyName, String value) {
        this.pluginProperties.setProperty(propertyName, value);
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
        if ( SystemUtils.IS_OS_WINDOWS ) {
            return "windows";
        } else if(SystemUtils.IS_OS_LINUX) {
            return "linux";
        } else if(SystemUtils.IS_OS_MAC_OSX) {
            return "osx";
        } else {
            throw new DbcException("OS not supported/implemented!");
        }
    }
}