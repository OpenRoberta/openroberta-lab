package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class NaoAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("nao", "", "", Util.loadProperties("classpath:/nao.properties")));
    }

    protected static ConfigurationAst makeStandard() {
        return new ConfigurationAst.Builder().build();
    }
}
