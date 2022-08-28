package de.fhg.iais.roberta.visitor.hardware.sensor;

import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
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

    default V visitKeysSensor(KeysSensor keysSensor) {
        throw new DbcException("KeysSensor not implemented!");
    }

    default V visitColorSensor(ColorSensor colorSensor) {
        throw new DbcException("ColorSensor not implemented!");
    }

    default V visitLightSensor(LightSensor lightSensor) {
        throw new DbcException("LightSensor not implemented!");
    }

    default V visitSoundSensor(SoundSensor soundSensor) {
        throw new DbcException("SoundSensor not implemented!");
    }

    default V visitEncoderSensor(EncoderSensor encoderSensor) {
        throw new DbcException("EncoderSensor not implemented!");
    }

    default V visitGyroSensor(GyroSensor gyroSensor) {
        throw new DbcException("GyroSensor not implemented!");
    }

    default V visitGyroReset(GyroReset gyroReset) {
        throw new DbcException("GyroReset not implemented!");
    }

    default V visitInfraredSensor(InfraredSensor infraredSensor) {
        throw new DbcException("InfraredSensor not implemented!");
    }

    default V visitTimerSensor(TimerSensor timerSensor) {
        throw new DbcException("TimerSensor not implemented!");
    }

    default V visitTouchSensor(TouchSensor touchSensor) {
        throw new DbcException("TouchSensor not implemented!");
    }

    default V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        throw new DbcException("UltrasonicSensor not implemented!");
    }

    default V visitCompassSensor(CompassSensor compassSensor) {
        throw new DbcException("CompassSensor not implemented!");
    }

    default V visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        throw new DbcException("TemperatureSensor not implemented!");
    }

    default V visitVoltageSensor(VoltageSensor voltageSensor) {
        throw new DbcException("VoltageSensor not implemented!");
    }

    default V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        throw new DbcException("AccelerometerSensor not implemented!");
    }

    default V visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        throw new DbcException("PinTouchSensor not implemented!");
    }

    default V visitGestureSensor(GestureSensor gestureSensor) {
        throw new DbcException("GestureSensor not implemented!");
    }

    default V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        throw new DbcException("PinGetValueSensor not implemented!");
    }

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        sensorGetSample.sensor.accept(this);
        return null;
    }

    default V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        throw new DbcException("IRSeekerSensor not implemented!");
    }

    default V visitMoistureSensor(MoistureSensor moistureSensor) {
        throw new DbcException("MoistureSensor not implemented!");
    }

    default V visitHumiditySensor(HumiditySensor humiditySensor) {
        throw new DbcException("HumiditySensor not implemented!");
    }

    default V visitMotionSensor(MotionSensor motionSensor) {
        throw new DbcException("MotionSensor not implemented!");
    }

    default V visitDropSensor(DropSensor dropSensor) {
        throw new DbcException("DropSensor not implemented!");
    }

    default V visitPulseSensor(PulseSensor pulseSensor) {
        throw new DbcException("PulseSensor not implemented!");
    }

    default V visitRfidSensor(RfidSensor rfidSensor) {
        throw new DbcException("RfidSensor not implemented!");
    }

    default V visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        throw new DbcException("VEML light sensor not implemented!");
    }

    default V visitParticleSensor(ParticleSensor particleSensor) {
        throw new DbcException("Particle sensor not implemented!");
    }

    default V visitHTColorSensor(HTColorSensor htColorSensor) {
        throw new DbcException("HTColorSensor not implemented!");
    }
}
