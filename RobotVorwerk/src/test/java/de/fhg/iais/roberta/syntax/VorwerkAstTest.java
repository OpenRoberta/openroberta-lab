package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class VorwerkAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("vorwerk", "", "", Util1.loadProperties("classpath:/vorwerk.properties")));
    }
}
