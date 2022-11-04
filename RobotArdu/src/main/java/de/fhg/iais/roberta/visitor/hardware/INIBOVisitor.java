package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actor.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface INIBOVisitor<V> extends ILightVisitor<V>, ISensorVisitor<V> {

    default V visitCodePadSensor(CodePadSensor codePadSensor) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitBodyLEDAction(BodyLEDAction bodyLEDAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitSendIRAction(SendIRAction sendIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitRememberAction(RememberAction rememberAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitRecallAction(RecallAction recallAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLedOffAction(LedOffAction ledOffAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLedOnAction(LedOnAction ledOnAction) {
        throw new DbcException("Block is not implemented!");
    }

}
