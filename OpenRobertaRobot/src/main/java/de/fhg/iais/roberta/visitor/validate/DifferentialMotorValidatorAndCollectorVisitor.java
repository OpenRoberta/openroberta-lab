package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;

public abstract class DifferentialMotorValidatorAndCollectorVisitor extends MotorValidatorAndCollectorVisitor implements IDifferentialMotorVisitor<Void> {

    public DifferentialMotorValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkAndAddDifferentialDriveBlock(driveAction);
        checkAndVisitMotionParam(driveAction, driveAction.getParam());
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        checkAndAddDifferentialDriveBlock(curveAction);
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.getParam());
        checkAndAddDifferentialDriveBlock(turnAction);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkAndAddDifferentialDriveBlock(stopAction);
        return null;
    }

    private void checkAndAddDifferentialDriveBlock(Action<Void> motionAction) {
        if ( hasDifferentialDriveCheck(motionAction) ) {
            hasEncodersOnDifferentialDriveCheck(motionAction);
            addDifferentialDriveToUsedHardware();
        }
    }

    private boolean hasDifferentialDriveCheck(Action<Void> motionAction) {
        ConfigurationComponent differentialDrive = getDifferentialDrive();
        if ( differentialDrive == null ) {
            addErrorToPhrase(motionAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            return false;
        } else if ( differentialDrive.getOptProperty("MOTOR_L").equals(differentialDrive.getOptProperty("MOTOR_R")) ) {
            addErrorToPhrase(motionAction, "CONFIGURATION_ERROR_OVERLAPPING_PORTS");
            return false;
        }
        return true;
    }

    private ConfigurationComponent getDifferentialDrive() {
        Map<String, ConfigurationComponent> configComponents = this.robotConfiguration.getConfigurationComponents();
        for ( ConfigurationComponent component : configComponents.values() ) {
            if ( component.getComponentType().equals(SC.DIFFERENTIALDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    private void hasEncodersOnDifferentialDriveCheck(Action<Void> motionParam) {
        ConfigurationComponent differentialDrive = getDifferentialDrive();
        Assert.notNull(differentialDrive, "differentialDrive block is missing in the configuration");
        List<ConfigurationComponent> rightMotors = getEncodersOnPort(differentialDrive.getOptProperty("MOTOR_R"));
        List<ConfigurationComponent> leftMotors = getEncodersOnPort(differentialDrive.getOptProperty("MOTOR_L"));

        if ( rightMotors.size() > 1 ) {
            addErrorToPhrase(motionParam, "CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS");
        } else if ( leftMotors.size() > 1 ) {
            addErrorToPhrase(motionParam, "CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS");
        } else if ( rightMotors.isEmpty() ) {
            addErrorToPhrase(motionParam, "CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING");
        } else if ( leftMotors.isEmpty() ) {
            addErrorToPhrase(motionParam, "CONFIGURATION_ERROR_MOTOR_LEFT_MISSING");
        }
    }

    private void addDifferentialDriveToUsedHardware() {
        ConfigurationComponent diffDrive = getDifferentialDrive();
        Assert.notNull(diffDrive, "differential missing in Configuration");

        usedHardwareBuilder.addUsedActor(new UsedActor(diffDrive.getUserDefinedPortName(), SC.DIFFERENTIALDRIVE));
        List<ConfigurationComponent> motorsR = getEncodersOnPort(diffDrive.getOptProperty("MOTOR_R"));
        List<ConfigurationComponent> motorsL = getEncodersOnPort(diffDrive.getOptProperty("MOTOR_L"));
        if ( !motorsL.isEmpty() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorsL.get(0).getUserDefinedPortName(), motorsL.get(0).getComponentType()));
        }
        if ( !motorsR.isEmpty() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorsR.get(0).getUserDefinedPortName(), motorsR.get(0).getComponentType()));
        }
    }

    private List<ConfigurationComponent> getEncodersOnPort(String port) {
        Map<String, ConfigurationComponent> configComponents = this.robotConfiguration.getConfigurationComponents();
        List<ConfigurationComponent> encoders = new ArrayList<>();
        for ( ConfigurationComponent component : configComponents.values() ) {
            if ( component.getComponentType().equals(SC.ENCODER) || component.getComponentType().equals(SC.MOTOR) || component.getComponentType().equals(SC.STEPMOTOR) ) {
                if ( component.getComponentProperties().containsValue(port) ) {
                    encoders.add(component);
                }
            }
        }
        return encoders;
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
}
