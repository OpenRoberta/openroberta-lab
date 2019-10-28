package de.fhg.iais.roberta;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class Ev3LejosAstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new EV3Factory(new PluginProperties("ev3lejosv1", "", "", Util.loadProperties("classpath:/ev3lejosv1.properties")));
    }
}
