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
    private static Properties robertaProperties = null;
    private static String defaultRobot = null;
    private static List<String> robotsOnWhiteList = null;

    private RobertaProperties() {
        // no objects
    }

    /**
     * store the final set of properties, that control the OpenRoberta system.<br>
     * <br>
     * After the ServerStarter has loaded the properties, merged with optional runtime properties, it calls this method with the (final) set of properties. They
     * are stored in this class. From here they can be retrieved and used elsewhere.<br>
     * <b>But note:</b> Getting properties from <b><i>Guice</i></b> is <b>preferred</b>
     *
     * @param properties
     */
    public static void setRobertaProperties(Properties properties) {
        robertaProperties = properties;
        String whiteList = getStringProperty("robot.whitelist");
        Assert.notNull(whiteList, "Property \"robot.whitelist\" not found");
        String[] whiteListItems = whiteList.split("\\s*,\\s*");
        Assert.isTrue(whiteListItems.length >= 1, "Property \"robot.whitelist\" must contain at least one robot");
        defaultRobot = whiteListItems[0];
        robotsOnWhiteList = Collections.unmodifiableList(Arrays.asList(whiteListItems));
    }

    public static Properties getRobertaProperties() {
        Assert.notNull(robertaProperties);
        return robertaProperties;
    }

    public static String getStringProperty(String propertyName) {
        Assert.notNull(robertaProperties);
        return robertaProperties.getProperty(propertyName);
    }

    public static int getIntProperty(String propertyName) {
        Assert.notNull(robertaProperties);
        String property = robertaProperties.getProperty(propertyName);
        return Integer.parseInt(property);
    }

    public static int getRobotNumberFromProperty(String robotName) {
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

    public static List<String> getRobotWhitelist() {
        return robotsOnWhiteList;
    }

    public static String getDefaultRobot() {
        return defaultRobot;
    }
}