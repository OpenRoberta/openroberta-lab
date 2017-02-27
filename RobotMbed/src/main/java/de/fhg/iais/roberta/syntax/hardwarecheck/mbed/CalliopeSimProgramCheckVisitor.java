package de.fhg.iais.roberta.syntax.hardwarecheck.mbed;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValueSensor;
import de.fhg.iais.roberta.syntax.action.mbed.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.expr.Image;
import de.fhg.iais.roberta.syntax.expr.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.functions.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.TemperatureSensor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

public class CalliopeSimProgramCheckVisitor extends SimulationProgramCheckVisitor implements MbedAstVisitor<Void> {

    public CalliopeSimProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        //motorOnAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        //motorStopAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().visit(this);
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ledOnAction.getLedColor().visit(this);
        return null;
    }

    @Override
    public Void visitAmbientLightSensor(AmbientLightSensor<Void> ambientLightSensor) {
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        return null;
    }

    @Override
    public Void visitMbedGetSampleSensor(MbedGetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().visit(this);
        rgbColor.getG().visit(this);
        rgbColor.getB().visit(this);
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValueSensor<Void> pinWriteValueSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
