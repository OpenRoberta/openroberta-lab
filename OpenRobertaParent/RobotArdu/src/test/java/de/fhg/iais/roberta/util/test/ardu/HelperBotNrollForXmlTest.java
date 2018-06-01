package de.fhg.iais.roberta.util.test.ardu;

import java.util.Properties;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.arduino.BotNrollConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.arduino.botnroll.Factory;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperBotNrollForXmlTest extends AbstractHelperForXmlTest {

    public HelperBotNrollForXmlTest() {
        super(
            new Factory(new RobertaProperties(Util1.loadProperties(null))),
            new BotNrollConfiguration.Builder()
                .addActor(new ActorPort("A", "MA"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(new ActorPort("B", "MB"), new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(new ActorPort("C", "MC"), new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(new ActorPort("D", "MD"), new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot.properties", robotProperties);
    }
}
