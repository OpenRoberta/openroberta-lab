package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

public interface IArduinoVisitor<V>
    extends IDisplayVisitor<V>, ISimpleSoundVisitor<V>, ILightVisitor<V>, ISerialVisitor<V>, IPinVisitor<V>, INeuralNetworkVisitor<V>, INano33BleSensorVisitor<V>, IHardwareVisitor<V> {

    V visitDropSensor(DropSensor<V> dropSensor);

    default V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        return sensorGetSample.getSensor().accept(this);
    }

    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    V visitMotionSensor(MotionSensor<V> motionSensor);

    V visitAccelerometerSensor(AccelerometerSensor<V> accelerometerSensor);

    V visitGyroSensor(GyroSensor<V> gyroSensor);

    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitMoistureSensor(MoistureSensor<V> moistureSensor);

    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    V visitPulseSensor(PulseSensor<V> pulseSensor);

    V visitRelayAction(RelayAction<V> relayAction);

    V visitKeysSensor(KeysSensor<Void> button);

    V visitRfidSensor(RfidSensor<V> rfidSensor);

    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    V visitVoltageSensor(VoltageSensor<V> voltageSensor);

}
