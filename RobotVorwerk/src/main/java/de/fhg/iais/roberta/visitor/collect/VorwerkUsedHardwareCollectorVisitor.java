package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.visitor.hardware.IVorwerkVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class VorwerkUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IVorwerkVisitor<Void> {

    public VorwerkUsedHardwareCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
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
