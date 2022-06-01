package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.visitor.hardware.IBotnrollVisitor;

public class BotnrollValidatorAndCollectorVisitor extends ArduinoDifferentialMotorValidatorAndCollectorVisitor implements IBotnrollVisitor<Void> {

    public BotnrollValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }


    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }


    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        checkAndVisitMotionParam(motorOnAction, motorOnAction.getParam());
        ConfigurationComponent actor = robotConfiguration.getConfigurationComponent(motorOnAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.y);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(voltageSensor.getUserDefinedPort(), SC.VOLTAGE, voltageSensor.getMode()));
        return null;
    }

}
