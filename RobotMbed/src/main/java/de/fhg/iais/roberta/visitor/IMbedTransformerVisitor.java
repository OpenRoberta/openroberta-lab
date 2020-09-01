package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

public interface IMbedTransformerVisitor<V> extends ITransformerVisitor<V>, IMbedVisitor<Phrase<V>> {

    @Override
    default Phrase<V> visitDisplayTextAction(DisplayTextAction<Phrase<V>> displayTextAction) {
        return DisplayTextAction
            .make(
                displayTextAction.getMode(),
                (Expr<V>) displayTextAction.getMsg().modify(this),
                displayTextAction.getProperty(),
                displayTextAction.getComment());
    }

    @Override
    default Phrase<V> visitPredefinedImage(PredefinedImage<Phrase<V>> predefinedImage) {
        return PredefinedImage.make(predefinedImage.getImageName(), predefinedImage.getProperty(), predefinedImage.getComment());
    }

    @Override
    default Phrase<V> visitDisplayImageAction(DisplayImageAction<Phrase<V>> displayImageAction) {
        return DisplayImageAction
            .make(
                displayImageAction.getDisplayImageMode(),
                (Expr<V>) displayImageAction.getValuesToDisplay().modify(this),
                displayImageAction.getProperty(),
                displayImageAction.getComment());
    }

    @Override
    default Phrase<V> visitImageShiftFunction(ImageShiftFunction<Phrase<V>> imageShiftFunction) {
        return ImageShiftFunction
            .make(
                (Expr<V>) imageShiftFunction.getImage().modify(this),
                (Expr<V>) imageShiftFunction.getPositions().modify(this),
                imageShiftFunction.getShiftDirection(),
                imageShiftFunction.getProperty(),
                imageShiftFunction.getComment());
    }

    @Override
    default Phrase<V> visitImageInvertFunction(ImageInvertFunction<Phrase<V>> imageInvertFunction) {
        return ImageInvertFunction
            .make((Expr<V>) imageInvertFunction.getImage().modify(this), imageInvertFunction.getProperty(), imageInvertFunction.getComment());
    }

    @Override
    default Phrase<V> visitImage(Image<Phrase<V>> image) {
        return Image.make(image.getImage(), image.getProperty(), image.getComment());
    }

    @Override
    default Phrase<V> visitLedOnAction(LedOnAction<Phrase<V>> ledOnAction) {
        return LedOnAction.make(ledOnAction.getPort(), (Expr<V>) ledOnAction.getLedColor().modify(this), ledOnAction.getProperty(), ledOnAction.getComment());
    }

    @Override
    default Phrase<V> visitRadioSendAction(RadioSendAction<Phrase<V>> radioSendAction) {
        return RadioSendAction
            .make(
                (Expr<V>) radioSendAction.getMsg().modify(this),
                radioSendAction.getType(),
                radioSendAction.getPower(),
                radioSendAction.getProperty(),
                radioSendAction.getComment());
    }

    @Override
    default Phrase<V> visitRadioReceiveAction(RadioReceiveAction<Phrase<V>> radioReceiveAction) {
        return RadioReceiveAction.make(radioReceiveAction.getType(), radioReceiveAction.getProperty(), radioReceiveAction.getComment());
    }

    @Override
    default Phrase<V> visitPinSetPullAction(PinSetPullAction<Phrase<V>> pinSetPullAction) {
        return PinSetPullAction.make(pinSetPullAction.getMode(), pinSetPullAction.getPort(), pinSetPullAction.getProperty(), pinSetPullAction.getComment());
    }

