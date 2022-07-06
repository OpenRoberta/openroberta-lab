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

public interface IMbedTransformerVisitor<V> extends ITransformerVisitor<V>, IMbedVisitor<Phrase<V>> {

    @Override
    default Phrase<V> visitDisplayTextAction(DisplayTextAction<Phrase<V>> displayTextAction) {
        return new DisplayTextAction<>(displayTextAction.getProperty(), displayTextAction.mode, (Expr<V>) displayTextAction.msg.modify(this));
    }

    @Override
    default Phrase<V> visitPredefinedImage(PredefinedImage<Phrase<V>> predefinedImage) {
        return new PredefinedImage<V>(predefinedImage.getProperty(), predefinedImage.getImageNameString());
    }

    @Override
    default Phrase<V> visitDisplayImageAction(DisplayImageAction<Phrase<V>> displayImageAction) {
        return new DisplayImageAction<>(displayImageAction.getProperty(), displayImageAction.displayImageMode, (Expr<V>) displayImageAction.getValuesToDisplay().modify(this));
    }

    @Override
    default Phrase<V> visitImageShiftFunction(ImageShiftFunction<Phrase<V>> imageShiftFunction) {
        return new ImageShiftFunction<>((Expr<V>) imageShiftFunction.image.modify(this), (Expr<V>) imageShiftFunction.positions.modify(this), imageShiftFunction.shiftDirection, imageShiftFunction.getProperty());
    }

    @Override
    default Phrase<V> visitImageInvertFunction(ImageInvertFunction<Phrase<V>> imageInvertFunction) {
        return new ImageInvertFunction<>(imageInvertFunction.getProperty(), (Expr<V>) imageInvertFunction.image.modify(this));
    }

    @Override
    default Phrase<V> visitImage(Image<Phrase<V>> image) {
        return new Image<>(image.image, image.getProperty());
    }

    @Override
    default Phrase<V> visitLedOnAction(LedOnAction<Phrase<V>> ledOnAction) {
        return new LedOnAction<>(ledOnAction.getProperty(), (Expr<V>) ledOnAction.ledColor.modify(this), ledOnAction.getUserDefinedPort(), ledOnAction.hide);
    }

    @Override
    default Phrase<V> visitRadioSendAction(RadioSendAction<Phrase<V>> radioSendAction) {
        return new RadioSendAction<>(radioSendAction.getProperty(), (Expr<V>) radioSendAction.message.modify(this), radioSendAction.type, radioSendAction.power);
    }

    @Override
    default Phrase<V> visitRadioReceiveAction(RadioReceiveAction<Phrase<V>> radioReceiveAction) {
        return new RadioReceiveAction<>(radioReceiveAction.getProperty(), radioReceiveAction.type);
    }

    @Override
    default Phrase<V> visitPinSetPullAction(PinSetPullAction<Phrase<V>> pinSetPullAction) {
        return new PinSetPullAction<>(pinSetPullAction.getProperty(), pinSetPullAction.pinPull, pinSetPullAction.port);
    }

