package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actor.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.actor.generic.MbedPinWriteValueAction;
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
import de.fhg.iais.roberta.syntax.actor.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.actor.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.actor.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.actor.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.actor.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.actor.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.expression.mbed.Image;
import de.fhg.iais.roberta.syntax.expression.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.function.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.function.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitor<V>
    extends IDisplayVisitor<V>, ILightVisitor<V>, ISoundVisitor<V>, IMotorVisitor<V>, ISensorVisitor<V>, IPinVisitor<V> {

    /**
     * visit a {@link DisplayTextAction}.
     *
     * @param displayTextAction phrase to be visited
     */
    default V visitDisplayTextAction(DisplayTextAction displayTextAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link PredefinedImage}.
     *
     * @param predefinedImage phrase to be visited
     */
    default V visitPredefinedImage(PredefinedImage predefinedImage) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayImageAction}.
     *
     * @param displayImageAction phrase to be visited
     */
    default V visitDisplayImageAction(DisplayImageAction displayImageAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    default V visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ImageInvertFunction}.
     *
     * @param imageInvertFunction phrase to be visited
     */
    default V visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link Image}.
     *
     * @param image phrase to be visited
     */
    default V visitImage(Image image) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    default V visitLedOnAction(LedOnAction ledOnAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioSendAction}.
     *
     * @param radioSendAction phrase to be visited
     */
    default V visitRadioSendAction(RadioSendAction radioSendAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioReceiveAction}.
     *
     * @param radioReceiveAction phrase to be visited
     */
    default V visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link PinSetPullAction}.
     *
     * @param pinSetPullAction phrase to be visited
     */
    default V visitPinSetPullAction(PinSetPullAction pinSetPullAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplaySetBrightnessAction}.
     *
     * @param displaySetBrightnessAction phrase to be visited
     */
    default V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayGetBrightnessAction}.
     *
     * @param displayGetBrightnessAction phrase to be visited
     */
    default V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplaySetPixelAction}.
     *
     * @param displaySetPixelAction phrase to be visited
     */
    default V visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayGetPixelAction}.
     *
     * @param displayGetPixelAction phrase to be visited
     */
    default V visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioSetChannelAction}.
     *
     * @param radioSetChannelAction phrase to be visited
     */
    default V visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link SingleMotorOnAction}.
     *
     * @param singleMotorOnAction phrase to be visited
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorOnAction
     */
    @Deprecated
    // needed for transformator
    default V visitSingleMotorOnAction(SingleMotorOnAction singleMotorOnAction) {
        throw new DbcException("Block is no longer supported and should not be used! Consider using 'Motor On'");
    }

    /**
     * visit a {@link SingleMotorStopAction}.
     *
     * @param singleMotorStopAction phrase to be visited
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorStopAction
     */
    @Deprecated
    // needed for transformator
    default V visitSingleMotorStopAction(SingleMotorStopAction singleMotorStopAction) {
        throw new DbcException("Block is no longer supported and should not be used! Consider using 'Motor Stop'");
    }

    /**
     * visit a {@link FourDigitDisplayShowAction}.
     *
     * @param fourDigitDisplayShowAction phrase to be visited
     */
    default V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link FourDigitDisplayClearAction}.
     *
     * @param fourDigitDisplayClearAction phrase to be visited
     */
    default V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link BothMotorsOnAction}.
     *
     * @param bothMotorsOnAction phrase to be visited
     */
    default V visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link BothMotorsStopAction}.
     *
     * @param bothMotorsStopAction phrase to be visited
     */
    default V visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioRssiSensor}.
     *
     * @param radioRssiSensor phrase to be visited
     */
    default V visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link LedBarSetAction}.
     *
     * @param ledBarSetAction phrase to be visited
     */
    default V visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link SwitchLedMatrixAction}.
     *
     * @param switchLedMatrixAction phrase to be visited
     */
    default V visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ServoSetAction}.
     *
     * @param servoSetAction phrase to be visited
     */
    default V visitServoSetAction(ServoSetAction servoSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link MotionKitSingleSetAction}.
     *
     * @param motionKitSingleSetAction phrase to be visited
     */
    default V visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link MotionKitDualSetAction}.
     *
     * @param motionKitDualSetAction phrase to be visited
     */
    default V visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitShowTextAction(ShowTextAction showTextAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitPlayFileAction(PlayFileAction playFileAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        throw new DbcException("Mbed devices should use MbedPinWriteValueAction!");
    }

    default V visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        throw new DbcException("Block is not implemented!");
    }
}