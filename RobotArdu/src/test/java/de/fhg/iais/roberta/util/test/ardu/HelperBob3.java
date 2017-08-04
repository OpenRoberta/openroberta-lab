package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Bob3Configuration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.Bob3Factory;

public class HelperBob3 extends de.fhg.iais.roberta.util.test.Helper {

    public HelperBob3() {
        super();
        Bob3Factory robotFactory = new Bob3Factory(null);
        Configuration brickConfiguration = new Bob3Configuration.Builder().build();
        setRobotFactory(robotFactory);
        setRobotConfiguration(brickConfiguration);
    }
}
