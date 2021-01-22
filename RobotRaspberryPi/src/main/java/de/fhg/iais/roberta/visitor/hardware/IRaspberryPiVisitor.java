package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.raspberrypi.*;
import de.fhg.iais.roberta.syntax.generic.raspberrypi.WriteGpioValueAction;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SmoothedSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IRaspberryPiVisitor<V> extends ISensorVisitor<V>, ILightVisitor<V>, ISoundVisitor<V>, IMotorVisitor<V> {

    V visitLedBlinkAction(LedBlinkAction<V> ledBlinkAction);

    V visitSmoothedSensor(SmoothedSensor<V> smoothedSensor);

    V visitLedBlinkFrequencyAction(LedBlinkFrequencyAction<V> ledBlinkFrequencyAction);

    V visitLedPulseAction(LedPulseAction<V> ledPulseAction);

    V visitRgbLedBlinkAction(RgbLedBlinkAction<V> rgbLedBlinkAction);

    V visitRgbLedBlinkFrequencyAction(RgbLedBlinkFrequencyAction<V> rgbLedBlinkFrequencyAction);

    V visitRgbLedPulseAction(RgbLedPulseAction<V> rgbLedPulseAction);

    V visitBuzzerBeepAction(BuzzerBeepAction<V> buzzerBeepAction);

    V visitBuzzerAction(BuzzerAction<V> buzzerAction);

    V visitMotorRaspOnAction(MotorRaspOnAction<V> motorRaspOnAction);

    V visitServoRaspOnAction(ServoRaspOnAction<V> servoRaspOnAction);

    V visitWriteGpioValueAction(WriteGpioValueAction<V> vWriteGpioValueAction);
}