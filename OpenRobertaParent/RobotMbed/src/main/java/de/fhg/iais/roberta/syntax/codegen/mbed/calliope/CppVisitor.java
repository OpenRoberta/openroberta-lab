package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.mbed.CalliopeConfiguration;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.GestureSensorMode;
import de.fhg.iais.roberta.mode.sensor.PinPull;
import de.fhg.iais.roberta.mode.sensor.PinValue;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
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
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.hardware.mbed.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotCppVisitor;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.LedColor;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
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
    private int numberOfArraysUsedInTemplate;
    private int currentArrayInTemplate;

    /**
     * initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private CppVisitor(CalliopeConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        this.codePreprocess = new UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

        this.loopsLabels = this.codePreprocess.getloopsLabelContainer();
        this.userDefinedMethods = this.codePreprocess.getUserDefinedMethods();
        this.usedVars = this.codePreprocess.getVisitedVars();
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
                this.sb.append("M_GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                this.sb.append("M_SQRT1_2");
                break;
            case INFINITY:
                this.sb.append("M_INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        //TODO there must be a way to make this code simpler
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
        if ( var.getTypeVar().isArray() ) {
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                final ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                this.sb.append(list.getValue().get().size() + ">");
            } else {
                if ( this.numberOfArraysUsedInTemplate != this.currentArrayInTemplate ) {
                    this.sb.append("N" + this.currentArrayInTemplate + "> ");
                    this.currentArrayInTemplate++;
                } else {
                    this.currentArrayInTemplate = 0;
                    this.sb.append("N" + this.currentArrayInTemplate + "> ");
                    this.currentArrayInTemplate++;
                }
            }
        }
        this.sb.append(whitespace() + var.getName());
        return null;
    }

    protected Void generateUsedVars() {
        for ( final VarDeclaration<Void> var : this.usedVars ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                /*if ( var.getTypeVar().isArray() ) {
                    ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                    this.sb.append(list.getValue().get().size() + ">");
                }*/
                this.sb.append(var.getName());
                this.sb.append(whitespace() + "=" + whitespace());
                var.getValue().visit(this);
                this.sb.append(";");
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        final Op op = binary.getOp();
        if ( op == Op.MOD ) {
            this.sb.append("(int) ");
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);

        final String sym = getBinaryOperatorSymbol(op);
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
            case MOD:
                this.sb.append("((int) ");
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
            this.sb.append("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
        } else {
            appendBreakStmt();
        }

        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("_uBit.sleep(");
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
        final String varType = displayTextAction.getMsg().getVarType().toString();
        this.sb.append("_uBit.display.");
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
        this.sb.append("_uBit.display.clear();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("_uBit.rgb.off();");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        toneAction.getFrequency().visit(this);
        this.sb.append("); ");
        this.sb.append("_uBit.sleep(");
        toneAction.getDuration().visit(this);
        this.sb.append("); ");
        this.sb.append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("); ");
        this.sb.append("_uBit.sleep(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("); ");
        this.sb.append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("_uBit.soundmotor.motor");
        if ( !motorOnAction.getPort().getCodeName().equals("AB") ) {
            this.sb.append(motorOnAction.getPort());
        }
        // fix for IT Gipfel
        else {
            this.sb.append("AOn(");
            motorOnAction.getParam().getSpeed().visit(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorB");
        }
        // fix for IT Gipfel
        this.sb.append("On(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitSingleMotorOnAction(SingleMotorOnAction<Void> singleMotorOnAction) {
        this.sb.append("_uBit.soundmotor.motorOn(");
        singleMotorOnAction.getSpeed().visit(this);
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
        this.sb.append("_uBit.soundmotor.motor");
        if ( motorStopAction.getPort().getCodeName().equals("AB") ) {
            this.sb.append("AOff();");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorB");
        } else {
            this.sb.append(motorStopAction.getPort());
        }
        this.sb.append("Off();");
        return null;
    }

    @Override
    public Void visitSingleMotorStopAction(SingleMotorStopAction<Void> singleMotorStopAction) {
        this.sb.append("_uBit.soundmotor.motor");
        switch ( (MotorStopMode) singleMotorStopAction.getMode() ) {
            case FLOAT:
                this.sb.append("Coast();");
                break;
            case NONFLOAT:
                this.sb.append("Break();");
                break;
            case SLEEP:
                this.sb.append("Sleep();");
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
        this.sb.append("round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        this.sb.append("_uBit.button" + brickSensor.getPort().getCodeName() + ".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("(_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
        if ( (gestureSensor.getMode() == GestureSensorMode.UP)
            || (gestureSensor.getMode() == GestureSensorMode.DOWN)
            || (gestureSensor.getMode() == GestureSensorMode.LEFT)
            || (gestureSensor.getMode() == GestureSensorMode.RIGHT) ) {
            this.sb.append("TILT_");
        }
        this.sb.append(gestureSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("_uBit.thermometer.getTemperature()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("_uBit.compass.heading()");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> microphoneSensor) {
        this.sb.append("_uBit.io.P21.getMicrophoneValue()");
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
        this.sb.append("_uBit.accelerometer.get");
        this.sb.append(gyroSensor.getPort().getCodeName());
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("( _uBit.systemTime() - _initTime )");
                break;
            case RESET:
                this.sb.append("_initTime = _uBit.systemTime();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        String port = pinTouchSensor.getPort().getCodeName();
        this.sb.append("_uBit.io." + port + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        String port = pinValueSensor.getPort().getCodeName();
        this.sb.append("_uBit.io." + port);
        switch ( (PinValue) pinValueSensor.getMode() ) {
            case DIGITAL:
            case ANALOG:
                this.sb.append(".get").append(pinValueSensor.getMode().getValues()[0]);
                break;
            case PULSE_HIGH:
            case PULSE_LOW:
                this.sb.append(".read").append(pinValueSensor.getMode().getValues()[0]);
                break;
            default:
                throw new DbcException("Value type  " + pinValueSensor.getMode() + " is not supported.");
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValue<Void> pinWriteValueSensor) {
        String port = pinWriteValueSensor.getPort().getCodeName();
        if ( pinWriteValueSensor.getPort().getCodeName().equals("4") ) {
            port = "P19";
        } else if ( pinWriteValueSensor.getPort().getCodeName().equals("5") ) {
            port = "P2";
        }
        String valueType = "AnalogValue(";
        if ( pinWriteValueSensor.getMode() == PinValue.DIGITAL ) {
            valueType = "DigitalValue(";
        }
        this.sb.append("_uBit.io." + port + ".set" + valueType);
        pinWriteValueSensor.getValue().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        String port = pinSetPullAction.getPort().getCodeName();
        this.sb.append("_uBit.io." + port + ".setPull(");
        switch ( (PinPull) pinSetPullAction.getMode() ) {
            case UP:
                this.sb.append("PullUp");
                break;
            case DOWN:
                this.sb.append("PullDown");
                break;
            case NONE:
            default:
                this.sb.append("PullNone");
                break;
        }
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
        // TODO check if timer is used in the user program
        if ( this.codePreprocess.isTimerSensorUsed() ) {
            this.sb.append("int _initTime = _uBit.systemTime();");
        }
        mainTask.getVariables().visit(this);
        nlIndent();
        nlIndent();
        this.sb.append("int main()");
        nlIndent();
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        // Initialise the micro:bit runtime.
        this.sb.append("_uBit.init();");
        generateUsedVars();
        nlIndent();
        if ( this.codePreprocess.isGreyScale() ) {
            this.sb.append("_uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        }
        if ( this.codePreprocess.isRadioUsed() ) {
            nlIndent();
            this.sb.append("_uBit.radio.enable();");
        }
        if ( this.codePreprocess.isAccelerometerUsed() ) {
            nlIndent();
            this.sb.append("_uBit.accelerometer.updateSample();");
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
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("_uBit.random(");
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
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("_uBit.random(");
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
                this.sb.append("isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case WHOLE:
                this.sb.append("isWhole(");
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
        final FunctionNames functName = mathOnListFunct.getFunctName();
        if ( functName == FunctionNames.STD_DEV ) {
            this.sb.append("standardDeviation(");
        } else if ( functName == FunctionNames.RANDOM ) {
            this.sb.append("randomElement(");
        } else {
            this.sb.append(functName.toString().toLowerCase() + "(");
        }
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
        this.sb.append("(_uBit.random(");
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
        final List<Expr<Void>> parameters = textJoinFunct.getParam().get();
        final int numberOfParameters = parameters.size();
        for ( int i = 0; i < numberOfParameters; i++ ) {
            this.sb.append("ManagedString(");
            parameters.get(i).visit(this);
            this.sb.append(")");
            if ( i < (numberOfParameters - 1) ) {
                this.sb.append(" + ");
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
        this.numberOfArraysUsedInTemplate = 0;
        final boolean isContainedArrayParameter = parameters.stream().filter(p -> p.getVarType().isArray()).findFirst().isPresent();
        if ( isContainedArrayParameter ) {
            this.sb.append("\ntemplate<");
            int i = 0;
            for ( Expr<Void> expr : parameters ) {
                if ( expr.getVarType().isArray() ) {
                    this.sb.append("size_t N" + i++ + ", ");
                }
            }
            this.sb.delete(this.sb.length() - 2, this.sb.length());
            this.sb.append(">");
            this.numberOfArraysUsedInTemplate = i;
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
        this.sb.append("_uBit.display.");
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
        this.sb.append("_uBit.rgb.setColour(");
        ledOnAction.getLedColor().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        this.sb.append("_uBit.radio.setTransmitPower(" + radioSendAction.getPower() + ");");
        nlIndent();
        switch ( radioSendAction.getType() ) {
            case NUMBER:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.getMsg().visit(this);
                this.sb.append("))");
                break;
            case BOOLEAN:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.getMsg().visit(this);
                this.sb.append(")?true:false)");
                break;
            case STRING:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((");
                radioSendAction.getMsg().visit(this);
                this.sb.append("))");
                break;
            //            case STRING:
            //                this.sb.append("_uBit.radio.datagram.send(");
            //                radioSendAction.getMsg().visit(this);
            //                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        switch ( radioReceiveAction.getType() ) {
            case NUMBER:
                this.sb.append("atoi((char*)_uBit.radio.datagram.recv().getBytes())");
                break;
            case BOOLEAN:
            case STRING:
                this.sb.append("ManagedString(_uBit.radio.datagram.recv())");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    };

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        this.sb.append("_uBit.radio.setGroup(");
        radioSetChannelAction.getChannel().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        this.sb.append("_uBit.radio.getRSSI()");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.sb.append("_uBit.accelerometer.get");
        if ( accelerometerSensor.getPort().getOraName().equals("STRENGTH") ) {
            this.sb.append("Strength");
        } else {
            this.sb.append(accelerometerSensor.getPort().getOraName());
        }
        this.sb.append("()");
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
        this.sb.append("_uBit.display.setBrightness((");
        displaySetBrightnessAction.getBrightness().visit(this);
        this.sb.append(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<Void> displayGetBrightnessAction) {
        this.sb.append("round(_uBit.display.getBrightness() * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        this.sb.append("_uBit.display.image.setPixelValue(");
        displaySetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().visit(this);
        this.sb.append(", ");
        displaySetPixelAction.getBrightness().visit(this);
        this.sb.append(" * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("round(_uBit.display.image.getPixelValue(");
        displayGetPixelAction.getX().visit(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().visit(this);
        this.sb.append(") * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        this.sb.append("fdd.show(");
        fourDigitDisplayShowAction.getValue().visit(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.getPosition().visit(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.getColon().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Void> fourDigitDisplayClearAction) {
        this.sb.append("fdd.clear();");
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        this.sb.append("ledBar.setLed(");
        ledBarSetAction.getX().visit(this);
        this.sb.append(", ");
        ledBarSetAction.getBrightness().visit(this);
        this.sb.append(");");
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
            this.sb.append("release_fiber();");
            this.sb.append("\n}\n");
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
        if ( (original == null) || (original.length() == 0) ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    private void addIncludes() {
        this.sb.append("#define _GNU_SOURCE\n\n");
        this.sb.append("#include \"MicroBit.h\" \n");
        this.sb.append("#include \"NEPODefs.h\"\n");
        if ( this.codePreprocess.isFourDigitDisplayUsed() ) {
            this.sb.append("#include \"FourDigitDisplay.h\"\n");
        }
        if ( this.codePreprocess.isLedBarUsed() ) {
            this.sb.append("#include \"Grove_LED_Bar.h\"\n");
        }
        this.sb.append("#include <array>\n");
        this.sb.append("#include <stdlib.h>\n");
        this.sb.append("MicroBit _uBit;\n\n");
        if ( this.codePreprocess.isFourDigitDisplayUsed() ) {
            this.sb.append("FourDigitDisplay fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);\n"); // Only works on the right UART Grove connector
        }
        if ( this.codePreprocess.isLedBarUsed() ) {
            this.sb.append("Grove_LED_Bar ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n"); // Only works on the right UART Grove connector; Clock/Data pins are swapped compared to 4DigitDisplay
        }
    }

    @Override
    protected void generateSignaturesOfUserDefinedMethods() {
        for ( final Method<Void> phrase : this.userDefinedMethods ) {
            appendTemplateIfArrayParameter(phrase.getParameters().get());
            nlIndent();
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()));
            if ( getLanguageVarTypeFromBlocklyType(phrase.getReturnType()).toString().contains("array") ) {
                this.sb.append("M>");
            }
            this.sb.append(" " + phrase.getMethodName() + "(");
            phrase.getParameters().visit(this);
            this.sb.append(");");
            nlIndent();
        }
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        this.sb.append("_uBit.soundmotor.motorAOn(");
        bothMotorsOnAction.getSpeedA().visit(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("_uBit.soundmotor.motorBOn(");
        bothMotorsOnAction.getSpeedB().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        this.sb.append("_uBit.soundmotor.motorAOff();");
        nlIndent();
        this.sb.append("_uBit.soundmotor.motorBOff();");
        return null;
    }
}