package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.INIBOVisitor;

public abstract class NIBOValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements INIBOVisitor<Void> {

    private final boolean isSim;

    public NIBOValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders, boolean isSim) {
        super(brickConfiguration, beanBuilders);
        this.isSim = isSim;
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction bodyLEDAction) {
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        addToPhraseIfUnsupportedInSim(sendIRAction, false, isSim);
        requiredComponentVisited(sendIRAction, sendIRAction.code);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        addToPhraseIfUnsupportedInSim(receiveIRAction, true, isSim);
        return null;
    }

    @Override
    public Void visitRememberAction(RememberAction rememberAction) {
        requiredComponentVisited(rememberAction, rememberAction.code);
        return null;
    }

    @Override
    public Void visitRecallAction(RecallAction recallAction) {
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction ledOffAction) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        requiredComponentVisited(ledOnAction, ledOnAction.ledColor);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(temperatureSensor.getUserDefinedPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitCodePadSensor(CodePadSensor codePadSensor) {
        addToPhraseIfUnsupportedInSim(codePadSensor, true, isSim);
        return null;
    }
}
