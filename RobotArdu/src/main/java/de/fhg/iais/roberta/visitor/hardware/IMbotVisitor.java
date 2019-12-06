package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IMbotVisitor<V> extends IActors4AutonomousDriveRobots<V>, ISensorVisitor<V>, ISerialVisitor<V> {

    V visitJoystick(Joystick<V> joystick);

    V visitFlameSensor(FlameSensor<V> flameSensor);

    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);
    
    V visitLEDMatrixImageAction(LEDMatrixImageAction<V> ledMatrixImageAction);
    
    V visitLEDMatrixTextAction(LEDMatrixTextAction<V> ledMatrixTextAction);
     
    V visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage);

    V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction);

    V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction);

    V visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<V> ledMatrixSetBrightnessAction);
}
