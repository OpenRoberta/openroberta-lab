package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.mbed.CalliopeConfiguration;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
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
import de.fhg.iais.roberta.syntax.codegen.RobotCppVisitor;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.expr.mbed.RgbColor;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
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
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor.GestureMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.MbedGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code for Calliope systems.</b> <br>
 */
public class CppVisitor extends RobotCppVisitor implements MbedAstVisitor<Void>, AstSensorsVisitor<Void>, AstActorMotorVisitor<Void>,
    AstActorDisplayVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {
    private final UsedHardwareCollectorVisitor codePreprocess;
    ArrayList<VarDeclaration<Void>> usedVars;

    /**
     * initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private CppVisitor(CalliopeConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        codePreprocess = new UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

        loopsLabels = codePreprocess.getloopsLabelContainer();
        userDefinedMethods = codePreprocess.getUserDefinedMethods();
        usedVars = codePreprocess.getVisitedVars();
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

        final CppVisitor astVisitor = new CppVisitor(brickConfiguration, programPhrases, 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        sb.append("ManagedString(");
        super.visitStringConst(stringConst);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                sb.append("M_PI");
                break;
            case E:
                sb.append("M_E");
                break;
            case GOLDEN_RATIO:
                sb.append("1.61803398875");
                break;
            case SQRT2:
                sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                sb.append("M_SQRT1_2");
                break;
            case INFINITY:
                sb.append("INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        //TODO there must be a way to make this code simpler
        sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
        if ( var.getTypeVar().isArray() ) {
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                final ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                sb.append(list.getValue().get().size() + ">");
            } else {
                sb.append("N>");
            }
        }
        sb.append(whitespace() + var.getName());
        return null;
    }

    protected Void generateUsedVars() {
        for ( final VarDeclaration<Void> var : usedVars ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                /*if ( var.getTypeVar().isArray() ) {
                    ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                    this.sb.append(list.getValue().get().size() + ">");
                }*/
                sb.append(var.getName());
                sb.append(whitespace() + "=" + whitespace());
                var.getValue().visit(this);
                sb.append(";");
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        final Op op = binary.getOp();
        if ( op == Op.MOD ) {
            sb.append("(int) ");
        }
        generateSubExpr(sb, false, binary.getLeft(), binary);

        final String sym = getBinaryOperatorSymbol(op);
        sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                sb.append("ManagedString(");
                generateSubExpr(sb, false, binary.getRight(), binary);
                sb.append(")");
                break;
            case DIVIDE:
                sb.append("((float) ");
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                sb.append(")");
                break;
            case MOD:
                sb.append("((int) ");
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                sb.append(")");
                break;
            default:
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
        }

        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                sb.append("\"\"");
                break;
            case BOOLEAN:
                sb.append("true");
                break;
            case NUMBER_INT:
                sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                sb.append("MicroBitColor()");
                break;
            case PREDEFINED_IMAGE:
                sb.append("MicroBitImage()");
                break;
            case IMAGE:
                sb.append("MicroBitImage()");
                break;
            default:
                sb.append("NULL");
                break;
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        final boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
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
            nlIndent();
            sb.append("uBit.sleep(1);");
        } else {
            appendBreakStmt();
        }

        decrIndentation();
        nlIndent();
        sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        sb.append("while (1) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        sb.append("uBit.sleep(1);");
        decrIndentation();
        nlIndent();
        sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        sb.append("uBit.sleep(");
        waitTimeStmt.getTime().visit(this);
        sb.append(");");
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
        final String varType = displayTextAction.getMsg().getVarType().toString();
        sb.append("uBit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !varType.equals("STRING") ) {
            ending = wrapInManageStringToDisplay(displayTextAction, ending);
        } else {
            displayTextAction.getMsg().visit(this);
        }

        sb.append(ending + ";");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        sb.append("uBit.display.clear();");
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
        sb.append("uBit.rgb.off();");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        sb.append("uBit.soundmotor.soundOn(");
        toneAction.getFrequency().visit(this);
        sb.append("); ");
        sb.append("uBit.sleep(");
        toneAction.getDuration().visit(this);
        sb.append("); ");
        sb.append("uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        sb.append("uBit.soundmotor.soundOn(");
        sb.append(playNoteAction.getFrequency());
        sb.append("); ");
        sb.append("uBit.sleep(");
        sb.append(playNoteAction.getDuration());
        sb.append("); ");
        sb.append("uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        sb.append("uBit.soundmotor.motor");
        if ( motorOnAction.getPort() != ActorPort.AB ) {
            sb.append(motorOnAction.getPort());
        }
        // fix for IT Gipfel
        else {
            sb.append("AOn(");
            motorOnAction.getParam().getSpeed().visit(this);
            sb.append(");");
            nlIndent();
            sb.append("uBit.soundmotor.motorB");
        }
        // fix for IT Gipfel
        sb.append("On(");
        motorOnAction.getParam().getSpeed().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        sb.append("uBit.soundmotor.motorOn(");
        singleMotorOnAction.getSpeed().visit(this);
        sb.append(");");
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
        sb.append("uBit.soundmotor.motor");
        if ( motorStopAction.getPort() == ActorPort.AB ) {
            sb.append("AOff();");
            nlIndent();
            sb.append("uBit.soundmotor.motorB");
        } else {
            sb.append(motorStopAction.getPort());
        }
        sb.append("Off();");
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        sb.append("uBit.soundmotor.motor");
        switch ( (MotorStopMode) singleMotorStopAction.getMode() ) {
            case FLOAT:
                sb.append("Coast();");
                break;
            case NONFLOAT:
                sb.append("Break();");
                break;
            case SLEEP:
                sb.append("Sleep();");
                break;
            default:
                throw new DbcException("Invalide stop mode " + singleMotorStopAction.getMode());
        }
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
        sb.append("uBit.display.readLightLevel()");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String key = brickSensor.getKey().toString();
        key = key.substring(key.length() - 1).toUpperCase();
        sb.append("uBit.button" + key + ".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        final GestureMode gest = gestureSensor.getMode();
        sb.append("(uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
        if ( (gestureSensor.getMode() == GestureMode.UP)
            || (gestureSensor.getMode() == GestureMode.DOWN)
            || (gestureSensor.getMode() == GestureMode.LEFT)
            || (gestureSensor.getMode() == GestureMode.RIGHT) ) {
            sb.append("TILT_");
        }
        sb.append(gest.toString() + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        sb.append("uBit.thermometer.getTemperature()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        sb.append("uBit.compass.heading()");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> microphoneSensor) {
        sb.append("uBit.io.P21.getMicrophoneValue()");
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
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                sb.append("( uBit.systemTime() - initTime )");
                break;
            case RESET:
                sb.append("initTime = uBit.systemTime();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        sb.append("uBit.io." + pinTouchSensor.getPin().getCalliopeName() + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        sb.append("uBit.io." + pinValueSensor.getPin().getCalliopeName());
        switch ( pinValueSensor.getValueType() ) {
            case DIGITAL:
                sb.append(".getDigitalValue()");
                break;
            case ANALOG:
                sb.append(".getAnalogValue()");
                break;
            case PULSEHIGH:
                sb.append(".readPulseHigh()");
                break;
            case PULSELOW:
                sb.append(".readPulseLow()");
                break;
            default:
                throw new DbcException("Valu type  " + pinValueSensor.getValueType() + " is not supported.");
        }
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValue<Void> pinWriteValueSensor) {
        String valueType = "AnalogValue(";
        if ( pinWriteValueSensor.getValueType() == ValueType.DIGITAL ) {
            valueType = "DigitalValue(";
        }
        sb.append("uBit.io." + pinWriteValueSensor.getPin().getCalliopeName() + ".set" + valueType);
        pinWriteValueSensor.getValue().visit(this);
        sb.append(");");
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
        sb.append("int initTime = uBit.systemTime(); \n");
        mainTask.getVariables().visit(this);

        sb.append("\n").append("int main() \n");
        sb.append("{");
        incrIndentation();
        nlIndent();
        // Initialise the micro:bit runtime.
        sb.append("uBit.init();");
        generateUsedVars();
        nlIndent();
        if ( codePreprocess.isGreyScale() ) {
            sb.append("uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        }
        if ( codePreprocess.isRadioUsed() ) {
            nlIndent();
            sb.append("uBit.radio.enable();");
        }
        if ( codePreprocess.isAccelerometerUsed() ) {
            nlIndent();
            sb.append("uBit.accelerometer.updateSample();");
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
            sb.append("null");
            return null;
        }
        String methodName = "findFirstOccurrenceOfElementInArray(";
        if ( indexOfFunct.getLocation() != IndexLocation.FIRST ) {
            methodName = "findLastOccurrenceOfElementInArray(";
        }
        sb.append(methodName);
        if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            sb.append("(double) ");
        } else if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.STRING ) {
            sb.append("(ManagedString) ");
        }
        indexOfFunct.getParam().get(1).visit(this);
        sb.append(", ");
        indexOfFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            sb.append("null");
            return null;
        }
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            lengthOfIsEmptyFunct.getParam().get(0).visit(this);
            sb.append(".empty()");
        } else {
            sb.append("((int) ");
            lengthOfIsEmptyFunct.getParam().get(0).visit(this);
            sb.append(".size())");
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            sb.append("null");
            return null;
        }
        listGetIndex.getParam().get(0).visit(this);
        sb.append("[");
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FIRST:
                sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                sb.append(" - 1");
                break;
            case RANDOM:
                sb.append("uBit.random(");
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                sb.append(")");
                break;
        }
        sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            return null;
        }
        listSetIndex.getParam().get(0).visit(this);
        sb.append("[");
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FIRST:
                sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                sb.append(" - 1");
                break;
            case RANDOM:
                sb.append("uBit.random(");
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                sb.append(")");
                break;
        }
        sb.append("]");
        sb.append(" = ");
        listSetIndex.getParam().get(1).visit(this);
        sb.append(";");
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
                sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(", 2) == 0");
                break;
            case ODD:
                sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(", 2) != 0");
                break;
            case PRIME:
                break;
            case WHOLE:
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case POSITIVE:
                sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" > 0");
                break;
            case NEGATIVE:
                sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(",");
                mathNumPropFunct.getParam().get(1).visit(this);
                sb.append(") == 0");
                break;
            default:
                break;
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        final FunctionNames functName = mathOnListFunct.getFunctName();
        if ( functName == FunctionNames.STD_DEV ) {
            sb.append("standardDeviation(");
        } else if ( functName == FunctionNames.RANDOM ) {
            sb.append("randomElement(");
        } else {
            sb.append(functName.toString().toLowerCase());
        }
        mathOnListFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        sb.append("((double) rand() / (RAND_MAX))");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        sb.append("(uBit.random(");
        mathRandomIntFunct.getParam().get(1).visit(this);
        sb.append(" - ");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(" + 1)");
        sb.append(" + ");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        sb.append("pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        final List<Expr<Void>> parameters = textJoinFunct.getParam().get();
        final int numberOfParameters = parameters.size();
        for ( int i = 0; i < numberOfParameters; i++ ) {
            sb.append("ManagedString(");
            parameters.get(i).visit(this);
            sb.append(")");
            if ( i < (numberOfParameters - 1) ) {
                sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        final List<Expr<Void>> parameters = methodVoid.getParameters().get();
        appendTemplateIfArrayParameter(parameters);
        super.visitMethodVoid(methodVoid);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        final List<Expr<Void>> parameters = methodReturn.getParameters().get();
        appendTemplateIfArrayParameter(parameters);
        super.visitMethodReturn(methodReturn);
        return null;
    }

    private void appendTemplateIfArrayParameter(List<Expr<Void>> parameters) {
        final boolean isContainedArrayParameter = parameters.stream().filter(p -> p.getVarType().isArray()).findFirst().isPresent();
        if ( isContainedArrayParameter ) {
            sb.append("\ntemplate<size_t N>");
        }
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        sb.append("MicroBitImage(\"" + predefinedImage.getImageName().getImageString() + "\")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        String end = ");";
        sb.append("uBit.display.");
        if ( displayImageAction.getDisplayImageMode().name().equals("ANIMATION") ) {
            sb.append("animateImages(");
            end = ", 200);";
        } else {
            sb.append("print(");
        }
        displayImageAction.getValuesToDisplay().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().visit(this);
        sb.append(".shiftImage" + capitalizeFirstLetter(imageShiftFunction.getShiftDirection().toString()) + "(");
        imageShiftFunction.getPositions().visit(this);
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
    public Void visitImage(Image<Void> image) {
        sb.append("MicroBitImage(\"");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                sb.append(map(Integer.parseInt(pixel), 0, 9, 0, 255));
                if ( j < 4 ) {
                    sb.append(",");
                }
            }
            sb.append("\\n");
        }
        sb.append("\")");
        return null;
    }

    @Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        sb.append(
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
        sb.append("uBit.rgb.setColour(");
        ledOnAction.getLedColor().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        sb.append("uBit.radio.setTransmitPower(" + radioSendAction.getPower() + ");\n");
        sb.append("uBit.radio.datagram.send(");
        radioSendAction.getMsg().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        sb.append("ManagedString(uBit.radio.datagram.recv())");
        return null;
    };

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        sb.append("uBit.radio.setGroup(");
        radioSetChannelAction.getChannel().visit(this);
        sb.append(");\n");
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        sb.append("uBit.radio.getRSSI()");
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
            sb.append("uBit.accelerometer.getStrength()");
        } else {
            sb.append(String.format("uBit.accelerometer.get%s()", accelerometerSensor.getAccelerationDirection()));
        }
        return null;
    }

    @Override
    public Void visitAccelerometerOrientationSensor(AccelerometerOrientationSensor<Void> accelerometerOrientationSensor) {
        sb.append(String.format("uBit.accelerometer." + accelerometerOrientationSensor.getAccelerationOrientationMode().getCppCode()));
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        sb.append("MicroBitColor(");
        rgbColor.getR().visit(this);
        sb.append(", ");
        rgbColor.getG().visit(this);
        sb.append(", ");
        rgbColor.getB().visit(this);
        sb.append(", ");
        rgbColor.getA().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        sb.append("uBit.display.setBrightness((");
        displaySetBrightnessAction.getBrightness().visit(this);
        sb.append(") * 255.0 / 9.0);");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        sb.append("round(uBit.display.getBrightness() * 9.0 / 255.0)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        sb.append("uBit.display.image.setPixelValue(");
        displaySetPixelAction.getX().visit(this);
        sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        sb.append(" * 255.0 / 9.0);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        sb.append("round(uBit.display.image.getPixelValue(");
        displayGetPixelAction.getX().visit(this);
        sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        sb.append(") * 9.0 / 255.0)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        addIncludes();
        generateSignaturesOfUserDefinedMethods();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            nlIndent();
            sb.append("release_fiber();");
            sb.append("\n}\n");
            generateUserDefinedMethods();
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

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (((x - in_min) * (out_max - out_min)) / (in_max - in_min)) + out_min;
    }

    private void arrayLen(Var<Void> arr) {
        sb.append(arr.getValue() + ".size()");
    }

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            sb.append("scroll(");
        } else {
            sb.append("print(");
        }
    }

    private String wrapInManageStringToDisplay(DisplayTextAction<Void> displayTextAction, String ending) {
        sb.append("ManagedString(");
        displayTextAction.getMsg().visit(this);
        ending += ")";
        return ending;
    }

    private String capitalizeFirstLetter(String original) {
        if ( (original == null) || (original.length() == 0) ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    private void addIncludes() {
        sb.append("#define _GNU_SOURCE\n\n");
        sb.append("#include \"MicroBit.h\" \n");
        sb.append("#include <array>\n");
        sb.append("#include <stdlib.h>\n");
        sb.append("MicroBit uBit;\n\n");
    }

    @Override
    protected void generateSignaturesOfUserDefinedMethods() {
        for ( final Method<Void> phrase : userDefinedMethods ) {
            appendTemplateIfArrayParameter(phrase.getParameters().get());
            nlIndent();
            sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()) + " ");
            sb.append(phrase.getMethodName() + "(");
            phrase.getParameters().visit(this);
            sb.append(");");
            nlIndent();
        }
    }
}