package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

public interface ISenseboxVisitor<V> extends IDisplayVisitor<V>, ISimpleSoundVisitor<V>, ILightVisitor<V>, ISerialVisitor<V>, IPinVisitor<V>, INeuralNetworkVisitor<V>, INano33BleSensorVisitor<V>, IHardwareVisitor<V> {

    V visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor);

    V visitCompassSensor(CompassSensor<V> compassSensor);

    V visitEnvironmentalSensor(EnvironmentalSensor<V> environmentalSensor);

    V visitGpsSensor(GpsSensor<V> gpsSensor);

    V visitGyroSensor(GyroSensor<V> gyroSensor);

    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    V visitKeysSensor(KeysSensor<V> keysSensor);

    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    V visitParticleSensor(ParticleSensor<V> particleSensor);

    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    V visitPlotClearAction(PlotClearAction<V> plotClearAction);

    V visitPlotPointAction(PlotPointAction<V> plotPointAction);

    V visitRelayAction(RelayAction<V> relayAction);

    V visitSendDataAction(SendDataAction<V> sendDataAction);

    V visitSoundSensor(SoundSensor<V> soundSensor);

    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    V visitVemlLightSensor(VemlLightSensor<V> vemlLightSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitVoltageSensor(VoltageSensor<V> voltageSensor);
}