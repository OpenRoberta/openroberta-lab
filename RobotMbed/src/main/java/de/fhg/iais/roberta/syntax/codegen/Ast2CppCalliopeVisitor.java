package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.mode.action.mbed.ActorPort;
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
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValueSensor;
import de.fhg.iais.roberta.syntax.action.mbed.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.check.MbedLoopsCounterVisitor;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.Image;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.hardwarecheck.mbed.UsedHardwareVisitor;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerOrientationSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.AccelerometerSensor.Mode;
import de.fhg.iais.roberta.syntax.sensor.mbed.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.MicrophoneSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.TemperatureSensor;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code for Calliope systems.</b> <br>
 */
public class Ast2CppCalliopeVisitor extends Ast2CppVisitor implements MbedAstVisitor<Void> {
    @SuppressWarnings("unused")
    private final CalliopeConfiguration brickConfiguration;

    private final UsedHardwareVisitor usedHardwareVisitor;

    /**
     * initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ast2CppCalliopeVisitor(CalliopeConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        this.brickConfiguration = brickConfiguration;

        this.usedHardwareVisitor = new UsedHardwareVisitor(programPhrases);

        this.loopsLabels = new MbedLoopsCounterVisitor(programPhrases).getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    public static String generate(CalliopeConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        Ast2CppCalliopeVisitor astVisitor = new Ast2CppCalliopeVisitor(brickConfiguration, programPhrases, 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("ManagedString(");
        super.visitStringConst(stringConst);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("M_PI");
                break;
            case E:
                this.sb.append("M_E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("1.61803398875");
                break;
            case SQRT2:
                this.sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                this.sb.append("M_SQRT1_2");
                break;
            case INFINITY:
                this.sb.append("INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        //TODO there must be a way to make this code simpler
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
            if ( var.getTypeVar().isArray() ) {
                ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                this.sb.append(list.getValue().get().size() + ">");
            }
            this.sb.append(whitespace() + var.getName());
            this.sb.append(whitespace() + "=" + whitespace());
            var.getValue().visit(this);
        } else {
            this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
            if ( var.getTypeVar().isArray() ) {
                this.sb.append("N>");
            }
            this.sb.append(whitespace() + var.getName());
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        Op op = binary.getOp();
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                this.sb.append("ManagedString(");
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                this.sb.append(")");
                break;
            case DIVIDE:
                this.sb.append("((float) ");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }

        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("true");
                break;
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                this.sb.append("MicroBitColor()");
                break;
            case PREDEFINED_IMAGE:
                this.sb.append("MicroBitImage()");
                break;
            case IMAGE:
                this.sb.append("MicroBitImage()");
                break;
            default:
                this.sb.append("NULL");
                break;
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                increaseLoopCounter();
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt();
        }
        addSleepIfForeverLoop(repeatStmt);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (1) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("uBit.sleep(100);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("uBit.sleep(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
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
        String ending = ")";
        String varType = displayTextAction.getMsg().getVarType().toString();
        this.sb.append("uBit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !varType.equals("STRING") ) {
            ending = wrapInManageStringToDisplay(displayTextAction, ending);
        } else {
            displayTextAction.getMsg().visit(this);
        }

        this.sb.append(ending + ";");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("uBit.display.clear();");
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
        this.sb.append("uBit.rgb.off();");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("uBit.soundmotor.soundOn(");
        toneAction.getFrequency().visit(this);
        this.sb.append("); ");
        this.sb.append("uBit.sleep(");
        toneAction.getDuration().visit(this);
        this.sb.append("); ");
        this.sb.append("uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("uBit.soundmotor.motor");
        if ( motorOnAction.getPort() != ActorPort.AB ) {
            this.sb.append(motorOnAction.getPort());
        }
        // fix for IT Gipfel
        else {
            this.sb.append("AOn(");
            motorOnAction.getParam().getSpeed().visit(this);
            this.sb.append(");");
            this.nlIndent();
            this.sb.append("uBit.soundmotor.motorB");
        }
        // fix for IT Gipfel
        this.sb.append("On(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(");");
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
        this.sb.append("uBit.soundmotor.motor");
        if ( motorStopAction.getPort() == ActorPort.AB ) {
            this.sb.append("AOff();");
            nlIndent();
            this.sb.append("uBit.soundmotor.motorB");
        } else {
            this.sb.append(motorStopAction.getPort());
        }
        this.sb.append("Off();");
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
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String key = brickSensor.getKey().toString();
        key = key.substring(key.length() - 1);
        this.sb.append("uBit.button" + key + ".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        String gest = gestureSensor.getMode().toString();
        this.sb.append("(uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
        if ( gestureSensor.getMode().toString() == "UP"
            || gestureSensor.getMode().toString() == "DOWN"
            || gestureSensor.getMode().toString() == "LEFT"
            || gestureSensor.getMode().toString() == "RIGHT" ) {
            this.sb.append("TILT_");
        }
        this.sb.append(gest + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("uBit.thermometer.getTemperature()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("uBit.compass.heading()");
        return null;
    }

    @Override
    public Void visitMicrophoneSensor(MicrophoneSensor<Void> microphoneSensor) {
        this.sb.append("uBit.io.P21.getAnalogValue()");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
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
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("( uBit.systemTime() - initTime )");
                break;
            case RESET:
                this.sb.append("initTime = uBit.systemTime();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        this.sb.append("uBit.io." + pinTouchSensor.getPin().getCalliopeName() + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        String valueType = "AnalogValue()";
        if ( pinValueSensor.getValueType() == ValueType.DIGITAL ) {
            valueType = "DigitalValue()";
        }
        this.sb.append("uBit.io." + pinValueSensor.getPin().getCalliopeName() + ".get" + valueType);
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValueSensor<Void> pinWriteValueSensor) {
        String valueType = "AnalogValue(";
        if ( pinWriteValueSensor.getValueType() == ValueType.DIGITAL ) {
            valueType = "DigitalValue(";
        }
        this.sb.append("uBit.io." + pinWriteValueSensor.getPin().getCalliopeName() + ".set" + valueType);
        pinWriteValueSensor.getValue().visit(this);
        this.sb.append(");");
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
    public Void visitMainTask(MainTask<Void> mainTask) {
        //        decrIndentation();
        this.sb.append("int initTime = uBit.systemTime(); \n");
        mainTask.getVariables().visit(this);
        this.sb.append("\n");
        generateUserDefinedMethods();
        this.sb.append("\n").append("int main() \n");
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        // Initialise the micro:bit runtime.
        this.sb.append("uBit.init();");
        nlIndent();
        if ( this.usedHardwareVisitor.isGreyScale() ) {
            this.sb.append("uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        }
        if ( this.usedHardwareVisitor.isRadioUsed() ) {
            nlIndent();
            this.sb.append("uBit.radio.enable();");
        }
        if ( this.usedHardwareVisitor.isAccelerometerUsed() ) {
            nlIndent();
            this.sb.append("uBit.accelerometer.updateSample();");
        }

        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = "findFirstOccurrenceOfElementInArray(";
        if ( indexOfFunct.getLocation() != IndexLocation.FIRST ) {
            methodName = "findLastOccurrenceOfElementInArray(";
        }
        this.sb.append(methodName);
        if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append("(double) ");
        } else if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.STRING ) {
            this.sb.append("(ManagedString) ");
        }
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            lengthOfIsEmptyFunct.getParam().get(0).visit(this);
            this.sb.append(".empty()");
        } else {
            this.sb.append("((int) ");
            lengthOfIsEmptyFunct.getParam().get(0).visit(this);
            this.sb.append(".size())");
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( getEnumCode(listGetIndex.getLocation()) ) {
            case "IndexLocation.FROM_START":
                listGetIndex.getParam().get(1).visit(this);
                break;
            case "IndexLocation.FROM_END":
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case "IndexLocation.FIRST":
                this.sb.append("0");
                break;
            case "IndexLocation.LAST":
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case "IndexLocation.RANDOM":
                this.sb.append("uBit.random(");
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            return null;
        }
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( getEnumCode(listSetIndex.getLocation()) ) {
            case "IndexLocation.FROM_START":
                listSetIndex.getParam().get(2).visit(this);
                break;
            case "IndexLocation.FROM_END":
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case "IndexLocation.FIRST":
                this.sb.append("0");
                break;
            case "IndexLocation.LAST":
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case "IndexLocation.RANDOM":
                this.sb.append("uBit.random(");
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(";");
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
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                break;
            case WHOLE:
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(",");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                break;
            case MIN:
                break;
            case MAX:
                break;
            case AVERAGE:
                break;
            case MEDIAN:
                break;
            case STD_DEV:
                break;
            case RANDOM:
                break;
            case MODE:
                break;
            default:
                break;
        }
        arrayLen((Var<Void>) mathOnListFunct.getParam().get(0));
        this.sb.append(", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("((double) rand() / (RAND_MAX))");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("(uBit.random(");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(" - ");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(" + 1)");
        this.sb.append(" + ");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        List<Expr<Void>> parameters = textJoinFunct.getParam().get();
        int numberOfParameters = parameters.size();
        for ( int i = 0; i < numberOfParameters; i++ ) {
            this.sb.append("ManagedString(");
            parameters.get(i).visit(this);
            this.sb.append(")");
            if ( i < numberOfParameters - 1 ) {
                this.sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        List<Expr<Void>> parameters = methodVoid.getParameters().get();
        appendTemplateIfArrayParameter(parameters);
        super.visitMethodVoid(methodVoid);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        List<Expr<Void>> parameters = methodReturn.getParameters().get();
        appendTemplateIfArrayParameter(parameters);
        super.visitMethodReturn(methodReturn);
        return null;
    }

    private void appendTemplateIfArrayParameter(List<Expr<Void>> parameters) {
        boolean isContainedArrayParameter = parameters.stream().filter(p -> p.getVarType().isArray()).findFirst().isPresent();
        if ( isContainedArrayParameter ) {
            this.sb.append("\ntemplate<size_t N>");
        }
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        this.sb.append("MicroBitImage(\"" + predefinedImage.getImageName().getImageString() + "\")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        String end = ");";
        this.sb.append("uBit.display.");
        if ( displayImageAction.getDisplayImageMode().name().equals("ANIMATION") ) {
            this.sb.append("animateImages(");
            end = ", 200);";
        } else {
            this.sb.append("print(");
        }
        displayImageAction.getValuesToDisplay().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().visit(this);
        this.sb.append(".shiftImage" + capitalizeFirstLetter(imageShiftFunction.getShiftDirection().toString()) + "(");
        imageShiftFunction.getPositions().visit(this);
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
    public Void visitImage(Image<Void> image) {
        this.sb.append("MicroBitImage(\"");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.sb.append(map(Integer.parseInt(pixel), 0, 9, 0, 255));
                if ( j < 4 ) {
                    this.sb.append(",");
                }
            }
            this.sb.append("\\n");
        }
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        this.sb.append(
            "MicroBitColor("
                + ledColor.getRedChannel()
                + ", "
                + ledColor.getGreenChannel()
                + ", "
                + ledColor.getBlueChannel()
                + ", "
                + ledColor.getAlphaChannel()
                + ")");
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.sb.append("uBit.rgb.setColour(");
        ledOnAction.getLedColor().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitAmbientLightSensor(AmbientLightSensor<Void> ambientLightSensor) {
        this.sb.append("uBit.display.readLightLevel()");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        this.sb.append("uBit.radio.datagram.send(");
        radioSendAction.getMsg().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        this.sb.append("ManagedString(uBit.radio.datagram.recv())");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("uBit.soundmotor.soundOn(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("); ");
        this.sb.append("uBit.sleep(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("); ");
        this.sb.append("uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMbedGetSampleSensor(MbedGetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        if ( accelerometerSensor.getAccelerationDirection() == Mode.STRENGTH ) {
            this.sb.append("uBit.accelerometer.getStrength()");
        } else {
            this.sb.append(String.format("uBit.accelerometer.get%s()", accelerometerSensor.getAccelerationDirection()));
        }
        return null;
    }

    @Override
    public Void visitAccelerometerOrientationSensor(AccelerometerOrientationSensor<Void> accelerometerOrientationSensor) {
        this.sb.append(String.format("uBit.accelerometer." + accelerometerOrientationSensor.getAccelerationOrientationMode().getCppCode()));
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("MicroBitColor(");
        rgbColor.getR().visit(this);
        this.sb.append(", ");
        rgbColor.getG().visit(this);
        this.sb.append(", ");
        rgbColor.getB().visit(this);
        this.sb.append(", ");
        rgbColor.getA().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        this.sb.append("uBit.display.setBrightness((");
        displaySetBrightnessAction.getBrightness().visit(this);
        this.sb.append(") * 255.0 / 9.0);");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        this.sb.append("round(uBit.display.getBrightness() * 9.0 / 255.0)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        this.sb.append("uBit.display.image.setPixelValue(");
        displaySetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        this.sb.append(" * 255.0 / 9.0);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("round(uBit.display.image.getPixelValue(");
        displayGetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        this.sb.append(") * 9.0 / 255.0)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.addConstants();

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("release_fiber();");
            this.sb.append("\n}\n");
        }

    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case R:
            case S:
            case T:
                return "";
            case ARRAY:
                return "array<";
            case ARRAY_NUMBER:
                return "array<double, ";
            case ARRAY_STRING:
                return "array<ManagedString,";
            case ARRAY_BOOLEAN:
                return "array<bool,";
            case ARRAY_IMAGE:
                return "array<MicroBitImage,";
            case ARRAY_COLOUR:
                return "array<MicroBitColor,";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "double";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "ManagedString";
            case VOID:
                return "void";
            case COLOR:
                return "MicroBitColor";
            case CONNECTION:
                return "int";
            case IMAGE:
                return "MicroBitImage";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private void addSleepIfForeverLoop(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == RepeatStmt.Mode.FOREVER ) {
            nlIndent();
            this.sb.append("uBit.sleep(1);");
        }
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private void arrayLen(Var<Void> arr) {
        this.sb.append(arr.getValue() + ".size()");
    }

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            this.sb.append("scroll(");
        } else {
            this.sb.append("print(");
        }
    }

    private String wrapInManageStringToDisplay(DisplayTextAction<Void> displayTextAction, String ending) {
        this.sb.append("ManagedString(");
        displayTextAction.getMsg().visit(this);
        ending += ")";
        return ending;
    }

    private String capitalizeFirstLetter(String original) {
        if ( original == null || original.length() == 0 ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    private void addConstants() {
        this.sb.append("#define _GNU_SOURCE\n\n");
        this.sb.append("#include \"MicroBit.h\" \n");
        this.sb.append("#include <array>\n");
        this.sb.append("#include <stdlib.h>\n");
        this.sb.append("MicroBit uBit;\n\n");
    }
}