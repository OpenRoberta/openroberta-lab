package de.fhg.iais.roberta.visitor.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IWeDoVisitor;

public class WedoValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IWeDoVisitor<Void> {
    private static final double DOUBLE_EPS = 1E-7;

    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("KEYS_SENSING", SC.KEY);
        put("INFRARED_SENSING", SC.INFRARED);
        put("TIMER_SENSING", SC.TIMER);
        put("GYRO_SENSING", SC.GYRO);
    }});

    private static final Map<String, String> ACTOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("MOTOR_ON_ACTION", SC.MOTOR);
        put("MOTOR_STOP_ACTION", SC.MOTOR);
        put("LIGHT_ACTION", SC.LED);
        put("LIGHT_STATUS_ACTION", SC.LED);
        put("TONE_ACTION", SC.BUZZER);
        put("PLAY_NOTE_ACTION", SC.BUZZER);
    }});

    public WedoValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        checkActorPresence(lightAction);
        requiredComponentVisited(lightAction, lightAction.rgbLedColor);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        checkActorPresence(lightStatusAction);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        checkActorPresence(motorOnAction);
        if ( motorOnAction.param.getDuration() != null ) {
            requiredComponentVisited(motorOnAction, motorOnAction.param.getDuration().getValue());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), SC.MOTOR));
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        checkActorPresence(motorStopAction);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        checkActorPresence(toneAction);
        requiredComponentVisited(toneAction, toneAction.frequency, toneAction.duration);
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        checkActorPresence(playNoteAction);
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
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPresence(keysSensor);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        checkSensorPresence(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        checkSensorPresence(infraredSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    private void checkSensorPresence(ExternalSensor sensor) {
        if ( !robotConfiguration.isComponentTypePresent(SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName())) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
    }

    private void checkActorPresence(Action actor) {
        if ( !robotConfiguration.isComponentTypePresent(ACTOR_COMPONENT_TYPE_MAP.get(actor.getKind().getName())) ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
    }
}
