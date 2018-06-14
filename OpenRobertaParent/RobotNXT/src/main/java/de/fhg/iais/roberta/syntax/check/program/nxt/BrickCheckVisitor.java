package de.fhg.iais.roberta.syntax.check.program.nxt;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.visitor.nxt.NxtAstVisitor;

public class BrickCheckVisitor extends RobotBrickCheckVisitor implements NxtAstVisitor<Void> {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

    @Override
    public Void visitLedAction(LedAction<Void> ledAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
