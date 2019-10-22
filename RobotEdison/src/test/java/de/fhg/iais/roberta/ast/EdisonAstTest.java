package de.fhg.iais.roberta.ast;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.EdisonFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class EdisonAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new EdisonFactory(new PluginProperties("edison", "", "", Util1.loadProperties("classpath:/edison.properties")));
    }
}
