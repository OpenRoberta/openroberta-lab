package de.fhg.iais.roberta.visitor.hardware;

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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.MbedTwo2ThreeTransformerVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IMbedVisitor<V>
    extends IDisplayVisitor<V>, ILightVisitor<V>, ISoundVisitor<V>, IMotorVisitor<V>, ISensorVisitor<V>, ISerialVisitor<V>, IPinVisitor<V> {

    /**
     * visit a {@link DisplayTextAction}.
     *
     * @param displayTextAction phrase to be visited
     */
    default V visitDisplayTextAction(DisplayTextAction<V> displayTextAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link PredefinedImage}.
     *
     * @param predefinedImage phrase to be visited
     */
    default V visitPredefinedImage(PredefinedImage<V> predefinedImage) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayImageAction}.
     *
     * @param displayImageAction phrase to be visited
     */
    default V visitDisplayImageAction(DisplayImageAction<V> displayImageAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    default V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ImageInvertFunction}.
     *
     * @param imageInvertFunction phrase to be visited
     */
    default V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link Image}.
     *
     * @param image phrase to be visited
     */
    default V visitImage(Image<V> image) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    default V visitLedOnAction(LedOnAction<V> ledOnAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioSendAction}.
     *
     * @param radioSendAction phrase to be visited
     */
    default V visitRadioSendAction(RadioSendAction<V> radioSendAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioReceiveAction}.
     *
     * @param radioReceiveAction phrase to be visited
     */
    default V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link PinSetPullAction}.
     *
     * @param pinSetPullAction phrase to be visited
     */
    default V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplaySetBrightnessAction}.
     *
     * @param displaySetBrightnessAction phrase to be visited
     */
    default V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayGetBrightnessAction}.
     *
     * @param displayGetBrightnessAction phrase to be visited
     */
    default V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplaySetPixelAction}.
     *
     * @param displaySetPixelAction phrase to be visited
     */
    default V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link DisplayGetPixelAction}.
     *
     * @param displayGetPixelAction phrase to be visited
     */
    default V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioSetChannelAction}.
     *
     * @param radioSetChannelAction phrase to be visited
     */
    default V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link SingleMotorOnAction}.
     *
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorOnAction
     * @param singleMotorOnAction phrase to be visited
     */
    @Deprecated
    default V visitSingleMotorOnAction(SingleMotorOnAction<V> singleMotorOnAction) {
        throw new DbcException("Block is no longer supported and should not be used!");
    }

    /**
     * visit a {@link SingleMotorStopAction}.
     *
     * @deprecated should only be used by {@link MbedTwo2ThreeTransformerVisitor} to generate a MotorStopAction
     * @param singleMotorStopAction phrase to be visited
     */
    @Deprecated
    default V visitSingleMotorStopAction(SingleMotorStopAction<V> singleMotorStopAction) {
        throw new DbcException("Block is no longer supported and should not be used!");
    }

    /**
     * visit a {@link FourDigitDisplayShowAction}.
     *
     * @param fourDigitDisplayShowAction phrase to be visited
     */
    default V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<V> fourDigitDisplayShowAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link FourDigitDisplayClearAction}.
     *
     * @param fourDigitDisplayClearAction phrase to be visited
     */
    default V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<V> fourDigitDisplayClearAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link BothMotorsOnAction}.
     *
     * @param bothMotorsOnAction phrase to be visited
     */
    default V visitBothMotorsOnAction(BothMotorsOnAction<V> bothMotorsOnAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link BothMotorsStopAction}.
     *
     * @param bothMotorsStopAction phrase to be visited
     */
    default V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link RadioRssiSensor}.
     *
     * @param radioRssiSensor phrase to be visited
     */
    default V visitRadioRssiSensor(RadioRssiSensor<V> radioRssiSensor) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link LedBarSetAction}.
     *
     * @param ledBarSetAction phrase to be visited
     */
    default V visitLedBarSetAction(LedBarSetAction<V> ledBarSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link SwitchLedMatrixAction}.
     *
     * @param switchLedMatrixAction phrase to be visited
     */
    default V visitSwitchLedMatrixAction(SwitchLedMatrixAction<V> switchLedMatrixAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link ServoSetAction}.
     *
     * @param servoSetAction phrase to be visited
     */
    default V visitServoSetAction(ServoSetAction<V> servoSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link MotionKitSingleSetAction}.
     *
     * @param motionKitSingleSetAction phrase to be visited
     */
    default V visitMotionKitSingleSetAction(MotionKitSingleSetAction<V> motionKitSingleSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    /**
     * visit a {@link MotionKitDualSetAction}.
     *
     * @param motionKitDualSetAction phrase to be visited
     */
    default V visitMotionKitDualSetAction(MotionKitDualSetAction<V> motionKitDualSetAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitShowTextAction(ShowTextAction<V> showTextAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Block is not implemented!");
    }

    @Override
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Block is not implemented!");
    }

}