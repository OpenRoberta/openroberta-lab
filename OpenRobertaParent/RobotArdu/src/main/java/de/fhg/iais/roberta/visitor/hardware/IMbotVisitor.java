package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousRobots;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IMbotVisitor<V> extends IActors4AutonomousRobots<V>, ISensorVisitor<V> {

    V visitJoystick(Joystick<V> joystick);

    V visitFlameSensor(FlameSensor<V> flameSensor);

    V visitImage(LedMatrix<V> ledMatrix);

    V visitLedOffAction(LedOffAction<V> ledOffAction);

    V visitLedOnAction(LedOnAction<V> ledOnAction);
}
