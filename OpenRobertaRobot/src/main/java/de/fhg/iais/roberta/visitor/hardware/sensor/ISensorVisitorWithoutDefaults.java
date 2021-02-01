package de.fhg.iais.roberta.visitor.hardware.sensor;

import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISensorVisitorWithoutDefaults<V> extends IHardwareVisitor<V> {
    /**
     * @param keysSensor to be visited
     */
    V visitKeysSensor(KeysSensor<V> keysSensor);

    /**
     * visit a {@link ColorSensor}.
     *
     * @param colorSensor to be visited
     */
    V visitColorSensor(ColorSensor<V> colorSensor);

    /**
     * visit a {@link LightSensor}.
     *
     * @param lightSensor to be visited
     */
    V visitLightSensor(LightSensor<V> lightSensor);

    /**
     * visit a {@link SoundSensor}.
     *
     * @param soundSensor to be visited
     */
    V visitSoundSensor(SoundSensor<V> soundSensor);

    /**
     * visit a {@link EncoderSensor}.
     *
     * @param encoderSensor to be visited
     */
    V visitEncoderSensor(EncoderSensor<V> encoderSensor);

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor to be visited
     */
    V visitGyroSensor(GyroSensor<V> gyroSensor);

    /**
     * visit a {@link InfraredSensor}.
     *
     * @param infraredSensor to be visited
     */
    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    /**
     * visit a {@link TimerSensor}.
     *
     * @param timerSensor to be visited
     */
    V visitTimerSensor(TimerSensor<V> timerSensor);

    /**
     * visit a {@link TouchSensor}.
     *
     * @param touchSensor to be visited
     */
    V visitTouchSensor(TouchSensor<V> touchSensor);

    /**
     * visit a {@link UltrasonicSensor}.
     *
     * @param ultrasonicSensor to be visited
     */
    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    /**
     * visit a {@link CompassSensor}.
     *
     * @param compassSensor to be visited
     */
    V visitCompassSensor(CompassSensor<V> compassSensor);

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    /**
     * visit a {@link VoltageSensor}.
     *
     * @param voltageSensor to be visited
     */
    V visitVoltageSensor(VoltageSensor<V> voltageSensor);

    /**
     * visit a {@link AccelerometerSensor}.
     *
     * @param accelerometerSensor to be visited
     */
    V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor);

    /**
     * visit a {@link PinTouchSensor}.
     *
     * @param pinTouchSensor to be visited
     */
    V visitPinTouchSensor(PinTouchSensor<V> pinTouchSensor);

    /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor to be visited
     */
    V visitGestureSensor(GestureSensor<V> gestureSensor);

    /**
     * visit a {@link PinGetValueSensor}.
     *
     * @param pinGetValueSensor to be visited
     */
    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample);

    /**
     * visit a {@link IRSeekerSensor}.
     *
     * @param irSeekerSensor to be visited
     */
    V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor);

    /**
     * visit a {@link MoistureSensor}.
     *
     * @param moistureSensor to be visited
     */
    V visitMoistureSensor(MoistureSensor<V> moistureSensor);

    /**
     * visit a {@link HumiditySensor}.
     *
     * @param humiditySensor to be visited
     */
    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    /**
     * visit a {@link MotionSensor}.
     *
     * @param motionSensor to be visited
     */

    V visitMotionSensor(MotionSensor<V> motionSensor);

    /**
     * visit a {@link DropSensor}.
     *
     * @param dropSensor to be visited
     */

    V visitDropSensor(DropSensor<V> dropSensor);

    /**
     * visit a {@link PulseSensor}.
     *
     * @param pulseSensor to be visited
     */

    V visitPulseSensor(PulseSensor<V> pulseSensor);

    /**
     * visit a {@link RfidSensor}.
     *
     * @param rfidSensor to be visited
     */

    V visitRfidSensor(RfidSensor<V> rfidSensor);

    /**
     * visit a {@link VemlLightSensor}.
     *
     * @param vemlLightSensor to be visited
     */

    V visitVemlLightSensor(VemlLightSensor<V> vemlLightSensor);

    /**
     * visit a {@link ParticleSensor}.
     *
     * @param particleSensor to be visited
     */

    V visitParticleSensor(ParticleSensor<V> particleSensor);

    /**
     * visit a {@link HTColorSensor}.
     *
     * @param htColorSensor to be visited
     */
    V visitHTColorSensor(HTColorSensor<V> htColorSensor);
}
