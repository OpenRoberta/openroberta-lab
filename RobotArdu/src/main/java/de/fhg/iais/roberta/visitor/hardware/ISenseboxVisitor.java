package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface ISenseboxVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor);

    V visitGpsSensor(GpsSensor gpsSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroReset(GyroReset gyroReset);

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitParticleSensor(ParticleSensor particleSensor);

    V visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitPlotClearAction(PlotClearAction plotClearAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitPlotPointAction(PlotPointAction plotPointAction);

    V visitLightAction(LightAction lightAction);

    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitRelayAction(RelayAction relayAction);

    V visitSendDataAction(SendDataAction sendDataAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitVemlLightSensor(VemlLightSensor vemlLightSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitVoltageSensor(VoltageSensor voltageSensor);
}