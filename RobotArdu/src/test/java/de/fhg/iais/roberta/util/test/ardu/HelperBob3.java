package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.factory.arduino.bob3.Factory;
import de.fhg.iais.roberta.util.test.Helper;

public class HelperBob3 extends Helper {

    public HelperBob3() {
        super();
        Factory robotFactory = new Factory(null);
        Configuration brickConfiguration = new Bob3Configuration.Builder().build();
        setRobotFactory(robotFactory);
        setRobotConfiguration(brickConfiguration);
    }
}
