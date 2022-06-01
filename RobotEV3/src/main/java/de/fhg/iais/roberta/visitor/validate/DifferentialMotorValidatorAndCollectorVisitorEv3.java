package de.fhg.iais.roberta.visitor.validate;

import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.visitor.EV3DevMethods;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;

public abstract class DifferentialMotorValidatorAndCollectorVisitorEv3 extends MotorValidatorAndCollectorVisitor implements IDifferentialMotorVisitor<Void> {

    public DifferentialMotorValidatorAndCollectorVisitorEv3(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        if ( Objects.equals(robotConfiguration.getRobotName(), "ev3dev") ) {
            /*
             Only add the helper methods for "ev3dev". Following methods are needed for visitCurveAction:
             -  EV3DevMethods.BUSY_WAIT, EV3DevMethods.CLAMP, EV3DevMethods.DRIVE_IN_CURVE, EV3DevMethods.SCALE_SPEED
            */
            usedMethodBuilder.addUsedMethod(EV3DevMethods.BUSY_WAIT);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.CLAMP);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.DRIVE_IN_CURVE);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.SCALE_SPEED);
        }
        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        Optional.ofNullable(curveAction.getParamLeft().getDuration())
            .ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        Optional.ofNullable(curveAction.getParamRight().getDuration())
            .ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        checkLeftRightMotorPort(curveAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.getParam());
        checkLeftRightMotorPort(turnAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkAndVisitMotionParam(driveAction, driveAction.getParam());
        checkLeftRightMotorPort(driveAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkLeftRightMotorPort(stopAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        if ( hasTooManyMotors(driveAction) ) {
            return;
        }

        ConfigurationComponent leftMotor = this.robotConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.robotConfiguration.getFirstMotor(SC.RIGHT);
        checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
        checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
        checkMotorRotationDirection(driveAction, leftMotor, rightMotor);
    }

    protected boolean hasTooManyMotors(Phrase<Void> driveAction) {
        if ( this.robotConfiguration.getMotors(SC.RIGHT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS");
            return true;
        }
        if ( this.robotConfiguration.getMotors(SC.LEFT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS");
            return true;
        }
        return false;
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

    private void checkMotorRotationDirection(Phrase<Void> driveAction, ConfigurationComponent leftMotor, ConfigurationComponent rightMotor) {
        if ( (leftMotor == null) || (rightMotor == null) ) {
            return;
        }

        boolean rotationDirectionsEqual = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(rightMotor.getProperty(SC.MOTOR_REVERSE));
        if ( !rotationDirectionsEqual ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION");
        }
    }

    private void checkForZeroSpeedInCurve(Expr<Void> speedLeft, Expr<Void> speedRight, Action<Void> action) {
        if ( speedLeft.getKind().hasName("NUM_CONST") && speedRight.getKind().hasName("NUM_CONST") ) {
            double speedLeftNumConst = Double.parseDouble(((NumConst<Void>) speedLeft).getValue());
            double speedRightNumConst = Double.parseDouble(((NumConst<Void>) speedRight).getValue());

            boolean bothMotorsHaveZeroSpeed = (Math.abs(speedLeftNumConst) < DOUBLE_EPS) && (Math.abs(speedRightNumConst) < DOUBLE_EPS);
            if ( bothMotorsHaveZeroSpeed ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private void addLeftAndRightMotorToUsedActors() {
        Optional<String> optionalLeftPort = Optional.ofNullable(robotConfiguration.getFirstMotor(SC.LEFT))
            .map(ConfigurationComponent::getUserDefinedPortName);

        Optional<String> optionalRightPort = Optional.ofNullable(robotConfiguration.getFirstMotor(SC.RIGHT))
            .map(ConfigurationComponent::getUserDefinedPortName);

        if ( optionalLeftPort.isPresent() && optionalRightPort.isPresent() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(optionalLeftPort.get(), SC.LARGE));
            usedHardwareBuilder.addUsedActor(new UsedActor(optionalRightPort.get(), SC.LARGE));
        }
    }
}