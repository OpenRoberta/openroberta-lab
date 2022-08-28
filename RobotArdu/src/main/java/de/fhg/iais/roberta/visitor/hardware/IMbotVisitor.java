package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.configuration.Joystick;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IMbotVisitor<V> extends ISensorVisitor<V>, IDifferentialMotorVisitor<V>, ILightVisitor<V>, ISimpleSoundVisitor<V> {

    default V visitJoystick(Joystick joystick) {
        throw new DbcException("Block not supported");
    }

    default V visitFlameSensor(FlameSensor flameSensor) {
        throw new DbcException("Block not supported");
    }

    V visitSendIRAction(SendIRAction sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction receiveIRAction);

    V visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction);

    V visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction);

    V visitLEDMatrixImage(LEDMatrixImage ledMatrixImage);

    V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction);

    V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction);

    V visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

}
