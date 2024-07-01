package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.CallibotKeysSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;

public class CalliopeCommonValidatorAndCollectorVisitor extends MbedV2ValidatorAndCollectorVisitor implements ICalliopeVisitor<Void> {
    private final boolean hasBlueTooth;
    protected final boolean isSim;

    public CalliopeCommonValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed,
        boolean hasBlueTooth) //
    {
        super(brickConfiguration, beanBuilders, displaySwitchUsed);
        this.isSim = isSim;
        this.hasBlueTooth = hasBlueTooth;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        addToPhraseIfUnsupportedInSim(colorSensor, true, isSim);
        checkSensorExists(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRgbLedsOnHiddenAction(RgbLedsOnHiddenAction rgbLedsOnHiddenAction) {
        requiredComponentVisited(rgbLedsOnHiddenAction, rgbLedsOnHiddenAction.colour);
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedsOnHiddenAction.hide.getValue(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitRgbLedsOffHiddenAction(RgbLedsOffHiddenAction rgbLedsOffHiddenAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedsOffHiddenAction.hide.getValue(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitCallibotKeysSensor(CallibotKeysSensor callibotKeysSensor) {
        addToPhraseIfUnsupportedInSim(callibotKeysSensor, true, isSim);
        addActorMaybeCallibot(callibotKeysSensor);
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor moistureSensor) {
        addToPhraseIfUnsupportedInSim(moistureSensor, true, isSim);
        checkSensorExists(moistureSensor);
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        requiredComponentVisited(displaySetBrightnessAction, displaySetBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_MATRIX_BRIGHTNESS);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        addToPhraseIfUnsupportedInSim(gyroSensor, true, isSim);
        checkSensorExists(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.ACCELEROMETER, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        addToPhraseIfUnsupportedInSim(humiditySensor, true, isSim);
        checkSensorExists(humiditySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(humiditySensor.getUserDefinedPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        addToPhraseIfUnsupportedInSim(infraredSensor, true, isSim);
        addActorMaybeCallibot(infraredSensor);
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        addToPhraseIfUnsupportedInSim(ledBarSetAction, false, isSim);
        checkActorByTypeExists(ledBarSetAction, "LEDBAR");
        requiredComponentVisited(ledBarSetAction, ledBarSetAction.x, ledBarSetAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.LED_BAR));
        usedMethodBuilder.addUsedMethod(CalliopeMethods.LED_BAR_SET_LED);
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        addToPhraseIfUnsupportedInSim(rgbLedOnAction, false, isSim);
        checkActorByTypeExists(rgbLedOnAction, SC.CALLIBOT);
        requiredComponentVisited(rgbLedOnAction, rgbLedOnAction.colour);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        addToPhraseIfUnsupportedInSim(rgbLedOffAction, false, isSim);
        checkActorByTypeExists(rgbLedOffAction, SC.CALLIBOT);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        requiredComponentVisited(rgbLedOnHiddenAction, rgbLedOnHiddenAction.colour);
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedOnHiddenAction.hide.getValue(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedOffHiddenAction.hide.getValue(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        addToPhraseIfUnsupportedInSim(ledAction, false, isSim);
        checkActorByTypeExists(ledAction, SC.CALLIBOT);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        if ( hasBlueTooth ) {
            addErrorToPhrase(radioSendAction, "BLOCK_NOT_SUPPORTED");
        } else {
            addToPhraseIfUnsupportedInSim(radioSendAction, false, isSim);
        }
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        if ( hasBlueTooth ) {
            addErrorToPhrase(radioReceiveAction, "BLOCK_NOT_SUPPORTED");
        } else {
            addToPhraseIfUnsupportedInSim(radioReceiveAction, true, isSim);
        }
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        if ( hasBlueTooth ) {
            addErrorToPhrase(radioSetChannelAction, "BLOCK_NOT_SUPPORTED");
        } else {
            addToPhraseIfUnsupportedInSim(radioSetChannelAction, false, isSim);
        }
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        if ( hasBlueTooth ) {
            addErrorToPhrase(radioRssiSensor, "BLOCK_NOT_SUPPORTED");
        } else {
            addToPhraseIfUnsupportedInSim(radioRssiSensor, true, isSim);
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        if ( isSim && (pinValueSensor.getMode().equals(SC.PULSEHIGH) || pinValueSensor.getMode().equals(SC.PULSELOW) || pinValueSensor.getMode().equals(SC.PULSE)) ) {
            addErrorToPhrase(pinValueSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return super.visitPinGetValueSensor(pinValueSensor);
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        addToPhraseIfUnsupportedInSim(motionKitDualSetAction, false, isSim);
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitDualSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor("", "MOTIONKIT"));
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SERVO_GET_ANGLE);
        }
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        addToPhraseIfUnsupportedInSim(motionKitSingleSetAction, false, isSim);
        if ( isMotionKitPinsOverlapping() ) {
            addErrorToPhrase(motionKitSingleSetAction, "MOTIONKIT_PIN_OVERLAP_WARNING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motionKitSingleSetAction.port, "MOTIONKIT"));
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SERVO_GET_ANGLE);
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        MotorDuration duration = motorOnAction.param.getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(motorOnAction, motorOnAction.param.getSpeed());
            requiredComponentVisited(motorOnAction, duration.getValue());
        }
        boolean callibot = addActorMaybeCallibot(motorOnAction);
        if ( !callibot ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_MOTOR);
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        boolean callibot = addActorMaybeCallibot(motorStopAction);
        if ( !callibot ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_MOTOR);
        }
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addToPhraseIfUnsupportedInSim(accelerometerSensor, true, isSim);
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        addToPhraseIfUnsupportedInSim(servoSetAction, false, isSim);
        requiredComponentVisited(servoSetAction, servoSetAction.value);
        if ( !addActorMaybeCallibot(servoSetAction) ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.SERVOMOTOR));
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SERVO_GET_ANGLE);
        }
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
        addToPhraseIfUnsupportedInSim(switchLedMatrixAction, false, isSim);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        addToPhraseIfUnsupportedInSim(ultrasonicSensor, true, isSim);
        checkSensorExists(ultrasonicSensor);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( confCompCallibot == null ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.ULTRASONIC_GET_DISTANCE);
        }
        addActorMaybeCallibot(ultrasonicSensor);
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        ConfigurationComponent usedActorA = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.portA);
        ConfigurationComponent usedActorB = robotConfiguration.optConfigurationComponent(bothMotorsOnAction.portB);
        ConfigurationComponent usedActorCallibot = robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        boolean allActorsPresent = (usedActorA != null) && (usedActorB != null);
        if ( usedActorCallibot != null && usedActorCallibot.componentType.equals("CALLIBOT") ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        } else if ( bothMotorsOnAction.portA.equals(bothMotorsOnAction.portB) || !usedActorA.componentType.equals(usedActorB.componentType) ) {
            addErrorToPhrase(bothMotorsOnAction, "BLOCK_NOT_EXECUTED");
        } else if ( !allActorsPresent ) {
            addErrorToPhrase(bothMotorsOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        boolean differentialDrive = checkDifferentialDrive();
        if ( differentialDrive && !(usedActorCallibot != null && usedActorCallibot.componentType.equals("CALLIBOT")) ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_BOTH_MOTORS);
        }
        requiredComponentVisited(bothMotorsOnAction, bothMotorsOnAction.speedA, bothMotorsOnAction.speedB);
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        if ( robotConfiguration.isComponentTypePresent(SC.CALLIBOT) ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
        } else {
            int num = robotConfiguration.getAllConfigurationComponentByType("MOTOR").size();
            if ( num == 0 ) {
                addErrorToPhrase(bothMotorsStopAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else if ( num == 1 ) {
                usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_MOTOR);
            } else if ( num > 1 ) {
                usedMethodBuilder.addUsedMethod(CalliopeMethods.SET_BOTH_MOTORS);
            }
        }
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        addToPhraseIfUnsupportedInSim(fourDigitDisplayClearAction, false, isSim);
        checkActorByTypeExists(fourDigitDisplayClearAction, "FOURDIGITDISPLAY");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.FOUR_DIGIT_DISPLAY));
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        addToPhraseIfUnsupportedInSim(fourDigitDisplayShowAction, false, isSim);
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
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        ConfigurationComponent configurationComponent = checkActorByPortExists(mbedPinWriteValueAction, mbedPinWriteValueAction.port);
        requiredComponentVisited(mbedPinWriteValueAction, mbedPinWriteValueAction.value);
        if ( configurationComponent != null ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(configurationComponent.userDefinedPortName, configurationComponent.componentType));
        }
        return null;
    }


    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addToPhraseIfUnsupportedInSim(playFileAction, false, this.isSim);
        return super.visitPlayFileAction(playFileAction);
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        addToPhraseIfUnsupportedInSim(soundToggleAction, false, this.isSim);
        return super.visitSoundToggleAction(soundToggleAction);
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        checkSensorExists(logoTouchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(logoTouchSensor.getUserDefinedPort(), "LOGOTOUCH", logoTouchSensor.getSlot()));
        return null;
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(logoSetTouchMode.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = logoSetTouchMode;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor("LOGOTOUCH", "LOGOTOUCH", logoSetTouchMode.mode));
        return null;
    }


    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pinSetTouchMode.sensorport, "PIN", pinSetTouchMode.mode));
        return null;
    }


    protected void checkForZeroSpeed(Phrase action, Expr speed) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst speedNumConst = (NumConst) speed;
            if ( Math.abs(Double.parseDouble(speedNumConst.value)) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    protected boolean addActorMaybeCallibot(WithUserDefinedPort phrase) {
        boolean callibot = false;
        final String userDefinedPort = phrase.getUserDefinedPort();
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( configurationComponent != null ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.CALLIBOT));
            callibot = true;
        } else {
            configurationComponent = checkActorByPortExists((Phrase) phrase, userDefinedPort);
            if ( configurationComponent != null ) {
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedPort, configurationComponent.componentType));
            }
        }
        return callibot;
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

    protected Boolean checkDifferentialDrive() {
        boolean differentialDrive = false;
        int countMotors = (int) this.robotConfiguration.getConfigurationComponentsValues().stream().filter(comp -> comp.componentType.equals("MOTOR")).count();
        if ( countMotors == 2 ) {
            usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DIFFERENTIALDRIVE));
            differentialDrive = true;
        }
        return differentialDrive;
    }
}
