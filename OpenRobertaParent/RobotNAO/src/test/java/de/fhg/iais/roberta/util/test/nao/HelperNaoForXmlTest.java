package de.fhg.iais.roberta.util.test.nao;

import java.util.Properties;

import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.nao.Factory;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperNaoForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperNaoForXmlTest() {
        super(new Factory(new RobertaProperties(Util1.loadProperties(null))), new NAOConfiguration.Builder().build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot.properties", robotProperties);
    }
}
