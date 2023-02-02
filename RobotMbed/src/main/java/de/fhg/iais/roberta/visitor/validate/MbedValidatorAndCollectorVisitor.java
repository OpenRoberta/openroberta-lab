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
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.IMbedVisitorWithoutDefault;

public class MbedValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IMbedVisitorWithoutDefault<Void> {

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
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        ConfigurationComponent usedActorA = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.portA);
        ConfigurationComponent usedActorB = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.portB);
        boolean allActorsPresent = (usedActorA != null) && (usedActorB != null);
        if ( !allActorsPresent ) {
            addErrorToPhrase(bothMotorsOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else if ( bothMotorsOnAction.portA.equals(bothMotorsOnAction.portB) || !usedActorA.componentType.equals(usedActorB.componentType) ) {
            addErrorToPhrase(bothMotorsOnAction, "BLOCK_NOT_EXECUTED");
        } else if ( usedActorA.componentType.equals("CALLIBOT") ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        checkDifferentialDrive();
        requiredComponentVisited(bothMotorsOnAction, bothMotorsOnAction.speedA, bothMotorsOnAction.speedB);
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        if ( robotConfiguration.isComponentTypePresent(SC.CALLIBOT) ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        } else if ( !robotConfiguration.isComponentTypePresent("MOTOR") ) {
            addErrorToPhrase(bothMotorsStopAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        checkSensorExists(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        checkSensorExists(compassSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
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
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        requiredComponentVisited(displaySetBrightnessAction, displaySetBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
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
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        checkActorByTypeExists(fourDigitDisplayClearAction, "FOURDIGITDISPLAY");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        checkActorByTypeExists(fourDigitDisplayShowAction, "FOURDIGITDISPLAY");
        requiredComponentVisited(
            fourDigitDisplayShowAction,
            fourDigitDisplayShowAction.value,
            fourDigitDisplayShowAction.position,
            fourDigitDisplayShowAction.colon);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
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
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        requiredComponentVisited(sensorGetSample, sensorGetSample.sensor);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        checkSensorExists(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.ACCELEROMETER, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        checkSensorExists(humiditySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(humiditySensor.getUserDefinedPort(), SC.HUMIDITY, humiditySensor.getMode()));
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
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        return addActorMaybeCallibot(infraredSensor);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorExists(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        checkActorByTypeExists(ledBarSetAction, "LEDBAR");
        requiredComponentVisited(ledBarSetAction, ledBarSetAction.x, ledBarSetAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.LED_BAR));
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        requiredComponentVisited(ledOnAction, ledOnAction.ledColor);
        return addActorMaybeCallibot(ledOnAction, SC.RGBLED);
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        // TODO: design better blockly blocks and don't reuse blocks with different number of parameters and don't use EmptyExpr
        String blocktype = lightAction.getProperty().getBlockType();
        checkActorByPortExists(lightAction, lightAction.port);
        if ( !blocktype.equals("robActions_brickLight_on") ) {
            requiredComponentVisited(lightAction, lightAction.rgbLedColor);
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        checkSensorExists(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        return addActorMaybeCallibot(lightStatusAction, SC.LIGHT);
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitDualSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor("", "MOTIONKIT"));
        }
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitSingleSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motionKitSingleSetAction.port, "MOTIONKIT"));
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("This block is not implemented.");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        MotorDuration duration = motorOnAction.param.getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(motorOnAction, motorOnAction.param.getSpeed());
            requiredComponentVisited(motorOnAction, duration.getValue());
        }
        checkDifferentialDrive();
        return addActorMaybeCallibot(motorOnAction);
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("This block is not implemented");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        checkMotorPort(motorStopAction);
        return addActorMaybeCallibot(motorStopAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        checkSensorExists(pinValueSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(pinValueSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction pinSetPullAction) {
        addErrorToPhrase(pinSetPullAction, "");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(pinTouchSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        throw new DbcException("Mbed devices should use MbedPinWriteValueAction!");
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
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
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
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        requiredComponentVisited(servoSetAction, servoSetAction.value);
        return addActorMaybeCallibot(servoSetAction, SC.SERVOMOTOR);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction singleMotorOnAction) {
        requiredComponentVisited(singleMotorOnAction, singleMotorOnAction.speed);
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction singleMotorStopAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        checkSensorExists(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
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

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorExists(ultrasonicSensor);
        return addActorMaybeCallibot(ultrasonicSensor, SC.ULTRASONIC);
    }

    private ConfigurationComponent checkActorByPortExists(Phrase actor, String port) {
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

    private void checkForZeroSpeed(Phrase action, Expr speed) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst speedNumConst = (NumConst) speed;
            if ( Math.abs(Double.parseDouble(speedNumConst.value)) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private Void addActorMaybeCallibot(WithUserDefinedPort phrase) {
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = checkActorByPortExists((Phrase) phrase, userDefinedPort);
        if ( configurationComponent != null ) {
            return addActorMaybeCallibot(phrase, configurationComponent.componentType);
        } else {
            return null; // checkActorByPortExists added the error message
        }
    }

    private Void addActorMaybeCallibot(WithUserDefinedPort phrase, String componentType) {
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

    private Boolean isMotionKitPinsOverlapping() {
        Map<String, ConfigurationComponent> usedConfig = robotConfiguration.getConfigurationComponents();
        if ( robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT) != null || robotConfiguration.isComponentTypePresent("LEDBAR") ||
            robotConfiguration.isComponentTypePresent("FOURDIGITDISPLAY") ||
            robotConfiguration.isComponentTypePresent(SC.ULTRASONIC) ||
            robotConfiguration.isComponentTypePresent(SC.HUMIDITY) ||
            robotConfiguration.isComponentTypePresent(SC.COLOUR) ) {
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

    private Void checkDifferentialDrive() {
        int countMotors = (int) this.robotConfiguration.getConfigurationComponentsValues()
            .stream()
            .filter(comp -> comp.componentType.equals("MOTOR"))
            .count();
        if ( countMotors == 2 ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DIFFERENTIALDRIVE));
        }
        return null;
    }
}
