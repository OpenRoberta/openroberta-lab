package de.fhg.iais.roberta.visitor.hardware;

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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IEdisonVisitor<V> extends ILightVisitor<V>, //for the red LEDs
    IDifferentialMotorVisitor<V>, //for the differential motor
    ISoundVisitor<V>, //for the sound sensor and beeper
    ISensorVisitor<V> //all other sensors (not all used)
{

    default V visitSendIRAction(SendIRAction<V> sendIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitSensorResetAction(ResetSensor<V> vResetSensor) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
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
    default V visitTimerSensor(TimerSensor<V> timerSensor) {
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
    default V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGestureSensor(GestureSensor<V> sensorGetSample) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor) {
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
