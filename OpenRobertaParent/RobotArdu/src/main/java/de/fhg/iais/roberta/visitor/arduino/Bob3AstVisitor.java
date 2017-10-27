package de.fhg.iais.roberta.visitor.arduino;

import de.fhg.iais.roberta.syntax.action.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.expr.arduino.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.Bob3TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.Bob3TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.GetSampleSensor;

public interface Bob3AstVisitor<V> extends ArduinoAstVisitor<V> {

    V visitBob3TemperatureSensor(Bob3TemperatureSensor<V> temperatureSensor);

    V visitBob3CodePadSensor(CodePadSensor<V> codePadSensor);

    @Override
    V visitRgbColor(RgbColor<V> rgbColor);

    V visitTouchSensor(Bob3TouchSensor<V> touchSensor);

    V visitLightSensor(AmbientLightSensor<V> lightSensor);

    V visitBodyLEDAction(BodyLEDAction<V> bodyLEDAction);

    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);

    V visitBob3GetSampleSensor(GetSampleSensor<V> bob3GetSampleSensor);

    V visitRememberAction(RememberAction<V> rememberAction);

    V visitRecallAction(RecallAction<V> recallAction);

}
