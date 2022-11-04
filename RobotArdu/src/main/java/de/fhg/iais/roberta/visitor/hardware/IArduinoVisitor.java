package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actor.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

public interface IArduinoVisitor<V>
    extends IDisplayVisitor<V>, ISimpleSoundVisitor<V>, ILightVisitor<V>, IPinVisitor<V>, INeuralNetworkVisitor<V>, IHardwareVisitor<V> {

    V visitDropSensor(DropSensor dropSensor);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitMotionSensor(MotionSensor motionSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroReset(GyroReset gyroReset);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitMoistureSensor(MoistureSensor moistureSensor);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitPulseSensor(PulseSensor pulseSensor);

    V visitRelayAction(RelayAction relayAction);

    V visitKeysSensor(KeysSensor button);

    V visitRfidSensor(RfidSensor rfidSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitVoltageSensor(VoltageSensor voltageSensor);

}
