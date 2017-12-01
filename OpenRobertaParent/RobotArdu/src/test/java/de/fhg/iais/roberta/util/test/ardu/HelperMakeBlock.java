package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.factory.arduino.mbot.Factory;
import de.fhg.iais.roberta.mode.action.MoveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.actors.arduino.mbot.ActorPort;

public class HelperMakeBlock extends de.fhg.iais.roberta.util.test.Helper {

    public HelperMakeBlock() {
        super();
        Factory robotFactory = new Factory();
        Configuration brickConfiguration =
            new MbotConfiguration.Builder()
                .addActor(ActorPort.M2, new Actor(ActorType.GEARED_MOTOR, true, MoveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.M1, new Actor(ActorType.GEARED_MOTOR, false, MoveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        setRobotFactory(robotFactory);
        setRobotConfiguration(brickConfiguration);
    }
}
