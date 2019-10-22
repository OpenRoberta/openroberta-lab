package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.SenseboxFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class SenseboxAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new SenseboxFactory(new PluginProperties("sensebox", "", "", Util1.loadProperties("classpath:/sensebox.properties")));
    }
}
