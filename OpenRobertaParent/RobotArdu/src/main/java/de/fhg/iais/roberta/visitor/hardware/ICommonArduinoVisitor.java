package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface ICommonArduinoVisitor<V> extends IActorVisitor<V>, ISensorVisitor<V> {

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */

    V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueSensor);

    V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction);

    V visitLedOffAction(LedOffAction<V> ledOffAction);

    V visitLedOnAction(LedOnAction<V> ledOnAction);

}
