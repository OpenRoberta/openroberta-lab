package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class Bob3AstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("bob3", "", "", Util.loadProperties("classpath:/bob3.properties")));
    }
}
