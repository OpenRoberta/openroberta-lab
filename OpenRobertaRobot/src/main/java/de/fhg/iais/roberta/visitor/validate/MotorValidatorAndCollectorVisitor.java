package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;

public abstract class MotorValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IMotorVisitor<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    public MotorValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPortAndAddUsedActor(motorGetPowerAction);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        checkAndVisitMotionParam(motorOnAction, motorOnAction.getParam());
        checkMotorPortAndAddUsedActor(motorOnAction);

        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
            boolean hasDuration = motorOnAction.getParam().getDuration() != null;
            if ( usedConfigurationBlock == null ) {
                addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else if ( SC.OTHER.equals(usedConfigurationBlock.getComponentType()) && hasDuration ) {
                addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED");
            }
        }

        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPortAndAddUsedActor(motorSetPowerAction);
        requiredComponentVisited(motorSetPowerAction, motorSetPowerAction.getPower());
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPortAndAddUsedActor(motorStopAction);
        return null;
    }

    protected void checkAndVisitMotionParam(Action<Void> action, MotionParam<Void> param) {
        MotorDuration<Void> duration = param.getDuration();
        Expr<Void> speed = param.getSpeed();

        requiredComponentVisited(action, speed);

        if ( duration != null ) {
            requiredComponentVisited(action, duration.getValue());
            checkForZeroSpeed(action, speed);
        }
    }

    protected void checkForZeroSpeed(Action<Void> action, Expr<Void> speed) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            if ( Math.abs(Double.parseDouble(((NumConst<Void>) speed).getValue())) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private void checkMotorPortAndAddUsedActor(MoveAction<Void> moveAction) {
        ConfigurationComponent actor = this.robotConfiguration.optConfigurationComponent(moveAction.getUserDefinedPort());
        if ( actor == null ) {
            addErrorToPhrase(moveAction, "CONFIGURATION_ERROR_MOTOR_MISSING");
            return;
        }

        usedHardwareBuilder.addUsedActor(new UsedActor(moveAction.getUserDefinedPort(), actor.getComponentType()));
    }

}
