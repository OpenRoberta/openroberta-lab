package de.fhg.iais.roberta.visitor.hardware.sensor;

import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISensorVisitor<V> extends IHardwareVisitor<V> {
    /**
     * visit a {@link KeysSensor}.
     *
     * @param keysSensor to be visited
     */
    default V visitKeysSensor(KeysSensor keysSensor) {
        throw new DbcException("KeysSensor not implemented!");
    }

    /**
     * visit a {@link ColorSensor}.
     *
     * @param colorSensor to be visited
     */
    default V visitColorSensor(ColorSensor colorSensor) {
        throw new DbcException("ColorSensor not implemented!");
    }

    /**
     * visit a {@link LightSensor}.
     *
     * @param lightSensor to be visited
     */
    default V visitLightSensor(LightSensor lightSensor) {
        throw new DbcException("LightSensor not implemented!");
    }

    /**
     * visit a {@link SoundSensor}.
     *
     * @param soundSensor to be visited
     */
    default V visitSoundSensor(SoundSensor soundSensor) {
        throw new DbcException("SoundSensor not implemented!");
    }

    /**
     * visit a {@link EncoderSensor}.
     *
     * @param encoderSensor to be visited
     */
    default V visitEncoderSensor(EncoderSensor encoderSensor) {
        throw new DbcException("EncoderSensor not implemented!");
    }

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor to be visited
     */
    default V visitGyroSensor(GyroSensor gyroSensor) {
        throw new DbcException("GyroSensor not implemented!");
    }

    /**
     * visit a {@link InfraredSensor}.
     *
     * @param infraredSensor to be visited
     */
    default V visitInfraredSensor(InfraredSensor infraredSensor) {
        throw new DbcException("InfraredSensor not implemented!");
    }

    /**
     * visit a {@link TimerSensor}.
     *
     * @param timerSensor to be visited
     */
    default V visitTimerSensor(TimerSensor timerSensor) {
        throw new DbcException("TimerSensor not implemented!");
    }

    /**
     * visit a {@link TouchSensor}.
     *
     * @param touchSensor to be visited
     */
    default V visitTouchSensor(TouchSensor touchSensor) {
        throw new DbcException("TouchSensor not implemented!");
    }

    /**
     * visit a {@link UltrasonicSensor}.
     *
     * @param ultrasonicSensor to be visited
     */
    default V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        throw new DbcException("UltrasonicSensor not implemented!");
    }

    /**
     * visit a {@link CompassSensor}.
     *
     * @param compassSensor to be visited
     */
    default V visitCompassSensor(CompassSensor compassSensor) {
        throw new DbcException("CompassSensor not implemented!");
    }

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor to be visited
     */
    default V visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        throw new DbcException("TemperatureSensor not implemented!");
    }

    /**
     * visit a {@link VoltageSensor}.
     *
     * @param voltageSensor to be visited
     */
    default V visitVoltageSensor(VoltageSensor voltageSensor) {
        throw new DbcException("VoltageSensor not implemented!");
    }

    /**
     * visit a {@link AccelerometerSensor}.
     *
     * @param accelerometerSensor to be visited
     */
    default V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        throw new DbcException("AccelerometerSensor not implemented!");
    }

    /**
     * visit a {@link PinTouchSensor}.
     *
     * @param pinTouchSensor to be visited
     */
    default V visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        throw new DbcException("PinTouchSensor not implemented!");
    }

    /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor to be visited
     */
    default V visitGestureSensor(GestureSensor gestureSensor) {
        throw new DbcException("GestureSensor not implemented!");
    }

    /**
     * visit a {@link PinGetValueSensor}.
     *
     * @param pinGetValueSensor to be visited
     */
    default V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        throw new DbcException("PinGetValueSensor not implemented!");
    }

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        sensorGetSample.sensor.accept(this);
        return null;
    }

    /**
     * visit a {@link IRSeekerSensor}.
     *
     * @param irSeekerSensor to be visited
     */
    default V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        throw new DbcException("IRSeekerSensor not implemented!");
    }

    /**
     * visit a {@link MoistureSensor}.
     *
     * @param moistureSensor to be visited
     */
    default V visitMoistureSensor(MoistureSensor moistureSensor) {
        throw new DbcException("MoistureSensor not implemented!");
    }

    /**
     * visit a {@link HumiditySensor}.
     *
     * @param humiditySensor to be visited
     */
    default V visitHumiditySensor(HumiditySensor humiditySensor) {
        throw new DbcException("HumiditySensor not implemented!");
    }

    /**
     * visit a {@link MotionSensor}.
     *
     * @param motionSensor to be visited
     */

    default V visitMotionSensor(MotionSensor motionSensor) {
        throw new DbcException("MotionSensor not implemented!");
    }

    /**
     * visit a {@link DropSensor}.
     *
     * @param dropSensor to be visited
     */

    default V visitDropSensor(DropSensor dropSensor) {
        throw new DbcException("DropSensor not implemented!");
    }

    /**
     * visit a {@link PulseSensor}.
     *
     * @param pulseSensor to be visited
     */

    default V visitPulseSensor(PulseSensor pulseSensor) {
        throw new DbcException("PulseSensor not implemented!");
    }

    /**
     * visit a {@link RfidSensor}.
     *
     * @param rfidSensor to be visited
     */

    default V visitRfidSensor(RfidSensor rfidSensor) {
        throw new DbcException("RfidSensor not implemented!");
    }

    /**
     * visit a {@link VemlLightSensor}.
     *
     * @param vemlLightSensor to be visited
     */

    default V visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        throw new DbcException("VEML light sensor not implemented!");
    }

    /**
     * visit a {@link ParticleSensor}.
     *
     * @param particleSensor to be visited
     */

    default V visitParticleSensor(ParticleSensor particleSensor) {
        throw new DbcException("Particle sensor not implemented!");
    }

    /**
     * visit a {@link HTColorSensor}.
     *
     * @param htColorSensor to be visited
     */
    default V visitHTColorSensor(HTColorSensor htColorSensor) {
        throw new DbcException("HTColorSensor not implemented!");
    }
}
