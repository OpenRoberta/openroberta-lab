package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.visitor.NxtAstVisitor;

public class NxtRobProgramCheckVisitor extends RobotProgramCheckVisitor implements NxtAstVisitor<Void> {

    public NxtRobProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

}
