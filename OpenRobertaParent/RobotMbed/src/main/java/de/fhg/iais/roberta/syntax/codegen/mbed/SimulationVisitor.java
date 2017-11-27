package de.fhg.iais.roberta.syntax.codegen.mbed;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValue;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.codegen.RobotSimulationVisitor;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.mbed.RgbColor;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerOrientationSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

public class SimulationVisitor extends RobotSimulationVisitor<Void> implements MbedAstVisitor<Void> {

    private SimulationVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        final SimulationVisitor astVisitor = new SimulationVisitor(brickConfiguration);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final String end = createClosingBracket();
        sb.append("createMotorOnAction(");
        motorOnAction.getParam().getSpeed().visit(this);
        sb.append(", " + "CONST.MOTOR_X" + motorOnAction.getPort());

        sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        final String end = createClosingBracket();
        sb.append("createStopMotorAction(");
        sb.append("CONST.MOTOR_X" + motorStopAction.getPort());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        final String end = createClosingBracket();
        sb.append("createStatusLight(CONST." + lightStatusAction.getStatus());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        final String end = createClosingBracket();
        sb.append("createClearDisplayAction(");
        sb.append(end);
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        final String key = brickSensor.getKey().toString().toUpperCase();
        sb.append("createGetSample(CONST.BUTTONS, CONST." + key + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        sb.append("createGetSample(CONST.AMBIENTLIGHT)");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        sb.append("createGetSample(CONST.COMPASS)");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        final String end = createClosingBracket();
        sb.append("createDisplayTextAction(CONST.");
        sb.append(displayTextAction.getMode().toString());
        sb.append(", ");
        displayTextAction.getMsg().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        final String image = predefinedImage.getImageName().getImageString();
        final String[] imageArray = image.split("\\\\n");
        String predefinedImageArray = "createConstant(CONST.IMAGE, [";
        for ( final String element : imageArray ) {
            predefinedImageArray += "[";
            predefinedImageArray += element;
            predefinedImageArray += "],";
        }
        predefinedImageArray += "])";
        sb.append(predefinedImageArray);
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        final String end = createClosingBracket();
        sb.append("createDisplayImageAction(CONST.");
        sb.append(displayImageAction.getDisplayImageMode() + ", ");
        displayImageAction.getValuesToDisplay().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        sb.append("createImageShiftAction(CONST." + imageShiftFunction.getShiftDirection() + ", ");
        imageShiftFunction.getPositions().visit(this);
        sb.append(", ");
        imageShiftFunction.getImage().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        sb.append("createImageInvertAction(");
        imageInvertFunction.getImage().visit(this);
        sb.append(")");
        return null;
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (((x - in_min) * (out_max - out_min)) / (in_max - in_min)) + out_min;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        String imageString = "";
        for ( int i = 0; i < 5; i++ ) {
            imageString += "[";
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                imageString += map(Integer.parseInt(pixel), 0, 9, 0, 255);
                if ( j < 4 ) {
                    imageString += ",";
                }
            }
            imageString += "]";
            if ( i < 4 ) {
                imageString += ",";
            }
        }

        sb.append("createConstant(CONST." + image.getKind().getName() + ", [" + imageString + "])");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        sb.append("createGetSample(CONST.GESTURE, CONST." + gestureSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        sb.append("createGetSample(CONST.TEMPERATURE)");
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        sb.append(
            "createConstant(CONST."
                + ledColor.getKind().getName()
                + ", ["
                + ledColor.getRedChannel()
                + ", "
                + ledColor.getGreenChannel()
                + ", "
                + ledColor.getBlueChannel()
                + "])");
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        final String end = createClosingBracket();
        sb.append("createLedOnAction(");
        ledOnAction.getLedColor().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        return null;
    }

    @Override
    public Void visitMbedGetSampleSensor(MbedGetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        sb.append("createRgbColor([");
        rgbColor.getR().visit(this);
        sb.append(", ");
        rgbColor.getG().visit(this);
        sb.append(", ");
        rgbColor.getB().visit(this);
        sb.append("])");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        sb.append("createPinTouchSensor(" + pinTouchSensor.getPin().getPinNumber() + ")");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        sb.append("createPinGetValueSensor(CONST." + pinValueSensor.getValueType().toString());
        sb.append(", " + pinValueSensor.getPin().getPinNumber() + ")");
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValue<Void> pinWriteValueSensor) {
        final String end = createClosingBracket();
        sb.append("createPinWriteValueSensor(CONST." + pinWriteValueSensor.getValueType().toString());
        sb.append(", " + pinWriteValueSensor.getPin().getPinNumber() + ", ");
        pinWriteValueSensor.getValue().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        final String end = createClosingBracket();
        sb.append("createDisplaySetBrightnessAction(");
        displaySetBrightnessAction.getBrightness().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        sb.append("createDisplayGetBrightnessAction(CONST.BRIGHTNESS)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        final String end = createClosingBracket();
        sb.append("createDisplaySetPixelAction(");
        displaySetPixelAction.getX().visit(this);
        sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        sb.append("createDisplayGetPixelAction(");
        displayGetPixelAction.getX().visit(this);
        sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        sb.append("0");
        return null;
    }

    @Override
    public Void visitAccelerometerOrientationSensor(AccelerometerOrientationSensor<Void> accelerometerOrientationSensor) {
        sb.append("0");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        sb.append("0");
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        return null;
    }
}
