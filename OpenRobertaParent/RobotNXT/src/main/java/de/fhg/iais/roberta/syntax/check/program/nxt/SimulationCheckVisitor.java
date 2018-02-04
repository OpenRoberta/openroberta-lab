package de.fhg.iais.roberta.syntax.check.program.nxt;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.nxt.NxtAstVisitor;

public class SimulationCheckVisitor extends RobotSimulationCheckVisitor implements NxtAstVisitor<Void> {

    public SimulationCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }
   
}
