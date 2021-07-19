package de.fhg.iais.roberta.visitor.hardware.actor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractValidatorAndCollectorVisitor;

public class MotorValidatorAndCollectorVisitor extends AbstractValidatorAndCollectorVisitor implements IMotorVisitor<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    public MotorValidatorAndCollectorVisitor(
        IVisitor<Void> mainVisitor,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(mainVisitor, robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPort(motorGetPowerAction);
        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorGetPowerAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorGetPowerAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.getParam().getSpeed());
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        if ( duration != null ) {
            // Instead of visitDuration ??
            requiredComponentVisited(motorOnAction, motorOnAction.getParam().getDuration().getValue());
        } else {
            checkForZeroSpeed(motorOnAction.getParam().getSpeed(), motorOnAction);
        }

        checkMotorPort(motorOnAction);
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
            boolean hasDuration = motorOnAction.getParam().getDuration() != null;
            if ( usedConfigurationBlock == null ) {
                addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else if ( SC.OTHER.equals(usedConfigurationBlock.getComponentType()) && hasDuration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }

        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorOnAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
        requiredComponentVisited(motorSetPowerAction, motorSetPowerAction.getPower());

        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorSetPowerAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorSetPowerAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorStopAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    protected void checkForZeroSpeed(Expr<Void> speed, Action<Void> action) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst<Void> speedNumConst = (NumConst<Void>) speed;
            if ( Math.abs(Double.valueOf(speedNumConst.getValue())) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    protected void checkMotorPort(MoveAction<Void> moveAction) {
        if ( this.robotConfiguration.optConfigurationComponent(moveAction.getUserDefinedPort()) == null ) {
            addErrorToPhrase(moveAction, "CONFIGURATION_ERROR_MOTOR_MISSING");
        }
    }
}
