package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actor.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.actor.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actor.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.actor.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.actor.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.actor.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.actor.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.actor.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.actor.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.actor.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.actor.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.actor.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.actor.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.actor.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.actor.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.actor.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.actor.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.actor.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.actor.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.actor.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.expression.mbed.Image;
import de.fhg.iais.roberta.syntax.expression.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.function.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.function.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;

public interface IMbedTransformerVisitor extends ITransformerVisitor, IMbedVisitor<Phrase> {

    @Override
    default Phrase visitDisplayTextAction(DisplayTextAction displayTextAction) {
        return new DisplayTextAction(displayTextAction.getProperty(), displayTextAction.mode, (Expr) displayTextAction.msg.modify(this));
    }

    @Override
    default Phrase visitPredefinedImage(PredefinedImage predefinedImage) {
        return new PredefinedImage(predefinedImage.getProperty(), predefinedImage.getImageNameString());
    }

    @Override
    default Phrase visitDisplayImageAction(DisplayImageAction displayImageAction) {
        return new DisplayImageAction(displayImageAction.getProperty(), displayImageAction.mutation, displayImageAction.displayImageMode, (Expr) displayImageAction.valuesToDisplay.modify(this));
    }

    @Override
    default Phrase visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        return new ImageShiftFunction(imageShiftFunction.getProperty(), imageShiftFunction.shiftDirection, (Expr) imageShiftFunction.image.modify(this), (Expr) imageShiftFunction.positions.modify(this));
    }

    @Override
    default Phrase visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        return new ImageInvertFunction(imageInvertFunction.getProperty(), (Expr) imageInvertFunction.image.modify(this));
    }

    @Override
    default Phrase visitImage(Image image) {
        return new Image(image.image, image.getProperty());
    }

    @Override
    default Phrase visitLedOnAction(LedOnAction ledOnAction) {
        return new LedOnAction(ledOnAction.getProperty(), (Expr) ledOnAction.ledColor.modify(this), ledOnAction.getUserDefinedPort(), ledOnAction.hide);
    }

    @Override
    default Phrase visitRadioSendAction(RadioSendAction radioSendAction) {
        return new RadioSendAction(radioSendAction.getProperty(), radioSendAction.mutation, radioSendAction.type, radioSendAction.power, (Expr) radioSendAction.message.modify(this));
    }

    @Override
    default Phrase visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        return new RadioReceiveAction(radioReceiveAction.getProperty(), radioReceiveAction.mutation, radioReceiveAction.type);
    }

    @Override
    default Phrase visitPinSetPullAction(PinSetPullAction pinSetPullAction) {
        return new PinSetPullAction(pinSetPullAction.getProperty(), pinSetPullAction.pinPull, pinSetPullAction.port);
    }

    @Override
    default Phrase visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        return new DisplaySetBrightnessAction(displaySetBrightnessAction.getProperty(), (Expr) displaySetBrightnessAction.brightness.modify(this));
    }

    @Override
    default Phrase visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        return new DisplayGetBrightnessAction(displayGetBrightnessAction.getProperty());
    }

    @Override
    default Phrase visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        return new DisplaySetPixelAction(displaySetPixelAction.getProperty(), (Expr) displaySetPixelAction.x.modify(this), (Expr) displaySetPixelAction.y.modify(this), (Expr) displaySetPixelAction.brightness.modify(this));
    }

    @Override
    default Phrase visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        return new DisplayGetPixelAction(displayGetPixelAction.getProperty(), (Expr) displayGetPixelAction.x.modify(this), (Expr) displayGetPixelAction.y.modify(this));
    }

    @Override
    default Phrase visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        return new RadioSetChannelAction(radioSetChannelAction.getProperty(), (Expr) radioSetChannelAction.channel.modify(this));
    }

    @Override
    default Phrase visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        return new FourDigitDisplayShowAction(fourDigitDisplayShowAction.getProperty(), (Expr) fourDigitDisplayShowAction.value.modify(this), (Expr) fourDigitDisplayShowAction.position.modify(this), (Expr) fourDigitDisplayShowAction.colon.modify(this));
    }

    @Override
    default Phrase visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        return new FourDigitDisplayClearAction(fourDigitDisplayClearAction.getProperty());
    }

    @Override
    default Phrase visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        return new BothMotorsOnAction(bothMotorsOnAction.getProperty(), (Expr) bothMotorsOnAction.speedA.modify(this), (Expr) bothMotorsOnAction.speedB.modify(this), bothMotorsOnAction.portA, bothMotorsOnAction.portB);
    }

    @Override
    default Phrase visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        return new BothMotorsStopAction(bothMotorsStopAction.getProperty());
    }

    @Override
    default Phrase visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        return new RadioRssiSensor(radioRssiSensor.getProperty(), radioRssiSensor.getSensorMetaDataBean());
    }

    @Override
    default Phrase visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        return new LedBarSetAction(ledBarSetAction.getProperty(), (Expr) ledBarSetAction.x.modify(this), (Expr) ledBarSetAction.brightness.modify(this));
    }

    @Override
    default Phrase visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return new SwitchLedMatrixAction(switchLedMatrixAction.getProperty(), switchLedMatrixAction.activated);
    }

    @Override
    default Phrase visitServoSetAction(ServoSetAction servoSetAction) {
        return new ServoSetAction(servoSetAction.getProperty(), servoSetAction.getUserDefinedPort(), (Expr) servoSetAction.value.modify(this));
    }

    @Override
    default Phrase visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        return new MotionKitSingleSetAction(motionKitSingleSetAction.getProperty(), motionKitSingleSetAction.port, motionKitSingleSetAction.direction);
    }

    @Override
    default Phrase visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        return new MotionKitDualSetAction(motionKitDualSetAction.getProperty(), motionKitDualSetAction.directionLeft, motionKitDualSetAction.directionRight);
    }

    // unrelated defaults

    @Override
    default Phrase visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        return IMbedVisitor.super.visitGetVolumeAction(getVolumeAction);
    }

    @Override
    default Phrase visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return IMbedVisitor.super.visitSetVolumeAction(setVolumeAction);
    }

    @Override
    default Phrase visitShowTextAction(ShowTextAction showTextAction) {
        return IMbedVisitor.super.visitShowTextAction(showTextAction);
    }

    @Override
    default Phrase visitPlayFileAction(PlayFileAction playFileAction) {
        return IMbedVisitor.super.visitPlayFileAction(playFileAction);
    }

    @Override
    default Phrase visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        return ITransformerVisitor.super.visitPinWriteValueAction(pinWriteValueAction);
    }
}
