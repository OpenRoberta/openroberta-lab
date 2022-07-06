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
    V visitKeysSensor(KeysSensor keysSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitVoltageSensor(VoltageSensor voltageSensor);

    V visitAccelerometer(AccelerometerSensor accelerometerSensor);

    V visitPinTouchSensor(PinTouchSensor pinTouchSensor);

    V visitGestureSensor(GestureSensor gestureSensor);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitGetSampleSensor(GetSampleSensor sensorGetSample);

    V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor);

    V visitMoistureSensor(MoistureSensor moistureSensor);

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitMotionSensor(MotionSensor motionSensor);

    V visitDropSensor(DropSensor dropSensor);

    V visitPulseSensor(PulseSensor pulseSensor);

    V visitRfidSensor(RfidSensor rfidSensor);

    V visitVemlLightSensor(VemlLightSensor vemlLightSensor);

    V visitParticleSensor(ParticleSensor particleSensor);

    V visitHTColorSensor(HTColorSensor htColorSensor);
}
