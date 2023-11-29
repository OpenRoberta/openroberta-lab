package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;

public class MicrobitV2TypecheckVisitor extends TypecheckCommonLanguageVisitor implements IMicrobitV2Visitor<BlocklyType> {
    public MicrobitV2TypecheckVisitor(UsedHardwareBean usedHardwareBean) {
        super(usedHardwareBean);
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(clearDisplayAction, this);
    }

    @Override
    public BlocklyType visitDisplayTextAction(DisplayTextAction displayTextAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY).typeCheckPhrases(displayTextAction, this, displayTextAction.msg);
    }

    @Override
    public BlocklyType visitPlayFileAction(PlayFileAction playFileAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(playFileAction, this);
    }

    @Override
    public BlocklyType visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(switchLedMatrixAction, this);
    }

    @Override
    public BlocklyType visitPredefinedImage(PredefinedImage predefinedImage) {
        return Sig.of(BlocklyType.IMAGE).typeCheckPhrases(predefinedImage, this);
    }

    @Override
    public BlocklyType visitDisplayImageAction(DisplayImageAction displayImageAction) {
        if ( displayImageAction.displayImageMode.equals("IMAGE") ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.IMAGE).typeCheckPhrases(displayImageAction, this, displayImageAction.valuesToDisplay);
        } else if ( displayImageAction.displayImageMode.equals("ANIMATION") ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.ARRAY_IMAGE).typeCheckPhrases(displayImageAction, this, displayImageAction.valuesToDisplay);
        } else {
            throw new DbcException("invalid displayImageMode " + displayImageAction.displayImageMode);
        }
    }

    @Override
    public BlocklyType visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        return Sig.of(BlocklyType.IMAGE, BlocklyType.IMAGE, BlocklyType.NUMBER).typeCheckPhrases(imageShiftFunction, this, imageShiftFunction.image, imageShiftFunction.positions);
    }

    @Override
    public BlocklyType visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        return Sig.of(BlocklyType.IMAGE, BlocklyType.IMAGE).typeCheckPhrases(imageInvertFunction, this, imageInvertFunction.image);
    }

    @Override
    public BlocklyType visitImage(Image image) {
        return Sig.of(BlocklyType.IMAGE).typeCheckPhrases(image, this);
    }

    @Override
    public BlocklyType visitRadioSendAction(RadioSendAction radioSendAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(radioSendAction, this);
    }

    @Override
    public BlocklyType visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(radioReceiveAction, this);
    }

    @Override
    public BlocklyType visitDisplaySetPixelAction(DisplaySetPixelAction disp) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER)
            .typeCheckPhrases(disp, this, disp.x, disp.y, disp.brightness);
    }

    @Override
    public BlocklyType visitDisplayGetPixelAction(DisplayGetPixelAction disp) {
        return Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(disp, this, disp.x, disp.y);
    }

    @Override
    public BlocklyType visitRadioSetChannelAction(RadioSetChannelAction radio) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(radio, this, radio.channel);
    }

    @Override
    public BlocklyType visitMbedPinWriteValueAction(MbedPinWriteValueAction pin) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(pin, this, pin.value);
    }

    @Override
    public BlocklyType visitKeysSensor(KeysSensor keysSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(keysSensor, this);
    }

    @Override
    public BlocklyType visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(soundToggleAction, this);
    }

    @Override
    public BlocklyType visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(setVolumeAction, this, setVolumeAction.volume);
    }

    @Override
    public BlocklyType visitSoundSensor(SoundSensor soundSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(soundSensor, this);
    }

    @Override
    public BlocklyType visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(logoTouchSensor, this);
    }

    @Override
    public BlocklyType visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(logoSetTouchMode, this);
    }

    @Override
    public BlocklyType visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(pinSetTouchMode, this);
    }

    @Override
    public BlocklyType visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(temperatureSensor, this);
    }

    @Override
    public BlocklyType visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(accelerometerSensor, this);
    }

    @Override
    public BlocklyType visitLightSensor(LightSensor lightSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(lightSensor, this);
    }

    @Override
    public BlocklyType visitGestureSensor(GestureSensor gestureSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(gestureSensor, this);
    }

    @Override
    public BlocklyType visitCompassSensor(CompassSensor compassSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(compassSensor, this);
    }

    @Override
    public BlocklyType visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(pinTouchSensor, this);
    }

    @Override
    public BlocklyType visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(pinValueSensor, this);
    }

    @Override
    public BlocklyType visitToneAction(ToneAction toneAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(toneAction, this, toneAction.frequency, toneAction.duration);
    }

    @Override
    public BlocklyType visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(playNoteAction, this);
    }
}
