package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actor.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actor.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;


public interface IFestobionicVisitor<V> extends IMotorVisitor<V>, ILightVisitor<V>, ISensorVisitor<V> {
    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorStopAction(MotorStopAction motorStopAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitKeysSensor(KeysSensor keysSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitColorSensor(ColorSensor colorSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitLightSensor(LightSensor lightSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitSoundSensor(SoundSensor soundSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitEncoderSensor(EncoderSensor encoderSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitEncoderReset(EncoderReset encoderReset) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGyroSensor(GyroSensor gyroSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitInfraredSensor(InfraredSensor infraredSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitTouchSensor(TouchSensor touchSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitCompassSensor(CompassSensor compassSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVoltageSensor(VoltageSensor voltageSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGestureSensor(GestureSensor gestureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMoistureSensor(MoistureSensor moistureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitHumiditySensor(HumiditySensor humiditySensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotionSensor(MotionSensor motionSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitDropSensor(DropSensor dropSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPulseSensor(PulseSensor pulseSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitRfidSensor(RfidSensor rfidSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        throw new DbcException("Not supported!");
    }

    default V visitLedOffAction(LedOffAction ledOffAction) {
        throw new DbcException("Not supported!");
    }

    default V visitLedOnAction(LedOnAction ledOnAction) {
        throw new DbcException("Not supported!");
    }

    default V visitStepMotorAction(StepMotorAction stepMotorAction) {
        throw new DbcException("Not supported!");
    }
}
