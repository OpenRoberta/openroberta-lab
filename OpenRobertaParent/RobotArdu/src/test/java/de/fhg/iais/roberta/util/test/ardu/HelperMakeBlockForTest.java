package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.factory.arduino.mbot.Factory;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.actors.arduino.mbot.ActorPort;
import de.fhg.iais.roberta.util.RobertaProperties;

public class HelperMakeBlockForTest extends de.fhg.iais.roberta.util.test.AbstractHelperForTest {

    public HelperMakeBlockForTest(RobertaProperties robertaProperties) {
        super(new Factory(robertaProperties));
        Configuration brickConfiguration =
            new MbotConfiguration.Builder()
                .addActor(ActorPort.M2, new Actor(ActorType.GEARED_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.M1, new Actor(ActorType.GEARED_MOTOR, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        setRobotConfiguration(brickConfiguration);
    }
}
