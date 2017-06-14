package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.BotNrollConfiguration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.BotNrollFactory;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.botnroll.ActorPort;

public class HelperBotNroll extends de.fhg.iais.roberta.util.test.Helper {

    public HelperBotNroll() {
        super();
        BotNrollFactory robotFactory = new BotNrollFactory(null);
        Configuration brickConfiguration =
            new BotNrollConfiguration.Builder()
                .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.C, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .build();
        setRobotFactory(robotFactory);
        setRobotConfiguration(brickConfiguration);
    }
}
