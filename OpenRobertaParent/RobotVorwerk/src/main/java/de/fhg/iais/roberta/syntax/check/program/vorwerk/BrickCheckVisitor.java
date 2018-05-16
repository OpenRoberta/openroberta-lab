package de.fhg.iais.roberta.syntax.check.program.vorwerk;

import de.fhg.iais.roberta.components.Configuration;
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

}
