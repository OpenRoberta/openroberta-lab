package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * Collector for the Arduino. Adds the blocks missing from the defaults of {@link ICollectorVisitor}. Defines the specific parent implementation to use (the one
 * of the collector) due to unrelated defaults.
 */
public interface IArduinoCollectorVisitor extends ICollectorVisitor, IArduinoVisitor<Void> {

    @Override
    default Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        pinWriteValueAction.getValue().accept(this);
        return null;
    }

    @Override
    default Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    default Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        sendDataAction.getId2Phenomena().forEach(stringExprPair -> {
            stringExprPair.getSecond().accept(this);
        });
        return null;
    }

    @Override
    default Void visitPlotPointAction(PlotPointAction<Void> plotPointAction) {
        plotPointAction.getValue().accept(this);
        plotPointAction.getTickmark().accept(this);
        return null;
    }

    @Override
    default Void visitPlotClearAction(PlotClearAction<Void> plotClearAction) {
        return null;
    }

    @Override
    default Void visitGpsSensor(GpsSensor<Void> gpsSensor) {
        return null;
    }

    @Override
    default Void visitEnvironmentalSensor(EnvironmentalSensor<Void> environmentalSensor) {
        return null;
    }

    // following methods are used to specify unrelated defaults

    @Override
    default Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return ICollectorVisitor.super.visitMotorGetPowerAction(motorGetPowerAction);
    }

    @Override
    default Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return ICollectorVisitor.super.visitMotorSetPowerAction(motorSetPowerAction);
    }

    @Override
    default Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return ICollectorVisitor.super.visitMotorStopAction(motorStopAction);
    }

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return ICollectorVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return ICollectorVisitor.super.visitPlayNoteAction(playNoteAction);
    }

    @Override
    default Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return ICollectorVisitor.super.visitPlayFileAction(playFileAction);
    }

    @Override
    default Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> lsm9ds1AccSensor) {
        lsm9ds1AccSensor.getX().accept(this);
        lsm9ds1AccSensor.getY().accept(this);
        lsm9ds1AccSensor.getZ().accept(this);
        return null;
    }

    @Override
    default Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> lsm9ds1GyroSensor) {
        lsm9ds1GyroSensor.getX().accept(this);
        lsm9ds1GyroSensor.getY().accept(this);
        lsm9ds1GyroSensor.getZ().accept(this);
        return null;
    }

    @Override
    default Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> lsm9ds1MagneticFieldSensor) {
        lsm9ds1MagneticFieldSensor.getX().accept(this);
        lsm9ds1MagneticFieldSensor.getY().accept(this);
        lsm9ds1MagneticFieldSensor.getZ().accept(this);
        return null;
    }

    @Override
    default Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> apds9960DistanceSensor) {
        apds9960DistanceSensor.getDistance().accept(this);
        return null;
    }

    @Override
    default Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> apds9960GestureSensor) {
        apds9960GestureSensor.getGesture().accept(this);
        return null;
    }

    @Override
    default Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> apds9960ColorSensor) {
        apds9960ColorSensor.getR().accept(this);
        apds9960ColorSensor.getG().accept(this);
        apds9960ColorSensor.getB().accept(this);
        return null;
    }

    @Override
    default Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> lps22hbPressureSensor) {
        lps22hbPressureSensor.getPressure().accept(this);
        return null;
    }

    @Override
    default Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> hts221TemperatureSensor) {
        hts221TemperatureSensor.getTemperature().accept(this);
        return null;
    }

    @Override
    default Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> hts221HumiditySensor) {
        hts221HumiditySensor.getHumidity().accept(this);
        return null;
    }

    @Override
    default Void visitNeuralNetworkSetup(NeuralNetworkSetup<Void> nn) {
        nn.getNumberOfClasses().accept(this);
        nn.getNumberInputNeurons().accept(this);
        nn.getMaxNumberOfNeurons().accept(this);
        return null;
    }

    @Override
    default Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData<Void> nn) {
        return null;
    }

    @Override
    default Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData<Void> nn) {
        nn.getRawData().accept(this);
        return null;
    }

    @Override
    default Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData<Void> nn) {
        nn.getClassNumber().accept(this);
        return null;
    }

    @Override
    default Void visitNeuralNetworkTrain(NeuralNetworkTrain<Void> nn) {
        return null;
    }

    @Override
    default Void visitNeuralNetworkClassify(NeuralNetworkClassify<Void> nn) {
        return null;
    }
}
