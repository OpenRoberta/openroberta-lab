package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;

public class Ev3SimProgramCheckVisitor extends SimulationProgramCheckVisitor {

    public Ev3SimProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

}
