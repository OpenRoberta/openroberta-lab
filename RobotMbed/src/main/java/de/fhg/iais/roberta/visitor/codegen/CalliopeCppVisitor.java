package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.w3c.dom.css.RGBColor;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
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
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.MbedUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code for Calliope systems.</b> <br>
 */
public final class CalliopeCppVisitor extends AbstractCppVisitor implements IMbedVisitor<Void> {
    private final MbedUsedHardwareCollectorVisitor codePreprocess;
    private final Configuration configuration;
    ArrayList<VarDeclaration<Void>> usedVars;

    /**
     * initialize the C++ code generator visitor for tests. <b>Will be removed in the future, if tests with textual representations of Nepo (instead of
     * blockly-based graphical representation have been finished.</b>
     *
     * @param programPhrases
     */
    public CalliopeCppVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases, 0);
        this.codePreprocess = null;
        this.configuration = null;
    }

    /**
     * initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private CalliopeCppVisitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
        this.configuration = brickConfiguration;
        this.codePreprocess = new MbedUsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

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
    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        final CalliopeCppVisitor astVisitor = new CalliopeCppVisitor(brickConfiguration, programPhrases, 0);
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
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
        if ( var.getTypeVar().isArray() && var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(" &");
        }
        this.sb.append(whitespace() + var.getName());
        return null;
    }

    protected Void generateUsedVars() {
        for ( final VarDeclaration<Void> var : this.usedVars ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
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
        } else if ( op == Op.NEQ ) {
            this.sb.append("!( ");
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        final String sym;
        if ( op.equals(Op.TEXT_APPEND) ) {
            sym = "=";
        } else if ( op.equals(Op.NEQ) ) {
            sym = "==";
        } else {
            sym = getBinaryOperatorSymbol(op);
        }

        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                generateSubExpr(this.sb, false, binary.getLeft(), binary);
                this.sb.append(" + ManagedString(");
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
            case NEQ:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(" )");
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
            case NUMBER:
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
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String port = lightStatusAction.getPort();
        switch ( port ) {
            case "0":
                this.sb.append("_uBit.rgb.off();");
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                this.sb.append("_cbSetRGBLed(_buf, &_i2c, ").append(port).append(", 0);");
                break;
            default:
                throw new DbcException("LedOffAction; invalid port: " + port);
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        toneAction.getFrequency().visit(this);
        this.sb.append("); ").append("_uBit.sleep(");
        toneAction.getDuration().visit(this);
        this.sb.append("); ").append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("); ").append("_uBit.sleep(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("); ").append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        switch ( port ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(port).append(", ");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(");");
                break;
            case "3":
                this.sb.append("_cbSetMotors(_buf, &_i2c, ");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(", ");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(");");
                break;
            case "AB":
                this.sb.append("_uBit.soundmotor.motorAOn(");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(");");
                nlIndent();
                port = "B";
            case "A":
            case "B":
                this.sb.append("_uBit.soundmotor.motor").append(port).append("On(");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("visitMotorOnAction; Invalide motor port: " + port);
        }
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
        String port = motorStopAction.getUserDefinedPort();
        switch ( port ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(port).append(", 0);");
                break;
            case "3":
                this.sb.append("_cbSetMotors(_buf, &_i2c, 0, 0);");
                break;
            case "AB":
                this.sb.append("_uBit.soundmotor.motorAOff()");
                nlIndent();
                port = "B";
            case "A":
            case "B":
                this.sb.append("_uBit.soundmotor.motor").append(port).append("Off();");
                break;
            default:
                throw new DbcException("visitMotorStopAction; Invalide motor port: " + port);
        }
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
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        String userDefined = keysSensor.getPort();
        String port = this.configuration.getConfigurationComponent(userDefined).getPortName();
        this.sb.append("_uBit.button").append(port).append(".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("(_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
        String mode = gestureSensor.getMode();
        if ( (mode.equals(SC.UP)) || (mode.equals(SC.DOWN)) || (mode.equals(SC.LEFT)) || (mode.equals(SC.RIGHT)) ) {
            this.sb.append("TILT_");
        }
        this.sb.append(mode + ")");
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
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort();
        switch ( port ) {
            case "1":
                this.sb.append("(_uBit.io.P2.readPulseHigh() * 0.017)");
                break;
            case "2":
                this.sb.append("_cbGetSampleUltrasonic(_buf, &_i2c)");
                break;
            default:
                throw new DbcException("UltrasonicSensor; Invalid ultrasonic port: " + port);
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort();
        if ( port.equals("1") || port.contentEquals("2") ) {
            this.sb.append("_cbGetSampleInfrared(_buf, &_i2c, ").append(port).append(")");
        } else {
            throw new DbcException("InfraredSensor; Invalid infrared port: " + port);
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String userDefined = gyroSensor.getPort();
        String port = this.configuration.getConfigurationComponent(userDefined).getPortName();
        this.sb.append("_uBit.accelerometer.get").append(port).append("()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("( _uBit.systemTime() - _initTime )");
                break;
            case SC.RESET:
                this.sb.append("_initTime = _uBit.systemTime();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        String userDefinedName = pinTouchSensor.getPort();
        String port = this.configuration.getConfigurationComponent(userDefinedName).getPortName();
        this.sb.append("_uBit.io." + port + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        String userDefinedName = pinValueSensor.getPort();
        String port = this.configuration.getConfigurationComponent(userDefinedName).getPortName();
        String mode = pinValueSensor.getMode();
        this.sb.append("_uBit.io." + port);
        switch ( mode ) {
            case SC.DIGITAL:
                this.sb.append(".getDigitalValue()");
                break;
            case SC.ANALOG:
                this.sb.append(".getAnalogValue()");
                break;
            case SC.PULSEHIGH:
                this.sb.append(".readPulseHigh()");
                break;
            case SC.PULSELOW:
                this.sb.append(".readPulseLow()");
                break;
            default:
                throw new DbcException("Value type  " + mode + " is not supported.");
        }
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        String userDefinedName = pinWriteValueSensor.getPort();
        String port = this.configuration.getConfigurationComponent(userDefinedName).getPortName();
        String valueType = pinWriteValueSensor.getMode().equals(SC.DIGITAL) ? "DigitalValue(" : "AnalogValue(";
        this.sb.append("_uBit.io.").append(port).append(".set").append(valueType);
        pinWriteValueSensor.getValue().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        String userDefinedName = pinSetPullAction.getPort();
        String port = this.configuration.getConfigurationComponent(userDefinedName).getPortName();
        String mode = pinSetPullAction.getMode();
        this.sb.append("_uBit.io." + port + ".setPull(Pull").append(WordUtils.capitalizeFully(mode)).append(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
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
        if ( this.codePreprocess.isCalliBotUsed() ) {
            nlIndent();
            this.sb.append("_cbInit(_buf, &_i2c, &_uBit);");
        }
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
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        throw new DbcException("Not supported list function " + listRepeat.getClass().getName());
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = "_getFirstOccuranceOfElement(";
        if ( indexOfFunct.getLocation() != IndexLocation.FIRST ) {
            methodName = "_getLastOccuranceOfElement(";
        }
        this.sb.append(methodName);

        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
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
                this.sb.append("isPrimeD(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case WHOLE:
                this.sb.append("isWholeD(");
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
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        this.sb.append("MicroBitImage(\"" + predefinedImage.getImageName().getImageString() + "\")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        String end = ");";
        if ( displayImageAction.getDisplayImageMode().name().equals("ANIMATION") ) {
            try {
                Expr<Void> values = displayImageAction.getValuesToDisplay();
                int valuesSize = ((ListCreate<Void>) values).getValue().get().size();
                this.sb.append("std::array<MicroBitImage, " + valuesSize + "> _animation = _convertToArray<MicroBitImage, " + valuesSize + ">(");
                displayImageAction.getValuesToDisplay().visit(this);
                this.sb.append(");");
                nlIndent();
            } catch ( Exception e ) {
                this.sb.append("for (MicroBitImage& image : ");
                displayImageAction.getValuesToDisplay().visit(this);
                this.sb.append(") {");
                this.sb.append("_uBit.display.print(image, 0, 0, 255, 200);");
                this.sb.append("_uBit.display.clear();");
                this.sb.append("}");
                return null;
            }
        }
        this.sb.append("_uBit.display.");
        if ( displayImageAction.getDisplayImageMode().name().equals("ANIMATION") ) {
            this.sb.append("animateImages(_animation, 200);");
            return null;
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
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb
            .append(
                "MicroBitColor("
                    + colorConst.getRedChannelInt()
                    + ", "
                    + colorConst.getGreenChannelInt()
                    + ", "
                    + colorConst.getBlueChannelInt()
                    + ", "
                    + 255
                    + ")");
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        String port = ledOnAction.getPort();
        switch ( port ) {
            case "0":
                this.sb.append("_uBit.rgb.setColour(");
                ledOnAction.getLedColor().visit(this);
                this.sb.append(");");
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                this.sb.append("_cbSetRGBLed(_buf, &_i2c, ").append(port).append(", ");
                ledOnAction.getLedColor().visit(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("LedOnAction; invalid port: " + port);
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String mode = lightAction.getMode().getValues()[0];
        if ( mode.equals(BlocklyConstants.HIGH) ) {
            mode = "1";
        } else if ( mode.equals("LOW") ) {
            mode = "0";
        } else {
            throw new DbcException("LightAction; invalid mode: " + mode);
        }
        this.sb.append("_cbSetLed(_buf, &_i2c, _cbLedState, ").append(lightAction.getPort()).append(", ").append(mode).append(");");
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

            default:
                throw new IllegalArgumentException("unhandled type");
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        switch ( radioReceiveAction.getType() ) {
            case BOOLEAN:
            case NUMBER:
                this.sb.append("atoi((char*)_uBit.radio.datagram.recv().getBytes())");
                break;
            case STRING:
                this.sb.append("ManagedString(_uBit.radio.datagram.recv())");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

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
        if ( accelerometerSensor.getPort().equals("STRENGTH") ) {
            this.sb.append("Strength");
        } else {
            this.sb.append(accelerometerSensor.getPort());
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
        this.sb.append(", (");
        displaySetPixelAction.getBrightness().visit(this);
        this.sb.append(") * _SET_BRIGHTNESS_MULTIPLIER);");
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
        this.sb.append("_fdd.show(");
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
        this.sb.append("_fdd.clear();");
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction<Void> ledBarSetAction) {
        this.sb.append("_ledBar.setLed(");
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
            if ( this.codePreprocess.isCalliBotUsed() ) {
                nlIndent();
                this.sb.append("_cbStop(_buf, &_i2c);");
            }
            nlIndent();
            this.sb.append("release_fiber();");
            decrIndentation();
            nlIndent();
            this.sb.append("}");
            nlIndent();
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
                return "std::list<>";
            case ARRAY_NUMBER:
                return "std::list<double>";
            case ARRAY_STRING:
                return "std::list<ManagedString>";
            case ARRAY_BOOLEAN:
                return "std::list<bool>";
            case ARRAY_IMAGE:
                return "std::list<MicroBitImage>";
            case ARRAY_COLOUR:
                return "std::list<MicroBitColor>";
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
        this.sb.append("#include \"MicroBit.h\"\n");
        this.sb.append("#include \"NEPODefs.h\"\n");

        if ( this.codePreprocess.isFourDigitDisplayUsed() ) {
            this.sb.append("#include \"FourDigitDisplay.h\"\n");
        }
        if ( this.codePreprocess.isLedBarUsed() ) {
            this.sb.append("#include \"Grove_LED_Bar.h\"\n");
        }
        if ( this.codePreprocess.isHumidityUsed() ) {
            this.sb.append("#include \"Sht31.h\"\n");
        }
        this.sb.append("#include <list>\n");
        this.sb.append("#include <array>\n");
        this.sb.append("#include <stdlib.h>\n");
        this.sb.append("MicroBit _uBit;\n");
        if ( this.codePreprocess.isFourDigitDisplayUsed() ) {
            this.sb.append("FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);\n"); // Only works on the right UART Grove connector
        }
        if ( this.codePreprocess.isLedBarUsed() ) {
            this.sb.append("Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n"); // Only works on the right UART Grove connector; Clock/Data pins are swapped compared to 4DigitDisplay
        }
        if ( this.codePreprocess.isHumidityUsed() ) {
            this.sb.append("Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n");
        }
        if ( this.codePreprocess.isCalliBotUsed() ) {
            this.sb.append("MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);");
            nlIndent();
            this.sb.append("char _buf[5] = { 0, 0, 0, 0, 0 };");
            nlIndent();
            this.sb.append("uint8_t _cbLedState = 0x00;");
        }
    }

    @Override
    protected void generateSignaturesOfUserDefinedMethods() {
        for ( final Method<Void> phrase : this.userDefinedMethods ) {
            nlIndent();
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()));
            this.sb.append(" " + phrase.getMethodName() + "(");
            phrase.getParameters().visit(this);
            this.sb.append(");");
            nlIndent();
        }
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        if ( bothMotorsOnAction.getPortA().contentEquals(BlocklyConstants.A) ) {
            this.sb.append("_uBit.soundmotor.motorAOn(");
            bothMotorsOnAction.getSpeedA().visit(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorBOn(");
            bothMotorsOnAction.getSpeedB().visit(this);
            this.sb.append(");");
        } else if ( bothMotorsOnAction.getPortA().equals("LEFT") ) {
            this.sb.append("_cbSetMotors(_buf, &_i2c, ");
            bothMotorsOnAction.getSpeedA().visit(this);
            this.sb.append(", ");
            bothMotorsOnAction.getSpeedB().visit(this);
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        this.sb.append("_uBit.soundmotor.motorAOff();");
        nlIndent();
        this.sb.append("_uBit.soundmotor.motorBOff();");
        return null;
    }

    /*
     * TODO: I don't know why I am doing this, but it seems that without this a semicolon is lost, somehow... Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        this.sb.append(";");
        return null;
    }

    /*
     * TODO: There is something wrong with semicolon generation for calliope. Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( ((ListElementOperations) listGetIndex.getElementOperation()).equals(ListElementOperations.REMOVE) ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        writeToSerial(serialWriteAction.getValue());
        return null;
    }

    private void writeToSerial(Expr<Void> valueToWrite) {
        if ( valueToWrite instanceof RgbColor<?>
            || valueToWrite instanceof ColorConst<?>
            || (valueToWrite instanceof Var && ((Var<Void>) valueToWrite).getVarType().equals(BlocklyType.COLOR)) ) {
            this.sb.append("_uBit.serial.setTxBufferSize(ManagedString(_castColorToString(");
            valueToWrite.visit(this);
            this.sb.append(")).length() + 2);");
            nlIndent();
            this.sb.append("_uBit.serial.send(");
            this.sb.append("_castColorToString(");
            valueToWrite.visit(this);
            this.sb.append(")");
        } else {
            this.sb.append("_uBit.serial.setTxBufferSize(ManagedString((");
            valueToWrite.visit(this);
            this.sb.append(")).length() + 2);");
            nlIndent();
            this.sb.append("_uBit.serial.send(");
            this.sb.append("ManagedString(");
            valueToWrite.visit(this);
            this.sb.append(")");
        }
        this.sb.append(" + \"\\r\\n\", MicroBitSerialMode::ASYNC);");
        nlIndent();
        // give serial some time
        this.sb.append("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        if ( humiditySensor.getMode().equals(SC.HUMIDITY) ) {
            this.sb.append("_sht31.readHumidity()");
        } else if ( humiditySensor.getMode().equals(SC.TEMPERATURE) ) {
            this.sb.append("_sht31.readTemperature()");
        } else {
            throw new UnsupportedOperationException("Mode " + humiditySensor.getMode() + " not supported!");
        }
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction<Void> switchLedMatrixAction) {
        if ( switchLedMatrixAction.isActivated() ) {
            this.sb.append("_uBit.display.enable();");
        } else {
            this.sb.append("_uBit.display.disable();");
        }
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        writeToSerial(debugAction.getValue());
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        if ( ((Binary<Void>) assertStmt.getAssert()).getLeft().getVarType().equals(BlocklyType.COLOR) ) {
            this.sb.append("assertNepo((");
            assertStmt.getAssert().visit(this);
            this.sb.append("), \"").append(assertStmt.getMsg()).append("\", \"");
            ((Binary<Void>) assertStmt.getAssert()).getLeft().visit(this);
            this.sb.append("\", \"").append(((Binary<Void>) assertStmt.getAssert()).getOp().toString()).append("\", \"");
            ((Binary<Void>) assertStmt.getAssert()).getRight().visit(this);
            this.sb.append("\");");
        } else {
            super.visitAssertStmt(assertStmt);
        }
        return null;
    }
}