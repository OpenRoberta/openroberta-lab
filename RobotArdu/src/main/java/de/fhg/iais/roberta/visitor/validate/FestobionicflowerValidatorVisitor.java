package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.components.ConfigurationComponent;

public final class FestobionicflowerValidatorVisitor extends AbstractProgramValidatorVisitor implements IFestobionicflowerVisitor<Void> {

    public FestobionicflowerValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        }
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            ledOffAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        if ( ledOnAction.getLedColor().getKind().getName().equals("EMPTY_EXPR") ) {
            ledOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            ledOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        if ( stepMotorAction.getStepMotorPos().getKind().getName().equals("EMPTY_EXPR") ) {
            stepMotorAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else if ( !this.robotConfiguration.isComponentTypePresent(SC.STEPMOTOR) ) {
            stepMotorAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.TOUCH) ) {
            touchSensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LIGHT) ) {
            lightSensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }
}
