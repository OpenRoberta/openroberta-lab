package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
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
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IMbotVisitor<V> extends ISensorVisitor<V>, ISerialVisitor<V>, IDifferentialMotorVisitor<V>, ILightVisitor<V>, ISimpleSoundVisitor<V> {

    default V visitJoystick(Joystick<V> joystick) {
        throw new DbcException("Block not supported");
    }

    default V visitFlameSensor(FlameSensor<V> flameSensor) {
        throw new DbcException("Block not supported");
    }

    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);

    V visitLEDMatrixImageAction(LEDMatrixImageAction<V> ledMatrixImageAction);

    V visitLEDMatrixTextAction(LEDMatrixTextAction<V> ledMatrixTextAction);

    V visitLEDMatrixImage(LEDMatrixImage<V> ledMatrixImage);

    V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<V> ledMatrixImageShiftFunction);

    V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<V> ledMatrixImageInverFunction);

    V visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<V> ledMatrixSetBrightnessAction);

    V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

}
