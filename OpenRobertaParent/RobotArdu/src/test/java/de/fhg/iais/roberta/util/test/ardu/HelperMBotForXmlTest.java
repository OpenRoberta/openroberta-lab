package de.fhg.iais.roberta.util.test.ardu;

import java.util.Properties;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.arduino.mbot.Factory;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class HelperMBotForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperMBotForXmlTest() {
        super(
            new Factory(new RobertaProperties(Util1.loadProperties(null))),
            new MbotConfiguration.Builder()
                .addActor(new ActorPort("motor2", "M2"), new Actor(ActorType.GEARED_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(new ActorPort("motor1", "M1"), new Actor(ActorType.GEARED_MOTOR, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot.properties", robotProperties);
    }
}
