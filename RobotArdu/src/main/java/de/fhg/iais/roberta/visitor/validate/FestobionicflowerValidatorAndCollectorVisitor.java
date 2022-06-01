package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

public final class FestobionicflowerValidatorAndCollectorVisitor extends ArduinoValidatorAndCollectorVisitor implements IFestobionicflowerVisitor<Void> {

    public FestobionicflowerValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            addErrorToPhrase(ledOffAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        if ( ledOnAction.getLedColor().getKind().getName().equals("EMPTY_EXPR") ) {
            addErrorToPhrase(ledOnAction, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            addErrorToPhrase(ledOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(ledOnAction, ledOnAction.getLedColor());
        usedHardwareBuilder.addUsedActor(new UsedActor("internal", SC.RGBLED));
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        if ( stepMotorAction.getStepMotorPos().getKind().getName().equals("EMPTY_EXPR") ) {
            addErrorToPhrase(stepMotorAction, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.STEPMOTOR) ) {
            addErrorToPhrase(stepMotorAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(stepMotorAction, stepMotorAction.getStepMotorPos());
        usedHardwareBuilder.addUsedActor(new UsedActor("internal", SC.STEPMOTOR));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.TOUCH) ) {
            addErrorToPhrase(touchSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LIGHT) ) {
            addErrorToPhrase(lightSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }
}
