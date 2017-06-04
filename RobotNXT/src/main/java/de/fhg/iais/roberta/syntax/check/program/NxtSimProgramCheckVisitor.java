package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.visitor.NxtAstVisitor;

public class NxtSimProgramCheckVisitor extends SimulationProgramCheckVisitor implements NxtAstVisitor<Void> {

    public NxtSimProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

}
