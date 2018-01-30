package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.factory.arduino.bob3.Factory;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.test.AbstractHelperForTest;

public class HelperBob3ForTest extends AbstractHelperForTest {

    public HelperBob3ForTest(RobertaProperties robertaProperties) {
        super(new Factory(robertaProperties));
        Configuration brickConfiguration = new Bob3Configuration.Builder().build();
        setRobotConfiguration(brickConfiguration);
    }
}