    @Override
    default Phrase<V> visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Phrase<V>> displaySetBrightnessAction) {
        return DisplaySetBrightnessAction
            .make(
                (Expr<V>) displaySetBrightnessAction.getBrightness().modify(this),
                displaySetBrightnessAction.getProperty(),
                displaySetBrightnessAction.getComment());
    }

    @Override
    default Phrase<V> visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Phrase<V>> displayGetBrightnessAction) {
        return DisplayGetBrightnessAction.make(displayGetBrightnessAction.getProperty(), displayGetBrightnessAction.getComment());
    }

    @Override
    default Phrase<V> visitDisplaySetPixelAction(DisplaySetPixelAction<Phrase<V>> displaySetPixelAction) {
        return DisplaySetPixelAction
            .make(
                (Expr<V>) displaySetPixelAction.getX().modify(this),
                (Expr<V>) displaySetPixelAction.getY().modify(this),
                (Expr<V>) displaySetPixelAction.getBrightness().modify(this),
                displaySetPixelAction.getProperty(),
                displaySetPixelAction.getComment());
    }

    @Override
    default Phrase<V> visitDisplayGetPixelAction(DisplayGetPixelAction<Phrase<V>> displayGetPixelAction) {
        return DisplayGetPixelAction
            .make(
                (Expr<V>) displayGetPixelAction.getX().modify(this),
                (Expr<V>) displayGetPixelAction.getY().modify(this),
                displayGetPixelAction.getProperty(),
                displayGetPixelAction.getComment());
    }

    @Override
    default Phrase<V> visitRadioSetChannelAction(RadioSetChannelAction<Phrase<V>> radioSetChannelAction) {
        return RadioSetChannelAction
            .make((Expr<V>) radioSetChannelAction.getChannel().modify(this), radioSetChannelAction.getProperty(), radioSetChannelAction.getComment());
    }

    @Override
    default Phrase<V> visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Phrase<V>> fourDigitDisplayShowAction) {
        return FourDigitDisplayShowAction
            .make(
                (Expr<V>) fourDigitDisplayShowAction.getValue().modify(this),
                (Expr<V>) fourDigitDisplayShowAction.getPosition().modify(this),
                (Expr<V>) fourDigitDisplayShowAction.getColon().modify(this),
                fourDigitDisplayShowAction.getProperty(),
                fourDigitDisplayShowAction.getComment());
    }

    @Override
    default Phrase<V> visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Phrase<V>> fourDigitDisplayClearAction) {
        return FourDigitDisplayClearAction.make(fourDigitDisplayClearAction.getProperty(), fourDigitDisplayClearAction.getComment());
    }

    @Override
    default Phrase<V> visitBothMotorsOnAction(BothMotorsOnAction<Phrase<V>> bothMotorsOnAction) {
        return BothMotorsOnAction
            .make(
                bothMotorsOnAction.getPortA(),
                bothMotorsOnAction.getPortB(),
                (Expr<V>) bothMotorsOnAction.getSpeedA().modify(this),
                (Expr<V>) bothMotorsOnAction.getSpeedB().modify(this),
                bothMotorsOnAction.getProperty(),
                bothMotorsOnAction.getComment());
    }

    @Override
    default Phrase<V> visitBothMotorsStopAction(BothMotorsStopAction<Phrase<V>> bothMotorsStopAction) {
        return BothMotorsStopAction.make(bothMotorsStopAction.getProperty(), bothMotorsStopAction.getComment());
    }

    @Override
    default Phrase<V> visitRadioRssiSensor(RadioRssiSensor<Phrase<V>> radioRssiSensor) {
        return RadioRssiSensor.make(radioRssiSensor.getProperty(), radioRssiSensor.getComment());
    }

    @Override
    default Phrase<V> visitLedBarSetAction(LedBarSetAction<Phrase<V>> ledBarSetAction) {
        return LedBarSetAction
            .make(
                (Expr<V>) ledBarSetAction.getX().modify(this),
                (Expr<V>) ledBarSetAction.getBrightness().modify(this),
                ledBarSetAction.getProperty(),
                ledBarSetAction.getComment());
    }

    @Override
    default Phrase<V> visitSwitchLedMatrixAction(SwitchLedMatrixAction<Phrase<V>> switchLedMatrixAction) {
        return SwitchLedMatrixAction.make(switchLedMatrixAction.isActivated(), switchLedMatrixAction.getProperty(), switchLedMatrixAction.getComment());
    }

    @Override
    default Phrase<V> visitServoSetAction(ServoSetAction<Phrase<V>> servoSetAction) {
        return ServoSetAction
            .make(servoSetAction.getPort(), (Expr<V>) servoSetAction.getValue().modify(this), servoSetAction.getProperty(), servoSetAction.getComment());
    }

    @Override
    default Phrase<V> visitMotionKitSingleSetAction(MotionKitSingleSetAction<Phrase<V>> motionKitSingleSetAction) {
        return MotionKitSingleSetAction
            .make(
                motionKitSingleSetAction.getPort(),
                motionKitSingleSetAction.getDirection(),
                motionKitSingleSetAction.getProperty(),
                motionKitSingleSetAction.getComment());
    }

    @Override
    default Phrase<V> visitMotionKitDualSetAction(MotionKitDualSetAction<Phrase<V>> motionKitDualSetAction) {
        return MotionKitDualSetAction
            .make(
                motionKitDualSetAction.getDirectionLeft(),
                motionKitDualSetAction.getDirectionRight(),
                motionKitDualSetAction.getProperty(),
                motionKitDualSetAction.getComment());
    }

    // unrelated defaults

    @Override
    default Phrase<V> visitVolumeAction(VolumeAction<Phrase<V>> volumeAction) {
        return IMbedVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Phrase<V> visitShowTextAction(ShowTextAction<Phrase<V>> showTextAction) {
        return IMbedVisitor.super.visitShowTextAction(showTextAction);
    }

    @Override
    default Phrase<V> visitPlayFileAction(PlayFileAction<Phrase<V>> playFileAction) {
        return IMbedVisitor.super.visitPlayFileAction(playFileAction);
    }
}
