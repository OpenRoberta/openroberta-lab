package de.fhg.iais.roberta;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.Ev3C4ev3Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class Ev3C4ev3AstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new Ev3C4ev3Factory(new PluginProperties("ev3c4ev3", "", "", Util1.loadProperties("classpath:/ev3c4ev3.properties")));
    }
}
