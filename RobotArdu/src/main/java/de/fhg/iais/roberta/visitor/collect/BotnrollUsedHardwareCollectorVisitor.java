package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

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
        UsedHardwareBean.Builder builder,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        ConfigurationAst robotConfiguration) {
        super(builder, robotConfiguration);
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.builder.addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.builder.addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.builder.addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.builder.addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor("B", SC.MEDIUM));
        this.builder.addUsedActor(new UsedActor("A", SC.MEDIUM));
        return null;
    }
}
