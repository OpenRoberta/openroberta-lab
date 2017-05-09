package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.MakeBlockConfiguration;
import de.fhg.iais.roberta.factory.MakeBlockFactory;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.makeblock.ActorPort;

public class HelperMakeBlock extends de.fhg.iais.roberta.util.test.Helper {

    public HelperMakeBlock() {
        super();
        MakeBlockFactory robotFactory = new MakeBlockFactory(null);
        Configuration brickConfiguration =
            new MakeBlockConfiguration.Builder()
                .addActor(ActorPort.M2, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.M1, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        setRobotFactory(robotFactory);
        setRobotConfiguration(brickConfiguration);
    }
}
