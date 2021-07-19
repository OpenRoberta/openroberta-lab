package de.fhg.iais.roberta.visitor.hardware.actor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.visitor.IVisitor;

public class DifferentialMotorValidatorAndCollectorVisitor extends MotorValidatorAndCollectorVisitor implements IDifferentialMotorVisitor<Void> {

    public DifferentialMotorValidatorAndCollectorVisitor(IVisitor<Void> mainVisitor, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(mainVisitor, robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        visitMotorDuration(curveAction, curveAction.getParamLeft().getDuration());
        visitMotorDuration(curveAction, curveAction.getParamRight().getDuration());

        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        checkLeftRightMotorPort(curveAction);
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkLeftRightMotorPort(turnAction);
        requiredComponentVisited(turnAction, turnAction.getParam().getSpeed());

        Expr<Void> speed = turnAction.getParam().getSpeed();
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, turnAction);
        }
        visitMotorDuration(turnAction, duration);
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);
        Expr<Void> speed = driveAction.getParam().getSpeed();
        requiredComponentVisited(driveAction, driveAction.getParam().getSpeed());

        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, driveAction);
        }
        visitMotorDuration(driveAction, duration);

        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkLeftRightMotorPort(stopAction);
        return null;
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        if ( validNumberOfMotors(driveAction) ) {
            ConfigurationComponent leftMotor = this.robotConfiguration.getFirstMotor(SC.LEFT);
            ConfigurationComponent rightMotor = this.robotConfiguration.getFirstMotor(SC.RIGHT);
            checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
            checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
            checkMotorRotationDirection(driveAction, leftMotor, rightMotor);
        }
    }

    protected boolean validNumberOfMotors(Phrase<Void> driveAction) {
        if ( this.robotConfiguration.getMotors(SC.RIGHT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS");
            return false;
        }
        if ( this.robotConfiguration.getMotors(SC.LEFT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS");
            return false;
        }
        return true;
    }

    private void checkRightMotorPresenceAndRegulation(Phrase<Void> driveAction, ConfigurationComponent rightMotor) {
        if ( rightMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, rightMotor, "CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED");
        }
    }

    private void checkLeftMotorPresenceAndRegulation(Phrase<Void> driveAction, ConfigurationComponent leftMotor) {
        if ( leftMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_LEFT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, leftMotor, "CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED");
        }
    }

    private void checkIfMotorRegulated(Phrase<Void> driveAction, ConfigurationComponent motor, String errorMsg) {
        if ( !motor.getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE) ) {
            addErrorToPhrase(driveAction, errorMsg);
        }
    }

    private void checkMotorRotationDirection(Phrase<Void> driveAction, ConfigurationComponent m1, ConfigurationComponent m2) {
        if ( (m1 != null) && (m2 != null) && !m1.getProperty(SC.MOTOR_REVERSE).equals(m2.getProperty(SC.MOTOR_REVERSE)) ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION");
        }
    }

    protected void checkMotorPort(MoveAction<Void> action) {
        if ( this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort()) == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_MOTOR_MISSING");
        }
    }

    private void checkForZeroSpeedInCurve(Expr<Void> speedLeft, Expr<Void> speedRight, Action<Void> action) {
        if ( speedLeft.getKind().hasName("NUM_CONST") && speedRight.getKind().hasName("NUM_CONST") ) {
            double speedLeftNumConst = Double.parseDouble(((NumConst<Void>) speedLeft).getValue());
            double speedRightNumConst = Double.parseDouble(((NumConst<Void>) speedRight).getValue());
            if ( (Math.abs(speedLeftNumConst) < DOUBLE_EPS) && (Math.abs(speedRightNumConst) < DOUBLE_EPS) ) {
                addWarningToPhrase(action, "BLOCK_NOT_EXECUTED");
            }
        }
    }

    private void visitMotorDuration(Phrase<Void> action, MotorDuration<Void> duration) {
        // TODO
        if ( duration != null ) {
            requiredComponentVisited(action, duration.getValue());
        }
    }

    private void addLeftAndRightMotorToUsedActors() {
        //TODO: remove the check
        if ( this.robotConfiguration != null ) {
            String userDefinedLeftPortName = this.robotConfiguration.getFirstMotor("LEFT").getUserDefinedPortName();
            String userDefinedRightPortName = this.robotConfiguration.getFirstMotor("RIGHT").getUserDefinedPortName();
            if ( (userDefinedLeftPortName != null) && (userDefinedRightPortName != null) ) {
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedLeftPortName, SC.LARGE));
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedRightPortName, SC.LARGE));
            }
        }
    }
}