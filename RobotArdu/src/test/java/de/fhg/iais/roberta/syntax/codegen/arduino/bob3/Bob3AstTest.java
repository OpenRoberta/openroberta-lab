package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.Bob3Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class Bob3AstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new Bob3Factory(new PluginProperties("bob3", "", "", Util1.loadProperties("classpath:/bob3.properties")));
    }
}
