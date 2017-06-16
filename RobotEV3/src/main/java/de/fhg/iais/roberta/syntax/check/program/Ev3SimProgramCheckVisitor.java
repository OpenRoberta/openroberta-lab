package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

public class Ev3SimProgramCheckVisitor extends SimulationProgramCheckVisitor {

    public Ev3SimProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}
