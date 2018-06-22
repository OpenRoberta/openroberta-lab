package de.fhg.iais.roberta.syntax.check.program.vorwerk;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.visitor.vorwerk.VorwerkAstVisitor;

public class BrickCheckVisitor extends RobotBrickCheckVisitor implements VorwerkAstVisitor<Void> {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitDropOffSensor(DropOffSensor<Void> dropOffSensor) {
        return null;
    }

    @Override
    public Void visitWallSensor(WallSensor<Void> wallSensor) {
        return null;
    }

    @Override
    public Void visitBrushOn(BrushOn<Void> brushOn) {
        return null;
    }

    @Override
    public Void visitBrushOff(BrushOff<Void> brushOff) {
        return null;
    }

    @Override
    public Void visitSideBrush(SideBrush<Void> sideBrush) {
        return null;
    }

    @Override
    public Void visitVacuumOn(VacuumOn<Void> vacuumOn) {
        return null;
    }

    @Override
    public Void visitVacuumOff(VacuumOff<Void> vacuumOff) {
        return null;
    }
}
