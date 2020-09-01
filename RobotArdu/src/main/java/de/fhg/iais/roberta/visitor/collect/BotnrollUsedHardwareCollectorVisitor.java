package de.fhg.iais.roberta.visitor.collect;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.visitor.hardware.IBotnrollVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class BotnrollUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IBotnrollVisitor<Void> {

    public BotnrollUsedHardwareCollectorVisitor(
        List<List<Phrase<Void>>> phrasesSet,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }
}
