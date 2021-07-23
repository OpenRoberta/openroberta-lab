package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.visitor.hardware.INIBOVisitor;

/**
 * Collector for the NIBO robots (BOB3, ROB3RTA). Adds the blocks missing from the defaults of {@link ICollectorVisitor}. Defines the specific parent implementation to use (the one of
 * the collector) due to unrelated defaults.
 */
public interface INIBOCollectorVisitor extends ICollectorVisitor, INIBOVisitor<Void> {
    @Override
    default Void visitCodePadSensor(CodePadSensor<Void> codePadSensor) {
        return null;
    }

    @Override
    default Void visitBodyLEDAction(BodyLEDAction<Void> bodyLEDAction) {
        return null;
    }

    @Override
    default Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        sendIRAction.getCode().accept(this);
        return null;
    }

    @Override
    default Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        return null;
    }

    @Override
    default Void visitRememberAction(RememberAction<Void> rememberAction) {
        rememberAction.getCode().accept(this);
        return null;
    }

    @Override
    default Void visitRecallAction(RecallAction<Void> recallAction) {
        return null;
    }

    @Override
    default Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    default Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ledOnAction.getLedColor().accept(this);
        return null;
    }
}
