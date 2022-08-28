package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DcMotorSetAction;
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
        return new DisplayImageAction(displayImageAction.getProperty(), displayImageAction.displayImageMode, (Expr) displayImageAction.getValuesToDisplay().modify(this));
    }

    @Override
    default Phrase visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        return new ImageShiftFunction((Expr) imageShiftFunction.image.modify(this), (Expr) imageShiftFunction.positions.modify(this), imageShiftFunction.shiftDirection, imageShiftFunction.getProperty());
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
        return new RadioSendAction(radioSendAction.getProperty(), (Expr) radioSendAction.message.modify(this), radioSendAction.type, radioSendAction.power);
    }

    @Override
    default Phrase visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        return new RadioReceiveAction(radioReceiveAction.getProperty(), radioReceiveAction.type);
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
        return RadioRssiSensor.make(radioRssiSensor.getSensorMetaDataBean(), radioRssiSensor.getProperty());
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
    default Phrase visitVolumeAction(VolumeAction volumeAction) {
        return IMbedVisitor.super.visitVolumeAction(volumeAction);
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
    default Phrase visitDcMotorSetAction(DcMotorSetAction dcMotorSetAction) {
        return new DcMotorSetAction(
            dcMotorSetAction.getProperty(),
            dcMotorSetAction.actorPort,
            (Expr) dcMotorSetAction.motor.modify(this),
            dcMotorSetAction.direction,
            (Expr) dcMotorSetAction.speed.modify(this)
        );
    }
}
