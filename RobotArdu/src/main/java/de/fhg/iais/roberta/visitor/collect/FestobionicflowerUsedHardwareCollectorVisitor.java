package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

/**
 * This visitor collects information for used actors and sensors in the NEPO AST
 */
public final class FestobionicflowerUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IFestobionicflowerVisitor<Void> {

    public FestobionicflowerUsedHardwareCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("internal", SC.RGBLED));
        ledOnAction.getLedColor().accept(this);
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("internal", SC.STEPMOTOR));
        stepMotorAction.getStepMotorPos().accept(this);
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }
}
