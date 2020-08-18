package de.fhg.iais.roberta.visitor.hardware;

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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IArduinoVisitor<V>
    extends IMotorVisitor<V>, IDisplayVisitor<V>, ISoundVisitor<V>, ILightVisitor<V>, ISensorVisitor<V>, ISerialVisitor<V>, IPinVisitor<V> {

    default V visitRelayAction(RelayAction<V> relayAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitDataSendAction(SendDataAction<V> sendDataAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitPlotPointAction(PlotPointAction<V> plotPointAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitPlotClearAction(PlotClearAction<V> plotClearAction) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitGpsSensor(GpsSensor<V> gpsSensor) {
        throw new DbcException("Block is not implemented!");
    }

    default V visitEnvironmentalSensor(EnvironmentalSensor<V> environmentalSensor) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        throw new DbcException("Not supported!");
    }
}
