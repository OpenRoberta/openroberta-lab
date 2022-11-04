package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actor.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actor.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actor.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.sensebox.GpsSensor;
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
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

public interface ISenseboxVisitor<V> extends IDisplayVisitor<V>, ISimpleSoundVisitor<V>, ILightVisitor<V>, IPinVisitor<V>, INeuralNetworkVisitor<V>, IHardwareVisitor<V> {

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

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitPlotClearAction(PlotClearAction plotClearAction);

    V visitPlotPointAction(PlotPointAction plotPointAction);

    V visitRelayAction(RelayAction relayAction);

    V visitSendDataAction(SendDataAction sendDataAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitVemlLightSensor(VemlLightSensor vemlLightSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitVoltageSensor(VoltageSensor voltageSensor);
}