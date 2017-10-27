package de.fhg.iais.roberta.syntax.check.program.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

public class SimulationCheckVisitor extends RobotSimulationCheckVisitor {

    public SimulationCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

}
