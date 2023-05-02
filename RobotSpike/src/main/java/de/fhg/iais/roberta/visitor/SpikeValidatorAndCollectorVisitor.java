package de.fhg.iais.roberta.visitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOffAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;


public class SpikeValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements ISpike {


    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
        put("COLOUR_SENSING", SC.COLOUR);
        put("KEYS_SENSING", SC.KEY);
    }});

    public SpikeValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, "DEFAULT"));
        return null;
    }

    @Override
    public Void visitDisplayClearAction(DisplayClearAction displayClearAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        requiredComponentVisited(displayImageAction, displayImageAction.valuesToDisplay);
        checkActorPort(displayImageAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "DISPLAY"));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        requiredComponentVisited(displayTextAction, displayTextAction.textToDisplay);
        checkActorPort(displayTextAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "DISPLAY"));
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        sensorGetSample.sensor.accept(this);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPort(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction ledOffAction) {
        checkActorPort(ledOffAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        requiredComponentVisited(ledOnAction, ledOnAction.colour);
        checkActorPort(ledOnAction);
        usedMethodBuilder.addUsedMethod(SpikeMethods.SETSTATUSLIGHT);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    public Void visitMainTask(MainTask mainTask) {
        requiredComponentVisited(mainTask, mainTask.variables);
        return null;
    }

    @Override
    public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        requiredComponentVisited(motorDiffCurveAction, motorDiffCurveAction.powerLeft, motorDiffCurveAction.powerRight);
        checkActorPort(motorDiffCurveAction);
        return checkDiffDrive(motorDiffCurveAction);
    }

    @Override
    public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        requiredComponentVisited(motorDiffCurveForAction, motorDiffCurveForAction.powerLeft, motorDiffCurveForAction.powerRight, motorDiffCurveForAction.distance);
        checkActorPort(motorDiffCurveForAction);
        return checkDiffDrive(motorDiffCurveForAction);
    }

    @Override
    public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        requiredComponentVisited(motorDiffOnAction, motorDiffOnAction.power);
        checkActorPort(motorDiffOnAction);
        return checkDiffDrive(motorDiffOnAction);
    }

    @Override
    public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        requiredComponentVisited(motorDiffOnForAction, motorDiffOnForAction.distance, motorDiffOnForAction.power);
        checkActorPort(motorDiffOnForAction);
        return checkDiffDrive(motorDiffOnForAction);
    }

    @Override
    public Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction) {
        checkActorPort(motorDiffStopAction);
        return checkDiffDrive(motorDiffStopAction);
    }

    @Override
    public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        requiredComponentVisited(motorDiffTurnAction, motorDiffTurnAction.power);
        checkActorPort(motorDiffTurnAction);
        return checkDiffDrive(motorDiffTurnAction);
    }

    @Override
    public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        requiredComponentVisited(motorDiffTurnForAction, motorDiffTurnForAction.degrees, motorDiffTurnForAction.power);
        checkActorPort(motorDiffTurnForAction);
        return checkDiffDrive(motorDiffTurnForAction);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.power);
        if ( checkActorPort(motorOnAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorOnAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        requiredComponentVisited(motorOnForAction, motorOnForAction.power, motorOnForAction.value);
        if ( checkActorPort(motorOnForAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorOnForAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        if ( checkActorPort(motorStopAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorStopAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        checkActorPort(playNoteAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        return null;
    }

    @Override
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        requiredComponentVisited(playToneAction, playToneAction.frequency, playToneAction.duration);
        checkActorPort(playToneAction);
        usedHardwareBuilder.addUsedActor(new UsedActor("", "HUB"));
        usedMethodBuilder.addUsedMethod(SpikeMethods.GETMIDI);
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        return null;
    }

    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        checkSensorPort(touchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(touchSensor.getUserDefinedPort(), SC.TOUCH, touchSensor.getMode()));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedMethodBuilder.addUsedMethod(SpikeMethods.GETSAMPLEULTRASONIC);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        usedHardwareBuilder.addUsedActor(new UsedActor(null, C.RANDOM_DOUBLE));
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        super.visitMathRandomIntFunct(mathRandomIntFunct);
        usedHardwareBuilder.addUsedActor(new UsedActor(null, C.RANDOM));
        return null;
    }

    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }

    private ConfigurationComponent getMotorFromUserName(String userName) {
        for ( Map.Entry<String, ConfigurationComponent> entry : this.robotConfiguration.getConfigurationComponents().entrySet() ) {
            ConfigurationComponent component = entry.getValue();
            String comProp = component.componentProperties.get("PORT");
            if ( comProp != null && comProp.equals(userName) ) {
                return component;
            }
        }
        return null;
    }

    private ConfigurationComponent getMotorFromPort(String portName) {
        for ( Map.Entry<String, ConfigurationComponent> entry : this.robotConfiguration.getConfigurationComponents().entrySet() ) {
            ConfigurationComponent component = entry.getValue();
            if ( component.userDefinedPortName.equals(portName) ) {
                return component;
            }
        }
        throw new DbcException("port " + portName + " is missing");
    }

    private Void checkDiffDrive(Phrase phrase) {
        ConfigurationComponent diffDrive = this.robotConfiguration.optConfigurationComponentByType("DIFFERENTIALDRIVE");
        if ( diffDrive == null ) {
            addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else {
            String leftUserPort = diffDrive.getComponentProperties().get("MOTOR_L");
            String rightUserPort = diffDrive.getComponentProperties().get("MOTOR_R");
            ConfigurationComponent leftMotor = getMotorFromUserName(leftUserPort);
            ConfigurationComponent rightMotor = getMotorFromUserName(rightUserPort);
            if ( leftMotor == null || rightMotor == null ) {
                addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else {
                usedHardwareBuilder.addUsedActor(new UsedActor("DIFFERENTIALDRIVE", SC.DIFFERENTIALDRIVE));
            }
        }
        return null;
    }

    private boolean checkActorPort(WithUserDefinedPort action) {
        Assert.isTrue(action instanceof Phrase, "checking Port of a non Phrase");
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = (Phrase) action;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
            return false;
        }
        return true;
    }
}
