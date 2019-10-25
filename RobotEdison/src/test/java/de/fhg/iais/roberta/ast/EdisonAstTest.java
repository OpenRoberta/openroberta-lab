package de.fhg.iais.roberta.ast;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class EdisonAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("edison", "", "", Util1.loadProperties("classpath:/edison.properties")));
    }
}
