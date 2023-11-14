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
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
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


/**
 * Abstract ValidatorCollectorVisitor, keeps shared methods for Lego and Pybricks
 * when extending from this class, make sure to execute super.[extended-method] since all basic
 * configuration checks are located in this class
 */
public abstract class AbstractSpikeValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements ISpikeVisitor<Void> {

    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
        put("COLOUR_SENSING", SC.COLOUR);
        put("KEYS_SENSING", SC.KEY);
    }});

    protected AbstractSpikeValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, "DEFAULT"));
        return null;
    }

    @Override
    final public Void visitDisplayClearAction(DisplayClearAction displayClearAction) {
        checkActorPort(displayClearAction);
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
        return null;
    }

    @Override
    final public Void visitImage(Image image) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPort(keysSensor);
        return null;
    }

    @Override
    final public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        requiredComponentVisited(rgbLedOnHiddenAction, rgbLedOnHiddenAction.colour);
        return null;
    }

    @Override
    public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        requiredComponentVisited(motorDiffCurveAction, motorDiffCurveAction.powerLeft, motorDiffCurveAction.powerRight);
        checkActorPort(motorDiffCurveAction);
        checkDiffDrive(motorDiffCurveAction);
        return null;
    }

    @Override
    public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        requiredComponentVisited(motorDiffCurveForAction, motorDiffCurveForAction.powerLeft, motorDiffCurveForAction.powerRight, motorDiffCurveForAction.distance);
        checkActorPort(motorDiffCurveForAction);
        checkDiffDrive(motorDiffCurveForAction);
        return null;
    }

    @Override
    public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        requiredComponentVisited(motorDiffOnAction, motorDiffOnAction.power);
        checkActorPort(motorDiffOnAction);
        checkDiffDrive(motorDiffOnAction);
        return null;
    }

    @Override
    public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        requiredComponentVisited(motorDiffOnForAction, motorDiffOnForAction.distance, motorDiffOnForAction.power);
        checkActorPort(motorDiffOnForAction);
        checkDiffDrive(motorDiffOnForAction);
        return null;
    }

    @Override
    final public Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction) {
        checkActorPort(motorDiffStopAction);
        checkDiffDrive(motorDiffStopAction);
        return null;
    }

    @Override
    public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        requiredComponentVisited(motorDiffTurnAction, motorDiffTurnAction.power);
        checkActorPort(motorDiffTurnAction);
        checkDiffDrive(motorDiffTurnAction);
        return null;
    }

    @Override
    public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        requiredComponentVisited(motorDiffTurnForAction, motorDiffTurnForAction.degrees, motorDiffTurnForAction.power);
        checkActorPort(motorDiffTurnForAction);
        checkDiffDrive(motorDiffTurnForAction);
        return null;
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
        return null;
    }

    @Override
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        requiredComponentVisited(playToneAction, playToneAction.frequency, playToneAction.duration);
        checkActorPort(playToneAction);
        return null;
    }

    @Override
    final public Void visitPredefinedImage(PredefinedImage predefinedImage) {
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
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    final public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        usedHardwareBuilder.addUsedActor(new UsedActor(null, C.RANDOM_DOUBLE));
        return null;
    }

    @Override
    final public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
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

    protected ConfigurationComponent getMotorFromUserName(String userName) {
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

    protected void checkDiffDrive(Phrase phrase) {
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
    }

    protected boolean checkActorPort(WithUserDefinedPort action) {
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