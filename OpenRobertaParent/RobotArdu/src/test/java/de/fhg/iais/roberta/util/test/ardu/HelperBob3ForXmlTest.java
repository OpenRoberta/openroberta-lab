package de.fhg.iais.roberta.util.test.ardu;

import java.util.Properties;

import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.Bob3Factory;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperBob3ForXmlTest extends AbstractHelperForXmlTest {

    public HelperBob3ForXmlTest() {
        super(new Bob3Factory("bob3", Util1.loadProperties("classpath:bob3.properties"), ""), new Bob3Configuration.Builder().build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot", robotProperties);
    }
}
