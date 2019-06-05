package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
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
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.MbedUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class MicrobitPythonVisitor extends AbstractPythonVisitor implements IMbedVisitor<Void> {
    private final MbedUsedHardwareCollectorVisitor usedHardwareVisitor;
    private boolean medianUsed;
    private boolean stdDevUsed;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private MicrobitPythonVisitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
        this.usedHardwareVisitor = new MbedUsedHardwareCollectorVisitor(programPhrases, brickConfiguration);
        this.loopsLabels = this.usedHardwareVisitor.getloopsLabelContainer();
        this.usedGlobalVarInFunctions = this.usedHardwareVisitor.getMarkedVariablesAsGlobal();
        programPhrases.forEach(e -> {
            e.forEach(e1 -> {
                if ( e1.toString().contains("MEDIAN") ) {
                    this.medianUsed = true;
                }
                if ( e1.toString().contains("STD_DEV") ) {
                    this.stdDevUsed = true;
                }
            });
        });
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        final MicrobitPythonVisitor astVisitor = new MicrobitPythonVisitor(brickConfiguration, programPhrases, 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    public static String generate(ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        final MicrobitPythonVisitor astVisitor = new MicrobitPythonVisitor(null, programPhrases, 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        this.sb.append("microbit.Image." + predefinedImage.getImageName());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("True");
                break;
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case CAPTURED_TYPE:
                this.sb.append("None");
            case ARRAY:
            case NULL:
                break;
            case PREDEFINED_IMAGE:
                this.sb.append("microbit.Image.SILLY");
                break;
            case IMAGE:
                this.sb.append("microbit.Image()");
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        decrIndentation();
        //        nlIndent();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("microbit.sleep(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("microbit.display.clear()");
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        this.sb.append("microbit.Image('");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.sb.append(pixel);
            }
            if ( i < 4 ) {
                this.sb.append(":");
            }
        }
        this.sb.append("')");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        this.sb.append("microbit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !displayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            displayTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            displayTextAction.getMsg().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            this.sb.append("scroll(");
        } else {
            this.sb.append("show(");
        }
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        this.sb.append("microbit.display.show(");
        displayImageAction.getValuesToDisplay().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().visit(this);
        this.sb.append(".invert()");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        String userDefined = keysSensor.getPort();
        this.sb.append("microbit.button_").append(userDefined.toLowerCase()).append(".is_pressed()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("microbit.temperature()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("( microbit.running_time() - timer1 )");
                break;
            case SC.RESET:
                this.sb.append("timer1 = microbit.running_time()");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");

        }

        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        String userDefined = accelerometerSensor.getPort();
        if ( userDefined.equals(SC.STRENGTH) ) {
            this.sb.append("math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)");
        } else {
            this.sb.append("microbit.accelerometer.get_").append(userDefined.toLowerCase()).append("()");
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        final String valueType = pinValueSensor.getMode().toString().toLowerCase();
        this.sb.append("microbit.pin");
        this.sb.append(pinValueSensor.getPort());
        this.sb.append(".read_");
        this.sb.append(valueType);
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.usedGlobalVarInFunctions.clear();
        this.usedGlobalVarInFunctions.add("timer1");
        final StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        generateUserDefinedMethods();
        this.sb.append("\n").append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        }
        return null;
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(this.INDENT).append("try:\n");
        this.sb.append(this.INDENT).append(this.INDENT).append("run()\n");
        this.sb.append(this.INDENT).append("except Exception as e:\n");
        this.sb.append(this.INDENT).append(this.INDENT).append("raise\n");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(this.INDENT).append("main()");
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("\"" + gestureSensor.getMode().toString().toLowerCase().replace("_", " ") + "\" == microbit.accelerometer.current_gesture()");
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().visit(this);
        this.sb.append(".shift_" + imageShiftFunction.getShiftDirection().toString().toLowerCase() + "(");
        imageShiftFunction.getPositions().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getFunctName() == FunctionNames.GET_SUBLIST ) {
            getSubFunct.getParam().get(0).visit(this);
            this.sb.append("[");
            switch ( (IndexLocation) getSubFunct.getStrParam().get(0) ) {
                case FIRST:
                    this.sb.append("0:");
                    break;
                case FROM_END:
                    this.sb.append("-");
                    getSubFunct.getParam().get(1).visit(this);
                    this.sb.append(":");
                    break;
                case FROM_START:
                    getSubFunct.getParam().get(1).visit(this);
                    this.sb.append(":");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.getStrParam().get(1) ) {
                case LAST:
                    this.sb.append("-1");
                    break;
                case FROM_END:
                    this.sb.append("-");
                    try {
                        getSubFunct.getParam().get(2).visit(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.getParam().get(1).visit(this);
                    }
                    break;
                case FROM_START:
                    try {
                        getSubFunct.getParam().get(2).visit(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.getParam().get(1).visit(this);
                    }
                    break;
                default:
                    break;
            }
            this.sb.append("]");
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(".index(");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("(len(");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(") - 1) - ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append("[::-1].index(");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LIST_LENGTH:
                this.sb.append("len( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("not ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("[");
        listCreate.getValue().visit(this);
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("[");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append("] * ");
        listRepeat.getParam().get(1).visit(this);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).visit(this);
        if ( getEnumCode(listGetIndex.getElementOperation()).equals("get") ) {
            this.sb.append("[");
        } else {
            this.sb.append(".pop(");
        }
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case RANDOM: // backwards compatibility
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case LAST:
                this.sb.append("-1");
                break;
            default:
                break;
        }
        if ( getEnumCode(listGetIndex.getElementOperation()).equals("get") ) {
            this.sb.append("]");
        } else {
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( getEnumCode(listSetIndex.getElementOperation()).equals("set") ) {
            listSetIndex.getParam().get(0).visit(this);
            this.sb.append("[");
        } else if ( getEnumCode(listSetIndex.getElementOperation()).equals("insert") ) {
            listSetIndex.getParam().get(0).visit(this);
            this.sb.append(".insert(");
        }
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case RANDOM: // backwards compatibility
            case FIRST:
                this.sb.append("0");
                break;
            case FROM_END:
                this.sb.append("-");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case LAST:
                this.sb.append("-1");
                break;
            default:
                break;
        }
        if ( getEnumCode(listSetIndex.getElementOperation()).equals("set") ) {
            this.sb.append("] = ");
            listSetIndex.getParam().get(1).visit(this);
        } else if ( getEnumCode(listSetIndex.getElementOperation()).equals("insert") ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(1).visit(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("min(max(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append("), ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2) == 0");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2) == 1");
                break;
            case PRIME:
                this.sb.append("false # not implemented yet");
                // TODO
                //                this.sb.append("BlocklyMethods.isPrime(");
                //                mathNumPropFunct.getParam().get(0).visit(this);
                //                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 1) == 0");
                break;
            case POSITIVE:
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % ");
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
                this.sb.append("sum(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case MIN:
                this.sb.append("min(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case MAX:
                this.sb.append("max(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case AVERAGE:
                this.sb.append("float(sum(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append("))/len(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case MEDIAN:
                this.sb.append("_median(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case STD_DEV:
                this.sb.append("_standard_deviation(");
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case RANDOM:
                mathOnListFunct.getParam().get(0).visit(this);
                this.sb.append("[0]");
                break;
            case MODE:
                // TODO
                //                this.sb.append("BlocklyMethods.modeOnList(");
                //                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("random.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("random.randint(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("\"\".join(str(arg) for arg in [");
        textJoinFunct.getParam().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("microbit.compass.heading()");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        this.sb.append("microbit.pin" + pinTouchSensor.getPort() + ".is_touched()");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        this.sb.append("radio.config(power=" + radioSendAction.getPower() + ")");
        nlIndent();
        this.sb.append("radio.send(str(");
        radioSendAction.getMsg().visit(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        switch ( radioReceiveAction.getType() ) {
            case NUMBER:
                this.sb.append("((lambda x: 0 if x is None else float(x))(radio.receive()))");
                break;
            case BOOLEAN:
                this.sb.append("('True' == radio.receive())");
                break;
            case STRING:
                this.sb.append("radio.receive()");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        this.sb.append("radio.config(group=");
        radioSetChannelAction.getChannel().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        this.sb.append("microbit.pin" + pinWriteValueAction.getPort());
        String valueType = pinWriteValueAction.getMode().equals(SC.DIGITAL) ? "digital(" : "analog(";
        this.sb.append(".write_").append(valueType);
        pinWriteValueAction.getValue().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        // TODO add as soon as microbit runtime is updated
        //        this.sb.append("microbit.pin" + pinSetPullAction.getPort().getValues()[0] + ".set_pull(");
        //        switch ( pinSetPullAction.getMode() ) {
        //            case SC.UP:
        //                this.sb.append("PULL_UP");
        //                break;
        //            case SC.DOWN:
        //                this.sb.append("PULL_DOWN");
        //                break;
        //            case SC.NONE:
        //            default:
        //                this.sb.append("NO_PULL");
        //                break;
        //        }
        //        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        this.sb.append("microbit.display.set_pixel(");
        displaySetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("microbit.display.get_pixel(");
        displayGetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        this.sb.append(")");
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

    private void generateMedianComputeFunction() {
        this.sb.append("def _median(l):");
        incrIndentation();
        nlIndent();
        this.sb.append("l = sorted(l)");
        nlIndent();
        this.sb.append("l_len = len(l)");
        nlIndent();
        this.sb.append("if l_len < 1:");
        incrIndentation();
        nlIndent();
        this.sb.append("return None");
        decrIndentation();
        nlIndent();
        this.sb.append("if l_len % 2 == 0:");
        incrIndentation();
        nlIndent();
        this.sb.append("return ( l[int( (l_len-1) / 2)] + l[int( (l_len+1) / 2)] ) / 2.0");
        decrIndentation();
        nlIndent();
        this.sb.append("else:");
        incrIndentation();
        nlIndent();
        this.sb.append("return l[int( (l_len-1) / 2)]");
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();
    }

    private void generateStandardDeviationComputeFunction() {
        this.sb.append("def _standard_deviation(l):");
        incrIndentation();
        nlIndent();
        this.sb.append("mean = float(sum(l)) / len(l)");
        nlIndent();
        this.sb.append("sd = 0");
        nlIndent();
        this.sb.append("for i in l:");
        incrIndentation();
        nlIndent();
        this.sb.append("sd += (i - mean)*(i - mean)");
        decrIndentation();
        nlIndent();
        this.sb.append("return math.sqrt(sd / len(l))");
        decrIndentation();
        nlIndent();
        nlIndent();
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("import microbit\n");
        this.sb.append("import random\n");
        this.sb.append("import math\n\n");
        this.sb.append("_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2\n\n");

        if ( this.usedHardwareVisitor.isRadioUsed() ) {
            this.sb.append("import radio\n\n");
            this.sb.append("radio.on()\n");
        } else {
            nlIndent();
        }
        this.sb.append("class BreakOutOfALoop(Exception): pass\n");
        this.sb.append("class ContinueLoop(Exception): pass\n\n");

        if ( this.medianUsed ) {
            generateMedianComputeFunction();
        }
        if ( this.stdDevUsed ) {
            generateStandardDeviationComputeFunction();
        }

        this.sb.append("timer1 = microbit.running_time()");

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
    public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        this.sb.append("# " + stmtTextComment.getTextComment());
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
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("math.pi");
                break;
            case E:
                this.sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                this.sb.append("_GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("math.sqrt(0.5)");
                break;
            case INFINITY:
                this.sb.append("math.inf");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        this.sb.append("print(");
        serialWriteAction.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
