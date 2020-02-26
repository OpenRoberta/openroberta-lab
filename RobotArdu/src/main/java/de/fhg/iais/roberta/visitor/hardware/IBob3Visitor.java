package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IBob3Visitor<V> extends ILightVisitor<V>, ISensorVisitor<V> {

    default V visitBob3CodePadSensor(CodePadSensor<V> codePadSensor) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitBodyLEDAction(BodyLEDAction<V> bodyLEDAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitSendIRAction(SendIRAction<V> sendIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitRememberAction(RememberAction<V> rememberAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitRecallAction(RecallAction<V> recallAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLedOffAction(LedOffAction<V> ledOffAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitLedOnAction(LedOnAction<V> ledOnAction) {
        throw new DbcException("Block is not implemented!");
    }

}
