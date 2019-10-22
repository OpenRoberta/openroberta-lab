package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class ArduinoAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new UnoFactory(new PluginProperties("uno", "", "", Util1.loadProperties("classpath:/uno.properties")));
    }
}
