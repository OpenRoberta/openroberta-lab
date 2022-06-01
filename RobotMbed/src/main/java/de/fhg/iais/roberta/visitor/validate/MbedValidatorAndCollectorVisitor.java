package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.IMbedVisitorWithoutDefault;

public class MbedValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IMbedVisitorWithoutDefault<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    public MbedValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        checkSensorExists(accelerometerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        ConfigurationComponent usedActorA = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.getPortA());
        ConfigurationComponent usedActorB = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.getPortB());
        boolean allActorsPresent = (usedActorA != null) && (usedActorB != null);
        if ( !allActorsPresent ) {
            addErrorToPhrase(bothMotorsOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else if ( bothMotorsOnAction.getPortA().equals(bothMotorsOnAction.getPortB()) || !usedActorA.getComponentType().equals(usedActorB.getComponentType()) ) {
            addErrorToPhrase(bothMotorsOnAction, "BLOCK_NOT_EXECUTED");
        } else if ( usedActorA.getComponentType().equals("CALLIBOT") ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        }
        requiredComponentVisited(bothMotorsOnAction, bothMotorsOnAction.getSpeedA(), bothMotorsOnAction.getSpeedB());
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        if ( robotConfiguration.isComponentTypePresent(SC.CALLIBOT) ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        } else if ( !robotConfiguration.isComponentTypePresent("MOTOR") ) {
            addErrorToPhrase(bothMotorsStopAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorExists(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        checkSensorExists(compassSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        requiredComponentVisited(displayGetPixelAction, displayGetPixelAction.getX(), displayGetPixelAction.getY());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        requiredComponentVisited(displayImageAction, displayImageAction.getValuesToDisplay());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        requiredComponentVisited(displaySetBrightnessAction, displaySetBrightnessAction.getBrightness());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        requiredComponentVisited(displaySetPixelAction, displaySetPixelAction.getBrightness(), displaySetPixelAction.getX(), displaySetPixelAction.getY());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        requiredComponentVisited(displayTextAction, displayTextAction.getMsg());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        checkActorByTypeExists(fourDigitDisplayClearAction, "FOURDIGITDISPLAY");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        checkActorByTypeExists(fourDigitDisplayShowAction, "FOURDIGITDISPLAY");
        requiredComponentVisited(
            fourDigitDisplayShowAction,
            fourDigitDisplayShowAction.getValue(),
            fourDigitDisplayShowAction.getPosition(),
            fourDigitDisplayShowAction.getColon());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        if ( gestureSensor.getMode().equals("SHAKE") ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.IS_GESTURE_SHAKE);
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gestureSensor.getUserDefinedPort(), SC.ACCELEROMETER, gestureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        requiredComponentVisited(sensorGetSample, sensorGetSample.getSensor());
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorExists(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.ACCELEROMETER, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        checkSensorExists(humiditySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(humiditySensor.getUserDefinedPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        requiredComponentVisited(imageInvertFunction, imageInvertFunction.getImage());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        requiredComponentVisited(imageShiftFunction, imageShiftFunction.getImage(), imageShiftFunction.getPositions());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return addActorMaybeCallibot(infraredSensor);
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        checkSensorExists(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        checkActorByTypeExists(ledBarSetAction, "LEDBAR");
        requiredComponentVisited(ledBarSetAction, ledBarSetAction.getX(), ledBarSetAction.getBrightness());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.LED_BAR));
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        requiredComponentVisited(ledOnAction, ledOnAction.getLedColor());
        return addActorMaybeCallibot(ledOnAction, SC.RGBLED);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        // TODO: design better blockly blocks and don't reuse blocks with different number of parameters and don't use EmptyExpr
        String blocktype = lightAction.getProperty().getBlockType();
        checkActorByPortExists(lightAction, lightAction.getPort());
        if ( !blocktype.equals("robActions_brickLight_on") ) {
            requiredComponentVisited(lightAction, lightAction.getRgbLedColor());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorExists(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return addActorMaybeCallibot(lightStatusAction, SC.LIGHT);
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitDualSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor("", "MOTIONKIT"));
        }
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitSingleSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motionKitSingleSetAction.getPort(), "MOTIONKIT"));
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        throw new DbcException("This block is not implemented.");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.getParam().getSpeed());
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(motorOnAction, motorOnAction.getParam().getSpeed());
            requiredComponentVisited(motorOnAction, duration.getValue());
        }
        return addActorMaybeCallibot(motorOnAction);
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        throw new DbcException("This block is not implemented");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        return addActorMaybeCallibot(motorStopAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        checkSensorExists(pinValueSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(pinValueSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        addErrorToPhrase(pinSetPullAction, "");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(pinTouchSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        ConfigurationComponent configurationComponent = checkActorByPortExists(pinWriteValueAction, pinWriteValueAction.getPort());
        requiredComponentVisited(pinWriteValueAction, pinWriteValueAction.getValue());
        if ( configurationComponent != null ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(configurationComponent.getUserDefinedPortName(), configurationComponent.getComponentType()));
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        checkActorByTypeExists(playNoteAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        requiredComponentVisited(radioSendAction, radioSendAction.getMsg());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        requiredComponentVisited(radioSetChannelAction, radioSetChannelAction.getChannel());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        requiredComponentVisited(servoSetAction, servoSetAction.getValue());
        return addActorMaybeCallibot(servoSetAction, SC.SERVOMOTOR);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        requiredComponentVisited(singleMotorOnAction, singleMotorOnAction.getSpeed());
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorExists(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorExists(temperatureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(temperatureSensor.getUserDefinedPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        checkActorByTypeExists(toneAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        if ( toneAction.getDuration().getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst<Void>) toneAction.getDuration()).getValue());
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorExists(ultrasonicSensor);
        return addActorMaybeCallibot(ultrasonicSensor, SC.ULTRASONIC);
    }

    private ConfigurationComponent checkActorByPortExists(Phrase<Void> actor, String port) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponent(port);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkActorByTypeExists(Phrase<Void> actor, String type) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponentByType(type);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkSensorExists(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.getComponentType();
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


    protected ConfigurationComponent checkMotorPort(MoveAction<Void> action) {
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_MOTOR_MISSING");
        }
        return configurationComponent;
    }

    private void checkForZeroSpeed(Phrase<Void> action, Expr<Void> speed) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst<Void> speedNumConst = (NumConst<Void>) speed;
            if ( Math.abs(Double.parseDouble(speedNumConst.getValue())) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private Void addActorMaybeCallibot(WithUserDefinedPort<Void> phrase) {
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = checkActorByPortExists((Phrase<Void>) phrase, userDefinedPort);
        if ( configurationComponent != null ) {
            return addActorMaybeCallibot(phrase, configurationComponent.getComponentType());
        } else {
            return null; // checkActorByPortExists added the error message
        }
    }

    private Void addActorMaybeCallibot(WithUserDefinedPort<Void> phrase, String componentType) {
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = checkActorByPortExists((Phrase<Void>) phrase, userDefinedPort);
        if ( configurationComponent != null ) {
            if ( configurationComponent.getComponentType().equals(SC.CALLIBOT) ) {
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
            String confType = confComp.getValue().getComponentType();
            if ( confType.equals(SC.SERVOMOTOR) || confType.equals(SC.DIGITAL_INPUT) || confType.equals(SC.ANALOG_INPUT) ) {
                String pin1 = confComp.getValue().getProperty("PIN1");
                if ( pin1.equals("1") || pin1.equals("2") || pin1.equals("4") || pin1.equals("5") || pin1.equals("C16") || pin1.equals("C17") ) {
                    return true;
                }
            }
        }
        return false;
    }
}
