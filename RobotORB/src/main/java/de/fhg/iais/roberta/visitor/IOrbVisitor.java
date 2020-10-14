package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IOrbVisitor<V> extends IActors4AutonomousDriveRobots<V>, ISensorVisitor<V> {
    // TODO overwrite capabilites that ORB does NOT have
}
