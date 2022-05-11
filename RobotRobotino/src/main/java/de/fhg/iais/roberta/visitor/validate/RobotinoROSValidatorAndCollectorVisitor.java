package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensorReset;
import de.fhg.iais.roberta.visitor.RobotinoMethods;

public class RobotinoROSValidatorAndCollectorVisitor extends AbstractRobotinoValidatorAndCollectorVisitor {

    public RobotinoROSValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        addMotorMethods();
        return super.visitOmnidriveAction(omnidriveAction);
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVEFORDISTANCE);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETPOSITION);
        return super.visitOmnidriveDistanceAction(omnidriveDistanceAction);
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETORIENTATION);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.TURNFORDEGREES);
        return super.visitTurnAction(turnAction);
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETORIENTATION);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVETOPOSITION);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDIRECTION);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETPOSITION);
        return super.visitOmnidrivePositionAction(omnidrivePositionAction);
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        if ( odometrySensor.getSlot().equals("THETA") ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETORIENTATION);
        }
        return super.visitOdometrySensor(odometrySensor);
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        if ( !odometrySensorReset.slot.equals("THETA") ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETORIENTATION);
        }
        if ( !odometrySensorReset.slot.equals("ALL") ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        }
        return super.visitOdometrySensorReset(odometrySensorReset);

    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        addMotorMethods();
        return super.visitMotorDriveStopAction(stopAction);
    }


    private void addMotorMethods() {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.OMNIDRIVESPEED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.PUBLISHVEL);
    }

}
