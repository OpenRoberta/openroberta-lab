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
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
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
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IArduinoVisitor<V> extends IVisitor<V> {

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitDropSensor(DropSensor dropSensor);

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitMotionSensor(MotionSensor motionSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroReset(GyroReset gyroReset);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitLightSensor(LightSensor lightSensor);

    V visitMoistureSensor(MoistureSensor moistureSensor);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitLightAction(LightAction lightAction);

    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitToneAction(ToneAction toneAction);

    V visitMainTask(MainTask mainTask);

    V visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitPulseSensor(PulseSensor pulseSensor);

    V visitRelayAction(RelayAction relayAction);

    V visitKeysSensor(KeysSensor button);

    V visitRfidSensor(RfidSensor rfidSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitVoltageSensor(VoltageSensor potentiometer);

    V visitNeuralNetworkSetup(NeuralNetworkSetup nn);

    V visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn);

    V visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn);

    V visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn);

    V visitNeuralNetworkTrain(NeuralNetworkTrain nn);

    V visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn);

    V visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn);

    V visitNeuralNetworkClassify(NeuralNetworkClassify nn);
}
