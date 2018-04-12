package de.fhg.iais.roberta.visitors.arduino;

import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.GetSampleSensor;

public interface Bob3AstVisitor<V> extends ArduinoAstVisitor<V> {

    V visitBob3CodePadSensor(CodePadSensor<V> codePadSensor);

    V visitBodyLEDAction(BodyLEDAction<V> bodyLEDAction);

    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);

    V visitBob3GetSampleSensor(GetSampleSensor<V> bob3GetSampleSensor);

    V visitRememberAction(RememberAction<V> rememberAction);

    V visitRecallAction(RecallAction<V> recallAction);

}
