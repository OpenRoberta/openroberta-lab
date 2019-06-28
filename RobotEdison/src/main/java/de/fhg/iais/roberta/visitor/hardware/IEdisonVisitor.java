package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.*;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IEdisonVisitor<V> extends
    ILightVisitor<V>, //for the red LEDs
    IDifferentialMotorVisitor<V>, //for the differential motor
    ISoundVisitor<V>, //for the sound sensor and beeper
    ISensorVisitor<V> //all other sensors (not all used)
     {

    V visitSendIRAction(SendIRAction<V> sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction);

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    default V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        return null;
    }

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link EncoderSensor}.
     *
     * @param encoderSensor to be visited
     */
    @Override
    default V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor to be visited
     */
    @Override
    default V visitGyroSensor(GyroSensor<V> gyroSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link TimerSensor}.
     *
     * @param timerSensor to be visited
     */
    @Override
    default V visitTimerSensor(TimerSensor<V> timerSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link TouchSensor}.
     *
     * @param touchSensor to be visited
     */
    @Override
    default V visitTouchSensor(TouchSensor<V> touchSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link UltrasonicSensor}.
     *
     * @param ultrasonicSensor to be visited
     */
    @Override
    default V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link CompassSensor}.
     *
     * @param compassSensor to be visited
     */
    @Override
    default V visitCompassSensor(CompassSensor<V> compassSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor to be visited
     */
    @Override
    default V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link VoltageSensor}.
     *
     * @param voltageSensor to be visited
     */
    @Override
    default V visitVoltageSensor(VoltageSensor<V> voltageSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link AccelerometerSensor}.
     *
     * @param accelerometerSensor to be visited
     */
    @Override
    default V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link PinTouchSensor}.
     *
     * @param sensorGetSample
     */
    @Override
    default V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link GestureSensor}.
     *
     * @param sensorGetSample
     */
    @Override
    default V visitGestureSensor(GestureSensor<V> sensorGetSample) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link PinGetValueSensor}.
     *
     * @param pinGetValueSensor
     */
    @Override
    default V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor) {
        throw new DbcException("operation not supported");
    }

   /**
    *
    * visit a {@link GetSampleSensor}.
    *
    * @param sensorGetSample to be visited
    */
    @Override default V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        return null;
    }

    /**
     * visit a {@link MoistureSensor}.
     *
     * @param moistureSensor
     */
    @Override
    default V visitMoistureSensor(MoistureSensor<V> moistureSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link HumiditySensor}.
     *
     * @param humiditySensor
     */
    @Override
    default V visitHumiditySensor(HumiditySensor<V> humiditySensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link MotionSensor}.
     *
     * @param motionSensor
     */
    @Override
    default V visitMotionSensor(MotionSensor<V> motionSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link DropSensor}.
     *
     * @param dropSensor
     */
    @Override
    default V visitDropSensor(DropSensor<V> dropSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link PulseSensor}.
     *
     * @param pulseSensor
     */
    @Override
    default V visitPulseSensor(PulseSensor<V> pulseSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link RfidSensor}.
     *
     * @param rfidSensor
     */
    @Override
    default V visitRfidSensor(RfidSensor<V> rfidSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link VemlLightSensor}.
     *
     * @param vemlLightSensor to be visited
     */
    @Override
    default V visitVemlLightSensor(VemlLightSensor<V> vemlLightSensor) {
        throw new DbcException("operation not supported");
    }

    /**
     * visit a {@link ResetSensor} for the block "edisonSensors_sensor_reset"
     * @param vResetSensor to be visited
     * @return
     */
    V visitSensorResetAction(ResetSensor<V> vResetSensor);

     }
