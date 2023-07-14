package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

public final class FestobionicflowerValidatorAndCollectorVisitor extends ArduinoValidatorAndCollectorVisitor implements IFestobionicflowerVisitor<Void> {

    public FestobionicflowerValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            addErrorToPhrase(rgbLedOffAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        if ( rgbLedOnAction.colour.hasName("EMPTY_EXPR") ) {
            addErrorToPhrase(rgbLedOnAction, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            addErrorToPhrase(rgbLedOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(rgbLedOnAction, rgbLedOnAction.colour);
        usedHardwareBuilder.addUsedActor(new UsedActor("internal", SC.RGBLED));
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction stepMotorAction) {
        if ( stepMotorAction.stepMotorPos.hasName("EMPTY_EXPR") ) {
            addErrorToPhrase(stepMotorAction, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.STEPMOTOR) ) {
            addErrorToPhrase(stepMotorAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(stepMotorAction, stepMotorAction.stepMotorPos);
        usedHardwareBuilder.addUsedActor(new UsedActor("internal", SC.STEPMOTOR));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.TOUCH) ) {
            addErrorToPhrase(touchSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LIGHT) ) {
            addErrorToPhrase(lightSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }
}
