package de.fhg.iais.roberta;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class Ev3C4ev3AstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new EV3Factory(new PluginProperties("ev3c4ev3", "", "", Util.loadProperties("classpath:/ev3c4ev3.properties")));
    }
}
