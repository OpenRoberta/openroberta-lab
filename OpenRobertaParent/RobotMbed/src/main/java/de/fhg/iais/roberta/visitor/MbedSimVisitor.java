package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValue;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.AbstractSimVisitor;

public final class MbedSimVisitor extends AbstractSimVisitor<Void> implements IMbedVisitor<Void> {

    private MbedSimVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        final MbedSimVisitor astVisitor = new MbedSimVisitor(brickConfiguration);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final String end = createClosingBracket();
        this.sb.append("createMotorOnAction(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(", " + "CONST.MOTOR_X" + motorOnAction.getPort());

        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        final String end = createClosingBracket();
        this.sb.append("createStopMotorAction(");
        this.sb.append("CONST.MOTOR_X" + motorStopAction.getPort());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        final String end = createClosingBracket();
        this.sb.append("createStatusLight(CONST." + lightStatusAction.getStatus());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        final String end = createClosingBracket();
        this.sb.append("createClearDisplayAction(");
        this.sb.append(end);
        return null;
    }

    //TODO: this code is duplicated for all specific sim visitors (refactor)
    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("createGetSample(CONST.TIMER, 'timer" + timerSensor.getPort().getOraName() + "')");
                break;
            case RESET:
                String end = createClosingBracket();
                this.sb.append("createResetTimer('timer" + timerSensor.getPort().getOraName() + "'");
                this.sb.append(end);
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        sensorGetSample.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        String end = createClosingBracket();
        this.sb.append("createToneAction(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        String end = createClosingBracket();
        this.sb.append("createToneAction(");
        this.sb.append("createConstant(CONST.NUM_CONST, " + playNoteAction.getFrequency() + ")");
        this.sb.append(", ");
        this.sb.append("createConstant(CONST.NUM_CONST, " + playNoteAction.getDuration() + ")");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String key = brickSensor.getPort().toString().toUpperCase();
        this.sb.append("createGetSample(CONST.BUTTONS, CONST." + key + ")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("createGetSample(CONST.AMBIENTLIGHT)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.sb.append("0");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("createGetSample(CONST.COMPASS)");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        final String end = createClosingBracket();
        this.sb.append("createDisplayTextAction(CONST.");
        this.sb.append(displayTextAction.getMode().toString());
        this.sb.append(", ");
        displayTextAction.getMsg().visit(this);
        this.sb.append(end);
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
        this.sb.append(predefinedImageArray);
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        final String end = createClosingBracket();
        this.sb.append("createDisplayImageAction(CONST.");
        this.sb.append(displayImageAction.getDisplayImageMode() + ", ");
        displayImageAction.getValuesToDisplay().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        this.sb.append("createImageShiftAction(CONST." + imageShiftFunction.getShiftDirection() + ", ");
        imageShiftFunction.getPositions().visit(this);
        this.sb.append(", ");
        imageShiftFunction.getImage().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        this.sb.append("createImageInvertAction(");
        imageInvertFunction.getImage().visit(this);
        this.sb.append(")");
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

        this.sb.append("createConstant(CONST." + image.getKind().getName() + ", [" + imageString + "])");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("createGetSample(CONST.GESTURE, CONST." + gestureSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("createGetSample(CONST.TEMPERATURE)");
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        this.sb
            .append(
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
        this.sb.append("createLedOnAction(");
        ledOnAction.getLedColor().visit(this);
        this.sb.append(end);
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
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        this.sb.append("createPinTouchSensor(" + pinTouchSensor.getPort().getOraName() + ")");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        this.sb.append("createPinGetValueSensor(CONST." + pinValueSensor.getMode());
        this.sb.append(", " + pinValueSensor.getPort().getOraName() + ")");
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValue<Void> pinWriteValueSensor) {
        final String end = createClosingBracket();
        this.sb.append("createPinWriteValueSensor(CONST." + pinWriteValueSensor.getMode());
        this.sb.append(", " + pinWriteValueSensor.getPort().getOraName() + ", ");
        pinWriteValueSensor.getValue().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        final String end = createClosingBracket();
        this.sb.append("createDisplaySetBrightnessAction(");
        displaySetBrightnessAction.getBrightness().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        this.sb.append("createDisplayGetBrightnessAction(CONST.BRIGHTNESS)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        final String end = createClosingBracket();
        this.sb.append("createDisplaySetPixelAction(");
        displaySetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("createDisplayGetPixelAction(");
        displayGetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.sb.append("0");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("createGetSample(CONST.SOUND)");
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

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
