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
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISensorVisitorWithoutDefaults<V> extends IHardwareVisitor<V> {
    V visitKeysSensor(KeysSensor<V> keysSensor);

    V visitColorSensor(ColorSensor<V> colorSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitSoundSensor(SoundSensor<V> soundSensor);

    V visitEncoderSensor(EncoderSensor<V> encoderSensor);

    V visitGyroSensor(GyroSensor<V> gyroSensor);

    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitTouchSensor(TouchSensor<V> touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    V visitCompassSensor(CompassSensor<V> compassSensor);

    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitVoltageSensor(VoltageSensor<V> voltageSensor);

    V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor);

    V visitPinTouchSensor(PinTouchSensor<V> pinTouchSensor);

    V visitGestureSensor(GestureSensor<V> gestureSensor);

    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample);

    V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor);

    V visitMoistureSensor(MoistureSensor<V> moistureSensor);

    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    V visitMotionSensor(MotionSensor<V> motionSensor);

    V visitDropSensor(DropSensor<V> dropSensor);

    V visitPulseSensor(PulseSensor<V> pulseSensor);

    V visitRfidSensor(RfidSensor<V> rfidSensor);

    V visitVemlLightSensor(VemlLightSensor<V> vemlLightSensor);

    V visitParticleSensor(ParticleSensor<V> particleSensor);

    V visitHTColorSensor(HTColorSensor<V> htColorSensor);
}
