package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
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
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IFestobionicVisitor<V> extends IMotorVisitor<V>, ISerialVisitor<V>, ILightVisitor<V>, ISensorVisitor<V> {
    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitKeysSensor(KeysSensor<V> keysSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitColorSensor(ColorSensor<V> colorSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitLightSensor(LightSensor<V> lightSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitSoundSensor(SoundSensor<V> soundSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGyroSensor(GyroSensor<V> gyroSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitTouchSensor(TouchSensor<V> touchSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitCompassSensor(CompassSensor<V> compassSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVoltageSensor(VoltageSensor<V> voltageSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPinTouchSensor(PinTouchSensor<V> pinTouchSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGestureSensor(GestureSensor<V> gestureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMoistureSensor(MoistureSensor<V> moistureSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitHumiditySensor(HumiditySensor<V> humiditySensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotionSensor(MotionSensor<V> motionSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitDropSensor(DropSensor<V> dropSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPulseSensor(PulseSensor<V> pulseSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitRfidSensor(RfidSensor<V> rfidSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVemlLightSensor(VemlLightSensor<V> vemlLightSensor) {
        throw new DbcException("Not supported!");
    }
}
