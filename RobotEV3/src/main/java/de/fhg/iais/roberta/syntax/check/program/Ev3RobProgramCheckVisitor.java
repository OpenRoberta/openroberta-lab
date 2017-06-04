package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;

public class Ev3RobProgramCheckVisitor extends RobotProgramCheckVisitor {

    public Ev3RobProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

}
