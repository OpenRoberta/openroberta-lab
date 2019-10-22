package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;

/**
 * Collector for the Edison.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface IEdisonCollectorVisitor extends ICollectorVisitor, IEdisonVisitor<Void> {

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
    default Void visitSensorResetAction(ResetSensor<Void> voidResetSensor) {
        return null;
    }

    // following methods are used to specify unrelated defaults

    @Override
    default Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        return ICollectorVisitor.super.visitGestureSensor(gestureSensor);
    }

    @Override
    default Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        return ICollectorVisitor.super.visitAccelerometer(accelerometerSensor);
    }

    @Override
    default Void visitDropSensor(DropSensor<Void> dropSensor) {
        return ICollectorVisitor.super.visitDropSensor(dropSensor);
    }

    @Override
    default Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        return ICollectorVisitor.super.visitPulseSensor(pulseSensor);
    }

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return ICollectorVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        return ICollectorVisitor.super.visitVemlLightSensor(vemlLightSensor);
    }

    @Override
    default Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return ICollectorVisitor.super.visitVoltageSensor(voltageSensor);
    }

    @Override
    default Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        return ICollectorVisitor.super.visitHumiditySensor(humiditySensor);
    }

    @Override
    default Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        return ICollectorVisitor.super.visitMoistureSensor(moistureSensor);
    }

    @Override
    default Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return ICollectorVisitor.super.visitTemperatureSensor(temperatureSensor);
    }

    @Override
    default Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return ICollectorVisitor.super.visitLightStatusAction(lightStatusAction);
    }

    @Override
    default Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return ICollectorVisitor.super.visitPinGetValueSensor(pinGetValueSensor);
    }

    @Override
    default Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return ICollectorVisitor.super.visitTimerSensor(timerSensor);
    }

    @Override
    default Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return ICollectorVisitor.super.visitGyroSensor(gyroSensor);
    }

    @Override
    default Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return ICollectorVisitor.super.visitMotorGetPowerAction(motorGetPowerAction);
    }

    @Override
    default Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return ICollectorVisitor.super.visitMotorSetPowerAction(motorSetPowerAction);
    }

    @Override
    default Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        return ICollectorVisitor.super.visitCompassSensor(compassSensor);
    }

    @Override
    default Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return ICollectorVisitor.super.visitEncoderSensor(encoderSensor);
    }

    @Override
    default Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        return ICollectorVisitor.super.visitMotionSensor(motionSensor);
    }

    @Override
    default Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        return ICollectorVisitor.super.visitPinTouchSensor(pinTouchSensor);
    }

    @Override
    default Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        return ICollectorVisitor.super.visitRfidSensor(rfidSensor);
    }

    @Override
    default Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        return ICollectorVisitor.super.visitUltrasonicSensor(ultrasonicSensor);
    }

    @Override
    default Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return ICollectorVisitor.super.visitTouchSensor(touchSensor);
    }
}
