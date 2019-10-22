package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISpeechVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IEv3Visitor<V> extends IActors4AutonomousDriveRobots<V>, IBluetoothVisitor<V>, ISpeechVisitor<V>, ISensorVisitor<V> {
    default V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        throw new DbcException("Block is not implemented!");
    }
}
