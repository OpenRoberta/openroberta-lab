package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousRobots;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IEv3Visitor<V> extends IActors4AutonomousRobots<V>, ISensorVisitor<V> {

}
