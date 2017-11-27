package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.mbed.MicrobitConfiguration;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.mbed.ValueType;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.hardware.mbed.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotPythonVisitor;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.mbed.RgbColor;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerOrientationSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor.Mode;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented
 * and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class PythonVisitor extends RobotPythonVisitor implements MbedAstVisitor<Void>, AstSensorsVisitor<Void>, AstActorMotorVisitor<Void>,
    AstActorDisplayVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {
    private final UsedHardwareCollectorVisitor usedHardwareVisitor;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private PythonVisitor(MicrobitConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        usedHardwareVisitor = new UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);
        loopsLabels = usedHardwareVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public static String generate(MicrobitConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        final PythonVisitor astVisitor = new PythonVisitor(brickConfiguration, programPhrases, 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        sb.append("'").append(StringEscapeUtils.escapeEcmaScript(stringConst.getValue().replaceAll("[<>\\$]", ""))).append("'");
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        sb.append("microbit.Image." + predefinedImage.getImageName());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                sb.append("\"\"");
                break;
            case BOOLEAN:
                sb.append("True");
                break;
            case NUMBER_INT:
                sb.append("0");
                break;
            case ARRAY:
            case NULL:
                break;
            case PREDEFINED_IMAGE:
                sb.append("microbit.Image.SILLY");
                break;
            case IMAGE:
                sb.append("microbit.Image()");
                break;
            default:
                sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        decrIndentation();
        //        nlIndent();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        sb.append("microbit.sleep(");
        waitTimeStmt.getTime().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        sb.append("microbit.display.clear()");
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        sb.append("microbit.Image('");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                sb.append(pixel);
            }
            if ( i < 4 ) {
                sb.append(":");
            }
        }
        sb.append("')");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
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
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        sb.append("microbit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !displayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            sb.append("str(");
            displayTextAction.getMsg().visit(this);
            sb.append(")");
        } else {
            displayTextAction.getMsg().visit(this);
        }
        sb.append(")");
        return null;
    }

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            sb.append("scroll(");
        } else {
            sb.append("show(");
        }
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        sb.append("microbit.display.show(");
        displayImageAction.getValuesToDisplay().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().visit(this);
        sb.append(".invert()");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        sb.append("microbit." + brickSensor.getKey().toString().toLowerCase() + ".is_pressed()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        sb.append("microbit.temperature()");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
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
    public Void visitSoundSensor(SoundSensor<Void> sensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> sensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        if ( timerSensor.getMode() == TimerSensorMode.GET_SAMPLE ) {
            sb.append("( microbit.running_time() - timer1 )");
        } else {
            sb.append("timer1 = microbit.running_time()");
        }
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
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        if ( accelerometerSensor.getAccelerationDirection() == Mode.STRENGTH ) {
            sb.append("math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)");
        } else {
            sb.append(String.format("microbit.accelerometer.get_%s()", accelerometerSensor.getAccelerationDirection().toString().toLowerCase()));
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        final String valueType = pinValueSensor.getValueType().toString().toLowerCase();
        sb.append("microbit.pin" + pinValueSensor.getPin().getPinNumber() + ".read_" + valueType + "()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        final StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        sb.append("\"" + gestureSensor.getMode().getPythonCode() + "\" == microbit.accelerometer.current_gesture()");
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().visit(this);
        sb.append(".shift_" + imageShiftFunction.getShiftDirection().toString().toLowerCase() + "(");
        imageShiftFunction.getPositions().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        // TODO
        //        this.sb.append("BlocklyMethods.listsGetSubList( ");
        //        getSubFunct.getParam().get(0).visit(this);
        //        this.sb.append(", ");
        //        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        //        this.sb.append(getEnumCode(where1));
        //        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
        //            this.sb.append(", ");
        //            getSubFunct.getParam().get(1).visit(this);
        //        }
        //        this.sb.append(", ");
        //        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        //        this.sb.append(getEnumCode(where2));
        //        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
        //            this.sb.append(", ");
        //            if ( getSubFunct.getParam().size() == 3 ) {
        //                getSubFunct.getParam().get(2).visit(this);
        //            } else {
        //                getSubFunct.getParam().get(1).visit(this);
        //            }
        //        }
        //        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                indexOfFunct.getParam().get(0).visit(this);
                sb.append(".index(");
                indexOfFunct.getParam().get(1).visit(this);
                sb.append(")");
                break;
            case LAST:
                sb.append("(len(");
                indexOfFunct.getParam().get(0).visit(this);
                sb.append(") - 1) - ");
                indexOfFunct.getParam().get(0).visit(this);
                sb.append("[::-1].index(");
                indexOfFunct.getParam().get(1).visit(this);
                sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                sb.append("len( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;

            case LIST_IS_EMPTY:
                sb.append("not ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        sb.append("[");
        listCreate.getValue().visit(this);
        sb.append("]");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        sb.append("[");
        listRepeat.getParam().get(0).visit(this);
        sb.append("] * ");
        listRepeat.getParam().get(1).visit(this);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).visit(this);
        if ( getEnumCode(listGetIndex.getElementOperation()).equals("get") ) {
            sb.append("[");
            listGetIndex.getParam().get(1).visit(this);
            sb.append("]");
        } else if ( getEnumCode(listGetIndex.getElementOperation()).equals("remove") ) {
            sb.append(".pop(");
            listGetIndex.getParam().get(1).visit(this);
            sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( getEnumCode(listSetIndex.getElementOperation()).equals("set") ) {
            listSetIndex.getParam().get(0).visit(this);
            sb.append("[");
            listSetIndex.getParam().get(1).visit(this);
            sb.append("] = ");
            listSetIndex.getParam().get(2).visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        sb.append("min(max(");
        mathConstrainFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        sb.append("), ");
        mathConstrainFunct.getParam().get(2).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % 2) == 0");
                break;
            case ODD:
                sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % 2) == 1");
                break;
            case PRIME:
                // TODO
                //                this.sb.append("BlocklyMethods.isPrime(");
                //                mathNumPropFunct.getParam().get(0).visit(this);
                //                this.sb.append(")");
                break;
            case WHOLE:
                sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % 1) == 0");
                break;
            case POSITIVE:
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" > 0");
                break;
            case NEGATIVE:
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % ");
                mathNumPropFunct.getParam().get(1).visit(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                sb.append("sum(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                sb.append("min(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                sb.append("max(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                sb.append("float(sum(");
                mathOnListFunct.getParam().get(0).visit(this);
                sb.append("))/len(");
                mathOnListFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case MEDIAN:
                // TODO
                //                this.sb.append("BlocklyMethods.medianOnList(");
                //                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                // TODO
                //                this.sb.append("BlocklyMethods.standardDeviatioin(");
                //                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                // TODO
                //                this.sb.append("BlocklyMethods.randOnList(");
                //                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                // TODO
                //                this.sb.append("BlocklyMethods.modeOnList(");
                //                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        sb.append("random.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        sb.append("random.randint(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        sb.append("\"\".join(");
        textJoinFunct.getParam().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        sb.append("microbit.compass.heading()");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        sb.append("microbit.pin" + pinTouchSensor.getPin().getPinNumber() + ".is_touched()");
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        sb.append("radio.config(power=" + radioSendAction.getPower() + ")");
        nlIndent();
        sb.append("radio.send(");
        radioSendAction.getMsg().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        sb.append("radio.receive()");
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        sb.append("radio.config(group=");
        radioSetChannelAction.getChannel().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMbedGetSampleSensor(MbedGetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValue<Void> pinWriteValueSensor) {
        sb.append("microbit.pin" + pinWriteValueSensor.getPin().getPinNumber());
        String valueType = "analog(";
        if ( pinWriteValueSensor.getValueType() == ValueType.DIGITAL ) {
            valueType = "digital(";
        }
        sb.append(".write_" + valueType);
        pinWriteValueSensor.getValue().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        return null;
    }

    @Override
    public Void visitAccelerometerOrientationSensor(AccelerometerOrientationSensor<Void> accelerometerOrientationSensor) {
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        sb.append("microbit.display.set_pixel(");
        displaySetPixelAction.getX().visit(this);
        sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        sb.append("microbit.display.get_pixel(");
        displayGetPixelAction.getX().visit(this);
        sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        sb.append("import microbit\n");
        sb.append("import random\n");
        sb.append("import math\n");

        if ( usedHardwareVisitor.isRadioUsed() ) {
            sb.append("import radio\n\n");
            sb.append("radio.on()\n");
        } else {
            nlIndent();
        }
        sb.append("class BreakOutOfALoop(Exception): pass\n");
        sb.append("class ContinueLoop(Exception): pass\n\n");
        sb.append("timer1 = microbit.running_time()");
        generateUserDefinedMethods();

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
    }

    @Override
    public String getEnumCode(IMode value) {
        return value.toString().toLowerCase();
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
