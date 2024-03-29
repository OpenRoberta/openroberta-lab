package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
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
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;

public class CalliopeValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements ICalliopeVisitor<Void> {
    public static final double DOUBLE_EPS = 1E-7;

    private final boolean isSim;
    private final boolean hasBlueTooth;

    public CalliopeValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean hasBlueTooth) //
    {
        super(brickConfiguration, beanBuilders);
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
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        requiredComponentVisited(displaySetBrightnessAction, displaySetBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
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
        return addActorMaybeCallibot(infraredSensor);
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        addToPhraseIfUnsupportedInSim(ledBarSetAction, false, isSim);
        checkActorByTypeExists(ledBarSetAction, "LEDBAR");
        requiredComponentVisited(ledBarSetAction, ledBarSetAction.x, ledBarSetAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.LED_BAR));
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
        return addActorMaybeCallibot(motorStopAction);
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
        return addActorMaybeCallibot(servoSetAction);
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
        return addActorMaybeCallibot(ultrasonicSensor);
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

}
