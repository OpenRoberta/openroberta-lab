package de.fhg.iais.roberta.visitor;


import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class Mbot2ValidatorAndCollectorVisitorTest {
    private static RobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        Properties properties = Util.loadPropertiesRecursively("classpath:/mbot2.properties");
        testFactory = new RobotFactory(new PluginProperties("mbot2", "", "", properties));
    }

    public static final String DEFAULT_XML = "/mbot2/configuration.default.xml";

    @Test
    public void driveActionTest() {
        // List<List<Phrase>> phrases = UnitTestHelper.getProgramAst(testFactory, DEFAULT_XML);
    }
}