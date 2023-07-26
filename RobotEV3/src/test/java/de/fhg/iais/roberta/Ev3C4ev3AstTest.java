package de.fhg.iais.roberta;

import java.util.Properties;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class Ev3C4ev3AstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        Properties properties = Util.loadPropertiesRecursively("classpath:/ev3c4ev3.properties");
        testFactory = new RobotFactory(new PluginProperties("ev3c4ev3", "", "", properties));
    }
}
