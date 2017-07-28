package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.bob3.Bob3BodyLEDAction;
import de.fhg.iais.roberta.syntax.action.bob3.Bob3ReceiveIRAction;
import de.fhg.iais.roberta.syntax.action.bob3.Bob3SendIRAction;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3CodePadSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.Bob3TouchSensor;

public interface Bob3AstVisitor<V> extends ArduAstVisitor<V> {

    V visitBob3TemperatureSensor(Bob3TemperatureSensor<V> temperatureSensor);

    V visitBob3CodePadSensor(Bob3CodePadSensor<V> codePadSensor);

    V visitRgbColor(RgbColor<V> rgbColor);

    V visitTouchSensor(Bob3TouchSensor<V> touchSensor);

    V visitLightSensor(Bob3AmbientLightSensor<V> lightSensor);

    V visitBodyLEDAction(Bob3BodyLEDAction<V> bodyLEDAction);

    V visitSendIRAction(Bob3SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(Bob3ReceiveIRAction<V> receiveIRAction);

}
