package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class MbotAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new MbotFactory(new PluginProperties("mbot", "", "", Util1.loadProperties("classpath:/mbot.properties")));
    }
}
