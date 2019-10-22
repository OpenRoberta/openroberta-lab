package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.MicrobitFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class MicrobitAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new MicrobitFactory(new PluginProperties("microbit", "", "", Util1.loadProperties("classpath:/microbit.properties")));
    }
}
