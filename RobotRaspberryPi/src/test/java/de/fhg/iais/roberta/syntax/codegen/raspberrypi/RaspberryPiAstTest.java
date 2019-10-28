package de.fhg.iais.roberta.syntax.codegen.raspberrypi;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.RaspberryPiFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class RaspberryPiAstTest extends AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new RaspberryPiFactory(new PluginProperties("raspberrypi", "", "", Util.loadProperties("classpath:/raspberrypi.properties")));
    }
}
