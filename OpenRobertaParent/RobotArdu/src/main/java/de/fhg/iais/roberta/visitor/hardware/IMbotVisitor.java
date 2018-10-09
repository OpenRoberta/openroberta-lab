package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.mbot.DisplayImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.DisplayTextAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.PIRMotionSensor;

public interface IMbotVisitor<V> extends ICommonArduinoVisitor<V> {

    V visitAmbientLightSensor(AmbientLightSensor<V> lightSensor);

    V visitPIRMotionSensor(PIRMotionSensor<V> motionSensor);

    V visitJoystick(Joystick<V> joystick);

    V visitFlameSensor(FlameSensor<V> flameSensor);

    V visitImage(LedMatrix<V> ledMatrix);

    V visitDisplayImageAction(DisplayImageAction<V> displayImageAction);

    V visitDisplayTextAction(DisplayTextAction<V> displayTextAction);

    V visitMbotGetSampleSensor(GetSampleSensor<V> getSampleSensor);

}
