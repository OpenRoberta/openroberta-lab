package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedDimAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedGetAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedSetAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.syntax.lang.stmt.IntentStmt;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SlotSensor;
import de.fhg.iais.roberta.visitor.collect.RaspberryPiMethods;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

public class RaspberryPiValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IRaspberryPiVisitor<Void> {

    public RaspberryPiValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
        usedMethodBuilder.addUsedMethod(RaspberryPiMethods.SIGNAL);
    }

    @Override
    public Void visitColorHexString(ColorHexString<Void> colorHexString) {
        return null;
    }

    @Override
    public Void visitLedSetAction(LedSetAction<Void> ledSetAction) {
        return null;
    }

    @Override
    public Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        return null;
    }

    @Override
    public Void visitLedDimAction(LedDimAction<Void> ledDimAction) {
        return null;
    }

    @Override
    public Void visitLedGetAction(LedGetAction<Void> ledGetAction) {
        return null;
    }

    @Override
    public Void visitIntentStmt(IntentStmt<Void> intentStmt) {
        usedMethodBuilder.addUsedMethod(RaspberryPiMethods.CONTAINS);
        usedHardwareBuilder.addUsedIntents(intentStmt.getIntent());
        intentStmt.getExpr().forEach(expr -> requiredComponentVisited(intentStmt, expr));
        intentStmt.getThenList().forEach(expr -> optionalComponentVisited(expr));
        optionalComponentVisited(intentStmt.getElseList());
        return null;
    }

    @Override
    public Void visitSlotSensor(SlotSensor<Void> slotSensor) {
        usedMethodBuilder.addUsedMethod(RaspberryPiMethods.CONTAINS);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        usedHardwareBuilder.addDeclaredVariable("board");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        usedMethodBuilder.addUsedMethod(RaspberryPiMethods.KEY_PRESSED);
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        optionalComponentVisited(sayTextAction.getSpeed());
        optionalComponentVisited(sayTextAction.getPitch());
        requiredComponentVisited(sayTextAction, sayTextAction.getMsg());
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.VOICE));
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        optionalComponentVisited(volumeAction.getVolume());
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    protected ConfigurationComponent checkSensorExists(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "ULTRASONIC_SENSING": //example
                    if ( !(type.equals("ULTRASONIC") || type.equals("CALLIBOT")) ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                default:
                    break;
            }
        }
        return usedSensor;
    }
}
