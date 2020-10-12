package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.mode.action.mbed.DisplayImageMode;
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
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

public class MbedBoardValidatorVisitor extends AbstractBoardValidatorVisitor implements IMbedVisitor<Void> {

    public MbedBoardValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getPort());
        if ( usedSensor == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( !type.equals("COLOUR") ) { // Mbed has COLOUR instead of COLOR
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !(type.equals("ULTRASONIC") || type.equals("CALLIBOT")) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( !(type.equals("INFRARED") || type.equals("CALLIBOT")) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !type.equals("GYRO") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "SOUND_SENSING":
                    if ( !type.equals("SOUND") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( !type.equals("LIGHT") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( !type.equals("COMPASS") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        checkSensorPort(keysSensor);
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().accept(this);
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().accept(this);
        if ( (displayImageAction.getDisplayImageMode() == DisplayImageMode.ANIMATION) && (displayImageAction.getValuesToDisplay() instanceof EmptyExpr) ) {
            displayImageAction.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        imageShiftFunction.getPositions().accept(this);
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorPort(temperatureSensor);
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(ledOnAction.getPort());
        if ( usedActor == null ) {
            ledOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        ledOnAction.getLedColor().accept(this);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(lightStatusAction.getPort());
        if ( usedActor == null ) {
            lightStatusAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitLightStatusAction(lightStatusAction);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
        if ( usedActor == null ) {
            motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitMotorOnAction(motorOnAction);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("BUZZER");
        if ( usedActor == null ) {
            playNoteAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitPlayNoteAction(playNoteAction);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(lightAction.getPort());
        if ( usedActor == null ) {
            lightAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitLightAction(lightAction);
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("BUZZER");
        if ( usedActor == null ) {
            toneAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitToneAction(toneAction);
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(motorStopAction.getUserDefinedPort());
        if ( usedActor == null ) {
            motorStopAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return super.visitMotorStopAction(motorStopAction);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().accept(this);
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        rgbColor.getA().accept(this);
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        checkSensorPort(pinValueSensor);
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(pinWriteValueAction.getPort());
        if ( usedActor == null ) {
            pinWriteValueAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        pinWriteValueAction.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().accept(this);
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        displaySetPixelAction.getBrightness().accept(this);
        displaySetPixelAction.getX().accept(this);
        displaySetPixelAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        displayGetPixelAction.getX().accept(this);
        displayGetPixelAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        radioSetChannelAction.getChannel().accept(this);
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("FOURDIGITDISPLAY");
        if ( usedActor == null ) {
            fourDigitDisplayShowAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        fourDigitDisplayShowAction.getValue().accept(this);
        fourDigitDisplayShowAction.getPosition().accept(this);
        fourDigitDisplayShowAction.getColon().accept(this);
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("FOURDIGITDISPLAY");
        if ( usedActor == null ) {
            fourDigitDisplayClearAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponentByType("LEDBAR");
        if ( usedActor == null ) {
            ledBarSetAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        ledBarSetAction.getX().accept(this);
        ledBarSetAction.getBrightness().accept(this);
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPull) {
        ConfigurationComponent usedActor = this.robotConfiguration.optConfigurationComponent(pinSetPull.getPort());
        if ( usedActor == null ) {
            pinSetPull.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        ConfigurationComponent usedActorA = this.robotConfiguration.optConfigurationComponent(bothMotorsOnAction.getPortA());
        ConfigurationComponent usedActorB = this.robotConfiguration.optConfigurationComponent(bothMotorsOnAction.getPortB());
        if ( (usedActorA == null) || (usedActorB == null) ) {
            bothMotorsOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        } else if ( (!usedActorA.getComponentType().equals("CALLIBOT")) && usedActorA.equals(usedActorB) ) {
            bothMotorsOnAction.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        checkSensorPort(humiditySensor);
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(servoSetAction.getPort());
        if ( usedSensor == null ) {
            servoSetAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        return null;
    }
}
