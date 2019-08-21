package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * Collector for the Mbed robots.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface IMbedCollectorVisitor extends ICollectorVisitor, IMbedVisitor<Void> {

    @Override
    default Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().visit(this);
        return null;
    }

    @Override
    default Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    default Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().visit(this);
        return null;
    }

    @Override
    default Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().visit(this);
        imageShiftFunction.getPositions().visit(this);
        return null;
    }

    @Override
    default Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().visit(this);
        return null;
    }

    @Override
    default Void visitImage(Image<Void> image) {
        return null;
    }

    @Override
    default Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ledOnAction.getLedColor().visit(this);
        return null;
    }

    @Override
    default Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().visit(this);
        return null;
    }

    @Override
    default Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        return null;
    }

    @Override
    default Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        return null;
    }

    @Override
    default Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().visit(this);
        return null;
    }

    @Override
    default Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        return null;
    }

    @Override
    default Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        displaySetPixelAction.getBrightness().visit(this);
        displaySetPixelAction.getX().visit(this);
        displaySetPixelAction.getY().visit(this);
        return null;
    }

    @Override
    default Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        displayGetPixelAction.getX().visit(this);
        displayGetPixelAction.getY().visit(this);
        return null;
    }

    @Override
    default Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        radioSetChannelAction.getChannel().visit(this);
        return null;
    }

    @Override
    default Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        singleMotorOnAction.getSpeed().visit(this);
        return null;
    }

    @Override
    default Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        return null;
    }

    @Override
    default Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        fourDigitDisplayShowAction.getColon().visit(this);
        fourDigitDisplayShowAction.getPosition().visit(this);
        fourDigitDisplayShowAction.getValue().visit(this);
        return null;
    }

    @Override
    default Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        return null;
    }

    @Override
    default Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        bothMotorsOnAction.getSpeedA().visit(this);
        bothMotorsOnAction.getSpeedB().visit(this);
        return null;
    }

    @Override
    default Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        return null;
    }

    @Override
    default Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        return null;
    }

    @Override
    default Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        ledBarSetAction.getBrightness().visit(this);
        ledBarSetAction.getX().visit(this);
        return null;
    }

    @Override
    default Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        return null;
    }

    @Override
    default Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        pinWriteValueAction.getValue().visit(this);
        return null;
    }

    // following methods are used to specify unrelated defaults

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return ICollectorVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        return ICollectorVisitor.super.visitShowTextAction(showTextAction);
    }

    @Override
    default Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return ICollectorVisitor.super.visitPlayFileAction(playFileAction);
    }
}
