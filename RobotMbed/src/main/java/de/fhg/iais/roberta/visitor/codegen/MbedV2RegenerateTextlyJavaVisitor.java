package de.fhg.iais.roberta.visitor.codegen;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.textlyJava.Microbitv2TextlyJavaVisitor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */

public final class MbedV2RegenerateTextlyJavaVisitor extends AbstractRegenerateTextlyJavaVisitor implements IMicrobitV2Visitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(MbedV2RegenerateTextlyJavaVisitor.class);

    protected final ConfigurationAst brickConfiguration;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programPhrases the program
     * @param brickConfiguration hardware configuration of the brick
     */
    public MbedV2RegenerateTextlyJavaVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst brickConfiguration) {
        super(programPhrases);
        this.brickConfiguration = brickConfiguration;
    }


    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.nlI().add("microbitv2.clearDisplay();");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        if ( displayTextAction.mode.equals("TEXT") ) {
            this.src.nlI().add("microbitv2.showText(");
        } else {
            this.src.nlI().add("microbitv2.showCharacter(");
        }
        displayTextAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        String predefinedImageName = predefinedImage.imageName;
        String textlyRepresentation = null;
        for ( Microbitv2TextlyJavaVisitor.PredefinedImageEnum imageEnum : Microbitv2TextlyJavaVisitor.PredefinedImageEnum.values() ) {
            if ( imageEnum.name().equalsIgnoreCase(predefinedImageName) ) {
                textlyRepresentation = imageEnum.imageString;
            }
        }
        this.src.add("image(").add(textlyRepresentation).add(")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        if ( displayImageAction.displayImageMode.equals("IMAGE") ) {
            this.src.nlI().add("microbitv2.showImage(");
        } else {
            this.src.nlI().add("microbitv2.showAnimation(");
        }
        displayImageAction.valuesToDisplay.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        this.src.add("image.shift(").add(imageShiftFunction.shiftDirection.toString().toLowerCase());
        this.src.add(",");
        imageShiftFunction.image.accept(this);
        this.src.add(",");
        imageShiftFunction.positions.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        this.src.add("image.invert(");
        imageInvertFunction.image.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        StringBuilder sb = new StringBuilder();
        sb.append("image.define(");
        for ( int i = 0; i < image.image.length; i++ ) {
            sb.append("[");
            for ( int j = 0; j < image.image[i].length; j++ ) {
                if ( image.image[i][j].equals("#") ) {
                    sb.append("#");
                } else {
                    sb.append("0");
                }
                if ( j < image.image[i].length - 1 ) {
                    sb.append(",");
                }
            }
            sb.append("]");
        }
        sb.append(")");
        this.src.add(sb.toString());
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        this.src.nlI().add("microbitv2.radioSend(").add(radioSendAction.type).add(",").add(radioSendAction.power).add(",");
        radioSendAction.message.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        this.src.add("microbitv2.receiveMessage(").add(radioReceiveAction.type).add(")");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        this.src.nlI().add("microbitv2.setLed(");
        displaySetPixelAction.brightness.accept(this);
        this.src.add(",");
        displaySetPixelAction.x.accept(this);
        this.src.add(",");
        displaySetPixelAction.y.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        this.src.add("microbitv2.getLedBrigthness(");
        displayGetPixelAction.x.accept(this);
        this.src.add(",");
        displayGetPixelAction.y.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        this.src.nlI().add("microbitv2.radioSet(");
        radioSetChannelAction.channel.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        this.src.nlI().add("microbitv2.writeValuePin(").add(mbedPinWriteValueAction.pinValue.toLowerCase()).add(",").add(mbedPinWriteValueAction.port).add(",");
        mbedPinWriteValueAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("microbitv2.keysSensor.isPressed(").add(keysSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("microbitv2.temperatureSensor()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.src.add("microbitv2.accelerometerSensor(").add(accelerometerSensor.getSlot().toLowerCase()).add(")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("microbitv2.lightSensor.getLevel()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        this.src.add("microbitv2.gestureSensor.currentGesture(");
        String textlyGesture = null;
        for ( Microbitv2TextlyJavaVisitor.GestureModes gestureMode : Microbitv2TextlyJavaVisitor.GestureModes.values() ) {
            if ( gestureMode.name().equalsIgnoreCase(gestureSensor.getMode()) ) {
                textlyGesture = gestureMode.textlyName;
                break;
            }
        }
        this.src.add(textlyGesture).add(")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        this.src.add("microbitv2.compassSensor.getAngle()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("microbitv2.timerSensor()");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.nlI().add("microbitv2.timerReset();");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        this.src.add("microbitv2.pinTouchSensor.isPressed(").add(pinTouchSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        this.src.add("microbitv2.pinGetValueSensor(").add(pinValueSensor.getUserDefinedPort(), ",", pinModes().get(pinValueSensor.getMode())).add(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.nlI().add("microbitv2.pitch(");
        toneAction.frequency.accept(this);
        this.src.add(",");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.nlI().add("microbitv2.pitch(").add(playNoteAction.frequency).add(",").add(playNoteAction.duration).add(");");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        if ( playFileAction.getProperty().blockType.equals("actions_play_expression") ) {
            this.src.nlI().add("microbitv2.playSound(").add(playFileAction.fileName.toLowerCase()).add(");");
        } else {
            this.src.nlI().add("microbitv2.playFile(").add(playFileAction.fileName.toLowerCase()).add(");");
        }

        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        this.src.nlI().add("microbitv2.switchLed(").add(switchLedMatrixAction.activated.toLowerCase()).add(");");
        return null;
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        this.src.nlI().add("microbitv2.speaker(").add(soundToggleAction.mode.toLowerCase()).add(");");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.nlI().add("microbitv2.setVolume(");
        setVolumeAction.volume.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("microbitv2.soundSensor.microphone.soundLevel()");
        return null;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        this.src.add("microbitv2.logoTouchSensor.isPressed()");
        return null;
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        return null;
    }

    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        this.src.nlI().add("microbitv2.pinSetTouchMode(").add(pinSetTouchMode.sensorport).add(" , ").add(pinSetTouchMode.mode.toLowerCase()).add(");");
        return null;
    }

    private String getOldModeClass(String sensorType, String sensorMode) {
        switch ( sensorType ) {
            case SC.TOUCH:
                if ( sensorMode.equals("PRESSED") || sensorMode.equals("DEFAULT") ) {
                    return "TouchSensorMode.TOUCH";
                } else {
                    return "TouchSensorMode." + sensorMode;
                }

            case SC.COLOR:
            case SC.COLOUR:
                if ( sensorMode.equals(SC.LIGHT) ) {
                    sensorMode = SC.RED;
                }
                return "ColorSensorMode." + sensorMode;
            case SC.ULTRASONIC:
                return "UltrasonicSensorMode." + sensorMode;
            case SC.INFRARED:
                return "InfraredSensorMode." + sensorMode;
            case SC.GYRO:
                return "GyroSensorMode." + sensorMode;
            case SC.COMPASS:
                return "CompassSensorMode." + sensorMode;
            case SC.IRSEEKER:
                return "IRSeekerSensorMode." + sensorMode;
            case SC.SOUND:
                return "SoundSensorMode." + sensorMode;
            case SC.HT_COLOR:
                return "HiTecColorSensorV2Mode." + sensorMode;
            default:
                throw new DbcException("There is mapping missing for " + sensorType + " with the old enums!");
        }
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color = "";
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#000000":
                color = "BLACK";
                break;
            case "#0057A6":
                color = "BLUE";
                break;
            case "#00642E":
                color = "GREEN";
                break;
            case "#F7D117":
                color = "YELLOW";
                break;
            case "#B30006":
                color = "RED";
                break;
            case "#FFFFFF":
                color = "WHITE";
                break;
            case "#532115":
                color = "BROWN";
                break;
            case "#585858":
                color = "NONE";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        this.src.add("PickColor.", color);
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        this.src.nlI().add("microbitv2.showOnSerial(");
        serialWriteAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    protected static Map<String, String> pinModes() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(
                        entry("ANALOG", "analog"),
                        entry("DIGITAL", "digital"),
                        entry("PULSEHIGH", "pulseHigh"),
                        entry("PULSELOW", "pulseLow")
                    )
                    .collect(entriesToMap()));
    }

    protected String getPinModes(String mode) {
        return pinModes().get(mode);
    }
}
