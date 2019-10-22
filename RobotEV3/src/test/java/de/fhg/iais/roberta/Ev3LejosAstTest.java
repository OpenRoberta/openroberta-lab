package de.fhg.iais.roberta;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.Ev3LejosV1Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class Ev3LejosAstTest extends Ev3AstTest {

    @BeforeClass
    public static void setup() {
        testFactory = new Ev3LejosV1Factory(new PluginProperties("ev3lejosv1", "", "", Util1.loadProperties("classpath:/ev3lejosv1.properties")));
    }
}
