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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IMbotVisitor<V> extends IActors4AutonomousDriveRobots<V>, ISensorVisitor<V>, ISerialVisitor<V> {

    default V visitJoystick(Joystick<V> joystick) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitFlameSensor(FlameSensor<V> flameSensor) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitSendIRAction(SendIRAction<V> sendIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixImageAction(LEDMatrixImageAction<V> ledMatrixImageAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixTextAction(LEDMatrixTextAction<V> ledMatrixTextAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixImage(LEDMatrixImage<V> ledMatrixImage) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<V> ledMatrixImageShiftFunction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<V> ledMatrixImageInverFunction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<V> ledMatrixSetBrightnessAction) {
        throw new DbcException("Block is not implemented!");
    }
}
