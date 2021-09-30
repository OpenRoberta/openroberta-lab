package de.fhg.iais.roberta.visitor;


import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.Mbot2ValidatorAndCollectorWorker;

public class Mbot2ValidatorAndCollectorVisitorTest {
    private static RobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("mbot2", "", "", Util.loadProperties("classpath:/mbot2.properties")));
    }

    public static final String DEFAULT_XML = "/mbot2/configuration.default.xml";

    @Test
    public void driveActionTest(){
       // List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, DEFAULT_XML);
    }
}