package de.fhg.iais.roberta;

import java.util.Properties;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;

public class Ev3LejosAstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        Properties properties = Util.loadPropertiesRecursively("classpath:/ev3lejosv1.properties");
        testFactory = new RobotFactory(new PluginProperties("ev3lejosv1", "", "", properties));
    }
}
