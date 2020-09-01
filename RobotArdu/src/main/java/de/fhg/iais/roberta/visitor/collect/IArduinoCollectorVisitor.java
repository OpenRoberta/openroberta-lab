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
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * Collector for the Arduino.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
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
}
