package de.fhg.iais.roberta.syntax;

import java.util.Properties;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class NaoAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        Properties properties = Util.loadPropertiesRecursively("classpath:/nao.properties");
        testFactory = new RobotFactory(new PluginProperties("nao", "", "", properties));
    }

    protected static ConfigurationAst makeStandard() {
        return new ConfigurationAst.Builder().build();
    }
}
