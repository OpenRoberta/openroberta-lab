package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.IMbedVisitor;

public class MbedValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IMbedVisitor<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    public MbedValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        checkSensorExists(accelerometerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        checkSensorExists(compassSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        requiredComponentVisited(displayGetPixelAction, displayGetPixelAction.x, displayGetPixelAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        requiredComponentVisited(displayImageAction, displayImageAction.valuesToDisplay);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }


    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        requiredComponentVisited(displaySetPixelAction, displaySetPixelAction.brightness, displaySetPixelAction.x, displaySetPixelAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        requiredComponentVisited(displayTextAction, displayTextAction.msg);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        if ( gestureSensor.getMode().equals("SHAKE") ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.IS_GESTURE_SHAKE);
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gestureSensor.getUserDefinedPort(), SC.ACCELEROMETER, gestureSensor.getMode()));
        return null;
    }


    @Override
    public Void visitImage(Image image) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        requiredComponentVisited(imageInvertFunction, imageInvertFunction.image);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        requiredComponentVisited(imageShiftFunction, imageShiftFunction.image, imageShiftFunction.positions);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }


    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorExists(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }


    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        checkSensorExists(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }


    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        checkSensorExists(pinValueSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(pinValueSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }


    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(pinTouchSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }


    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        ConfigurationComponent configurationComponent = checkActorByPortExists(mbedPinWriteValueAction, mbedPinWriteValueAction.port);
        requiredComponentVisited(mbedPinWriteValueAction, mbedPinWriteValueAction.value);
        if ( configurationComponent != null ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(configurationComponent.userDefinedPortName, configurationComponent.componentType));
        }
        return null;
    }


    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        checkActorByTypeExists(playNoteAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }


    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        requiredComponentVisited(radioSendAction, radioSendAction.message);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        requiredComponentVisited(radioSetChannelAction, radioSetChannelAction.channel);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }


    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        checkSensorExists(temperatureSensor);
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
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        checkActorByTypeExists(toneAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        if ( toneAction.duration.getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst) toneAction.duration).value);
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }


    protected ConfigurationComponent checkActorByPortExists(Phrase actor, String port) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponent(port);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkActorByTypeExists(Phrase actor, String type) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponentByType(type);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkSensorExists(ExternalSensor sensor) {
        ConfigurationComponent usedSensor = robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.componentType;
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( !type.equals("COLOUR") ) { // Mbed has COLOUR instead of COLOR
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !(type.equals("ULTRASONIC") || type.equals("CALLIBOT")) ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( !(type.equals("INFRARED") || type.equals("CALLIBOT")) ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !type.equals("GYRO") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "SOUND_SENSING":
                    if ( !type.equals("SOUND") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( !type.equals("LIGHT") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( !type.equals("COMPASS") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                default:
                    break;
            }
        }
        return usedSensor;
    }


    protected ConfigurationComponent checkMotorPort(MoveAction action) {
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_MOTOR_MISSING");
        }
        return configurationComponent;
    }

    protected void checkForZeroSpeed(Phrase action, Expr speed) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst speedNumConst = (NumConst) speed;
            if ( Math.abs(Double.parseDouble(speedNumConst.value)) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    protected Void addActorMaybeCallibot(WithUserDefinedPort phrase) {
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = checkActorByPortExists((Phrase) phrase, userDefinedPort);
        if ( configurationComponent != null ) {
            return addActorMaybeCallibot(phrase, configurationComponent.componentType);
        } else {
            return null; // checkActorByPortExists added the error message
        }
    }

    protected Void addActorMaybeCallibot(WithUserDefinedPort phrase, String componentType) {
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = checkActorByPortExists((Phrase) phrase, userDefinedPort);
        if ( configurationComponent != null ) {
            if ( configurationComponent.componentType.equals(SC.CALLIBOT) ) {
                usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
            } else {
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedPort, componentType));
            }
        }
        return null;
    }

    protected Boolean isMotionKitPinsOverlapping() {
        Map<String, ConfigurationComponent> usedConfig = robotConfiguration.getConfigurationComponents();
        if ( robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT) != null || robotConfiguration.isComponentTypePresent("LEDBAR") || robotConfiguration.isComponentTypePresent("FOURDIGITDISPLAY") || robotConfiguration.isComponentTypePresent(SC.ULTRASONIC) || robotConfiguration.isComponentTypePresent(SC.HUMIDITY) || robotConfiguration.isComponentTypePresent(SC.COLOUR) ) {
            return true;
        }
        for ( Map.Entry<String, ConfigurationComponent> confComp : usedConfig.entrySet() ) {
            String confType = confComp.getValue().componentType;
            if ( confType.equals(SC.SERVOMOTOR) || confType.equals(SC.DIGITAL_INPUT) || confType.equals(SC.ANALOG_INPUT) ) {
                String pin1 = confComp.getValue().getProperty("PIN1");
                if ( pin1.equals("1") || pin1.equals("2") || pin1.equals("4") || pin1.equals("5") || pin1.equals("C16") || pin1.equals("C17") ) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Void checkDifferentialDrive() {
        int countMotors = (int) this.robotConfiguration.getConfigurationComponentsValues().stream().filter(comp -> comp.componentType.equals("MOTOR")).count();
        if ( countMotors == 2 ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DIFFERENTIALDRIVE));
        }
        return null;
    }
}
