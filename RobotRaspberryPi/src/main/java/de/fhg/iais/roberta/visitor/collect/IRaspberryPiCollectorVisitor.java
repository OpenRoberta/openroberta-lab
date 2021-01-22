package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.actors.raspberrypi.*;
import de.fhg.iais.roberta.syntax.generic.raspberrypi.WriteGpioValueAction;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SmoothedSensor;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

/**
 * Collector for the Raspberry Pi. Adds the blocks missing from the defaults of {@link ICollectorVisitor}. Defines the specific parent implementation to use
 * (the one of the collector) due to unrelated defaults.
 */
public interface IRaspberryPiCollectorVisitor extends ICollectorVisitor, IRaspberryPiVisitor<Void> {

    @Override
    default Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        ledBlinkAction.getNumBlinks().accept(this);
        ledBlinkAction.getOnTime().accept(this);
        ledBlinkAction.getOffTime().accept(this);
        return null;
    }

    @Override
    default Void visitLedBlinkFrequencyAction(LedBlinkFrequencyAction<Void> ledBlinkFrequencyAction) {
        ledBlinkFrequencyAction.getFrequency().accept(this);
        ledBlinkFrequencyAction.getNumBlinks().accept(this);
        return null;
    }

    @Override
    default Void visitRgbLedBlinkAction(RgbLedBlinkAction<Void> rgbLedBlinkAction) {
        rgbLedBlinkAction.getNumBlinks().accept(this);
        rgbLedBlinkAction.getOnTime().accept(this);
        rgbLedBlinkAction.getOffTime().accept(this);
        rgbLedBlinkAction.getOnColor().accept(this);
        rgbLedBlinkAction.getOffColor().accept(this);

        return null;
    }

    @Override
    default Void visitRgbLedBlinkFrequencyAction(RgbLedBlinkFrequencyAction<Void> rgbLedBlinkFrequencyAction) {
        rgbLedBlinkFrequencyAction.getFrequency().accept(this);
        rgbLedBlinkFrequencyAction.getOnColor().accept(this);
        rgbLedBlinkFrequencyAction.getOffColor().accept(this);
        rgbLedBlinkFrequencyAction.getNumBlinks().accept(this);

        return null;
    }

    @Override
    default Void visitRgbLedPulseAction(RgbLedPulseAction<Void> rgbLedPulseAction) {
        rgbLedPulseAction.getFadeInTime().accept(this);
        rgbLedPulseAction.getFadeOutTime().accept(this);
        rgbLedPulseAction.getOnColor().accept(this);
        rgbLedPulseAction.getOffColor().accept(this);
        rgbLedPulseAction.getNumBlinks().accept(this);
        return null;
    }

    @Override
    default Void visitBuzzerBeepAction(BuzzerBeepAction<Void> buzzerBeepAction) {
        buzzerBeepAction.getOnTime().accept(this);
        buzzerBeepAction.getOffTime().accept(this);
        buzzerBeepAction.getNumBeeps().accept(this);
        return null;
    }

    @Override
    default Void visitBuzzerAction(BuzzerAction<Void> buzzerAction) {
        return null;
    }

    @Override
    default Void visitMotorRaspOnAction(MotorRaspOnAction<Void> motorRaspOnAction) {
        if ( motorRaspOnAction.getDurationValue() != null ) {
            motorRaspOnAction.getDurationValue().accept(this);
        }
        motorRaspOnAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitServoRaspOnAction(ServoRaspOnAction<Void> servoRaspOnAction) {
        return null;
    }

    @Override
    default Void visitWriteGpioValueAction(WriteGpioValueAction<Void> voidWriteGpioValueAction) {
        return null;
    }

    @Override
    default Void visitSmoothedSensor(SmoothedSensor<Void> smoothedSensor) {
        return null;
    }

    default Void visitLedPulseAction(LedPulseAction<Void> ledPulseAction) {
        return null;
    }

    // following methods are used to specify unrelated defaults
}
