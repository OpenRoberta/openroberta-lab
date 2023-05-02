package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
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
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;

public abstract class MbedTransformerVisitor extends TransformerVisitor {
    public Phrase visitDisplayTextAction(DisplayTextAction displayTextAction) {
        return new DisplayTextAction(displayTextAction.getProperty(), displayTextAction.mode, (Expr) displayTextAction.msg.modify(this));
    }

    public Phrase visitPredefinedImage(PredefinedImage predefinedImage) {
        return new PredefinedImage(predefinedImage.getProperty(), predefinedImage.getImageNameString());
    }

    public Phrase visitDisplayImageAction(DisplayImageAction displayImageAction) {
        return new DisplayImageAction(displayImageAction.getProperty(), displayImageAction.mutation, displayImageAction.displayImageMode, (Expr) displayImageAction.valuesToDisplay.modify(this));
    }

    public Phrase visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        return new ImageShiftFunction(imageShiftFunction.getProperty(), imageShiftFunction.shiftDirection, (Expr) imageShiftFunction.image.modify(this), (Expr) imageShiftFunction.positions.modify(this));
    }

    public Phrase visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        return new ImageInvertFunction(imageInvertFunction.getProperty(), (Expr) imageInvertFunction.image.modify(this));
    }


    public Phrase visitImage(Image image) {
        return new Image(image.image, image.getProperty());
    }


    public Phrase visitLedOnAction(LedOnAction ledOnAction) {
        return new LedOnAction(ledOnAction.getProperty(), (Expr) ledOnAction.ledColor.modify(this), ledOnAction.getUserDefinedPort(), ledOnAction.hide);
    }


    public Phrase visitRadioSendAction(RadioSendAction radioSendAction) {
        return new RadioSendAction(radioSendAction.getProperty(), radioSendAction.mutation, radioSendAction.type, radioSendAction.power, (Expr) radioSendAction.message.modify(this));
    }


    public Phrase visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        return new RadioReceiveAction(radioReceiveAction.getProperty(), radioReceiveAction.mutation, radioReceiveAction.type);
    }

    public Phrase visitPinSetPullAction(PinSetPullAction pinSetPullAction) {
        return new PinSetPullAction(pinSetPullAction.getProperty(), pinSetPullAction.pinPull, pinSetPullAction.port);
    }


    public Phrase visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        return new DisplaySetBrightnessAction(displaySetBrightnessAction.getProperty(), (Expr) displaySetBrightnessAction.brightness.modify(this));
    }


    public Phrase visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        return new DisplayGetBrightnessAction(displayGetBrightnessAction.getProperty());
    }


    public Phrase visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        return new DisplaySetPixelAction(displaySetPixelAction.getProperty(), (Expr) displaySetPixelAction.x.modify(this), (Expr) displaySetPixelAction.y.modify(this), (Expr) displaySetPixelAction.brightness.modify(this));
    }


    public Phrase visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        return new DisplayGetPixelAction(displayGetPixelAction.getProperty(), (Expr) displayGetPixelAction.x.modify(this), (Expr) displayGetPixelAction.y.modify(this));
    }


    public Phrase visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        return new RadioSetChannelAction(radioSetChannelAction.getProperty(), (Expr) radioSetChannelAction.channel.modify(this));
    }


    public Phrase visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        return null;
    }


    public Phrase visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        return new FourDigitDisplayShowAction(fourDigitDisplayShowAction.getProperty(), (Expr) fourDigitDisplayShowAction.value.modify(this), (Expr) fourDigitDisplayShowAction.position.modify(this), (Expr) fourDigitDisplayShowAction.colon.modify(this));
    }


    public Phrase visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        return new FourDigitDisplayClearAction(fourDigitDisplayClearAction.getProperty());
    }


    public Phrase visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        return new BothMotorsOnAction(bothMotorsOnAction.getProperty(), (Expr) bothMotorsOnAction.speedA.modify(this), (Expr) bothMotorsOnAction.speedB.modify(this), bothMotorsOnAction.portA, bothMotorsOnAction.portB);
    }


    public Phrase visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        return new BothMotorsStopAction(bothMotorsStopAction.getProperty());
    }


    public Phrase visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        return new RadioRssiSensor(radioRssiSensor.getProperty(), radioRssiSensor.getSensorMetaDataBean());
    }


    public Phrase visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        return new LedBarSetAction(ledBarSetAction.getProperty(), (Expr) ledBarSetAction.x.modify(this), (Expr) ledBarSetAction.brightness.modify(this));
    }


    public Phrase visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return new SwitchLedMatrixAction(switchLedMatrixAction.getProperty(), switchLedMatrixAction.activated);
    }


    public Phrase visitServoSetAction(ServoSetAction servoSetAction) {
        return new ServoSetAction(servoSetAction.getProperty(), servoSetAction.getUserDefinedPort(), (Expr) servoSetAction.value.modify(this));
    }


    public Phrase visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        return new MotionKitSingleSetAction(motionKitSingleSetAction.getProperty(), motionKitSingleSetAction.port, motionKitSingleSetAction.direction);
    }


    public Phrase visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        return new MotionKitDualSetAction(motionKitDualSetAction.getProperty(), motionKitDualSetAction.directionLeft, motionKitDualSetAction.directionRight);
    }

    // unrelated defaults
    @Override
    public Phrase visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        return super.visitPinWriteValueAction(pinWriteValueAction);
    }

    /**
     * visit a {@link SingleMotorOnAction}.
     *
     * @param singleMotorOnAction phrase to be visited
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorOnAction
     */
    @Deprecated
    // needed for transformator
    abstract Phrase visitSingleMotorOnAction(SingleMotorOnAction singleMotorOnAction);

    /**
     * visit a {@link SingleMotorStopAction}.
     *
     * @param singleMotorStopAction phrase to be visited
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorStopAction
     */
    @Deprecated
    // needed for transformator
    abstract Phrase visitSingleMotorStopAction(SingleMotorStopAction singleMotorStopAction);
}
