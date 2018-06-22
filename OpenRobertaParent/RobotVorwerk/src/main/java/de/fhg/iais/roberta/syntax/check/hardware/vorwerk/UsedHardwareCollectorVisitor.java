package de.fhg.iais.roberta.syntax.check.hardware.vorwerk;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.visitor.vorwerk.VorwerkAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements VorwerkAstVisitor<Void> {

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        super(brickConfiguration);
        check(phrasesSet);
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
