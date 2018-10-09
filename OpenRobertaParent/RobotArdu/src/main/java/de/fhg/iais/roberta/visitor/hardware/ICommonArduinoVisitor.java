package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.visitor.hardware.IActorVisitor;
import de.fhg.iais.roberta.visitor.hardware.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface ICommonArduinoVisitor<V> extends IActorVisitor<V>, ISensorVisitor<V> {

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    V visitLedOnAction(LedOnAction<V> ledOnAction);

    V visitLedOffAction(LedOffAction<V> ledOffAction);

    V visitExternalLedOnAction(ExternalLedOnAction<V> externalLedOnAction);

    V visitExternalLedOffAction(ExternalLedOffAction<V> externalLedOffAction);

    V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueSensor);

    V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction);

}
