package de.fhg.iais.roberta;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class Ev3DevAstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new EV3Factory(new PluginProperties("ev3dev", "", "", Util1.loadProperties("classpath:/ev3dev.properties")));
    }
}