    @Override
    default Phrase<V> visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Phrase<V>> displaySetBrightnessAction) {
        return new DisplaySetBrightnessAction<>(displaySetBrightnessAction.getProperty(), (Expr<V>) displaySetBrightnessAction.brightness.modify(this));
    }

    @Override
    default Phrase<V> visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Phrase<V>> displayGetBrightnessAction) {
        return new DisplayGetBrightnessAction<>(displayGetBrightnessAction.getProperty());
    }

    @Override
    default Phrase<V> visitDisplaySetPixelAction(DisplaySetPixelAction<Phrase<V>> displaySetPixelAction) {
        return new DisplaySetPixelAction<>(displaySetPixelAction.getProperty(), (Expr<V>) displaySetPixelAction.x.modify(this), (Expr<V>) displaySetPixelAction.y.modify(this), (Expr<V>) displaySetPixelAction.brightness.modify(this));
    }

    @Override
    default Phrase<V> visitDisplayGetPixelAction(DisplayGetPixelAction<Phrase<V>> displayGetPixelAction) {
        return new DisplayGetPixelAction<>(displayGetPixelAction.getProperty(), (Expr<V>) displayGetPixelAction.x.modify(this), (Expr<V>) displayGetPixelAction.y.modify(this));
    }

    @Override
    default Phrase<V> visitRadioSetChannelAction(RadioSetChannelAction<Phrase<V>> radioSetChannelAction) {
        return new RadioSetChannelAction<>(radioSetChannelAction.getProperty(), (Expr<V>) radioSetChannelAction.channel.modify(this));
    }

    @Override
    default Phrase<V> visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Phrase<V>> fourDigitDisplayShowAction) {
        return new FourDigitDisplayShowAction<>(fourDigitDisplayShowAction.getProperty(), (Expr<V>) fourDigitDisplayShowAction.value.modify(this), (Expr<V>) fourDigitDisplayShowAction.position.modify(this), (Expr<V>) fourDigitDisplayShowAction.colon.modify(this));
    }

    @Override
    default Phrase<V> visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Phrase<V>> fourDigitDisplayClearAction) {
        return new FourDigitDisplayClearAction<>(fourDigitDisplayClearAction.getProperty());
    }

    @Override
    default Phrase<V> visitBothMotorsOnAction(BothMotorsOnAction<Phrase<V>> bothMotorsOnAction) {
        return new BothMotorsOnAction<V>(bothMotorsOnAction.getProperty(), (Expr<V>) bothMotorsOnAction.speedA.modify(this), (Expr<V>) bothMotorsOnAction.speedB.modify(this), bothMotorsOnAction.portA, bothMotorsOnAction.portB);
    }

    @Override
    default Phrase<V> visitBothMotorsStopAction(BothMotorsStopAction<Phrase<V>> bothMotorsStopAction) {
        return new BothMotorsStopAction<>(bothMotorsStopAction.getProperty());
    }

    @Override
    default Phrase<V> visitRadioRssiSensor(RadioRssiSensor<Phrase<V>> radioRssiSensor) {
        return RadioRssiSensor.make(radioRssiSensor.getSensorMetaDataBean(), radioRssiSensor.getProperty());
    }

    @Override
    default Phrase<V> visitLedBarSetAction(LedBarSetAction<Phrase<V>> ledBarSetAction) {
        return new LedBarSetAction<>(ledBarSetAction.getProperty(), (Expr<V>) ledBarSetAction.x.modify(this), (Expr<V>) ledBarSetAction.brightness.modify(this));
    }

    @Override
    default Phrase<V> visitSwitchLedMatrixAction(SwitchLedMatrixAction<Phrase<V>> switchLedMatrixAction) {
        return new SwitchLedMatrixAction<>(switchLedMatrixAction.getProperty(), switchLedMatrixAction.activated);
    }

    @Override
    default Phrase<V> visitServoSetAction(ServoSetAction<Phrase<V>> servoSetAction) {
        return new ServoSetAction<>(servoSetAction.getProperty(), servoSetAction.getUserDefinedPort(), (Expr<V>) servoSetAction.value.modify(this));
    }

    @Override
    default Phrase<V> visitMotionKitSingleSetAction(MotionKitSingleSetAction<Phrase<V>> motionKitSingleSetAction) {
        return new MotionKitSingleSetAction<>(motionKitSingleSetAction.getProperty(), motionKitSingleSetAction.port, motionKitSingleSetAction.direction);
    }

    @Override
    default Phrase<V> visitMotionKitDualSetAction(MotionKitDualSetAction<Phrase<V>> motionKitDualSetAction) {
        return new MotionKitDualSetAction<>(motionKitDualSetAction.getProperty(), motionKitDualSetAction.directionLeft, motionKitDualSetAction.directionRight);
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
