package de.fhg.iais.roberta.visitor.codegen;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.text.WordUtils;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
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
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code for Calliope systems.</b> <br>
 */
public final class CalliopeCppVisitor extends AbstractCppVisitor implements ICalliopeVisitor<Void> {
    private static final Map<String, String> PIN_MAP = new HashMap<>(); // TODO better?
    private static final Map<String, String> CALLIBOT_TO_PIN_MAP = new HashMap<>();

    static {
        PIN_MAP.put("0", "P12");
        PIN_MAP.put("1", "P0");
        PIN_MAP.put("2", "P1");
        PIN_MAP.put("3", "P16");
        PIN_MAP.put("4", "P19");
        PIN_MAP.put("5", "P2");
        PIN_MAP.put("C04", "P3");
        PIN_MAP.put("C05", "P4");
        PIN_MAP.put("C06", "P10");
        PIN_MAP.put("C07", "P13");
        PIN_MAP.put("C08", "P14");
        PIN_MAP.put("C09", "P15");
        PIN_MAP.put("C10", "P9");
        PIN_MAP.put("C11", "P7");
        PIN_MAP.put("C12", "P6");
        PIN_MAP.put("C16", "P2");
        PIN_MAP.put("C17", "P8");
        PIN_MAP.put("C18", "P20");
        PIN_MAP.put("C19", "P19");

        CALLIBOT_TO_PIN_MAP.put("MOTOR_L", "0");
        CALLIBOT_TO_PIN_MAP.put("MOTOR_R", "2");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_LF", "1");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_RF", "4");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_LR", "2");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_RR", "3");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_A", "5");
        CALLIBOT_TO_PIN_MAP.put("LED_L", "1");
        CALLIBOT_TO_PIN_MAP.put("LED_R", "2");
        CALLIBOT_TO_PIN_MAP.put("LED_B", "3");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_L", "2");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_R", "1");
        CALLIBOT_TO_PIN_MAP.put("ULTRASONIC", "2");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S1", "0x14");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S2", "0x15");
    }

    private final ConfigurationAst robotConfiguration;

    /**
     * initialize the C++ code generator visitor.
     *
     * @param robotConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public CalliopeCppVisitor(List<List<Phrase>> programPhrases, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.robotConfiguration = robotConfiguration;
    }

    private static String getCallibotPin(ConfigurationComponent confComp, String port) {
        String s =
            confComp
                .getComponentProperties()
                .entrySet()
                .stream()
                .filter(entry -> port.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new DbcException("Invalid port!"));
        return CALLIBOT_TO_PIN_MAP.get(s);
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        this.sb.append("ManagedString(");
        super.visitStringConst(stringConst);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.typeVar));
        if ( var.typeVar.isArray() && var.value.getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(" &");
        }
        this.sb.append(whitespace() + var.getCodeSafeName());
        return null;
    }

    private Void generateUsedVars() {
        for ( final VarDeclaration var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                this.sb.append("___" + var.name);
                this.sb.append(whitespace() + "=" + whitespace());
                var.value.accept(this);
                this.sb.append(";");
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        final Op op = binary.op;
        if ( op == Op.MOD ) {
            this.sb.append("(int) ");
        } else if ( op == Op.NEQ ) {
            this.sb.append("!( ");
        }
        generateSubExpr(this.sb, false, binary.left, binary);
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
                generateSubExpr(this.sb, false, binary.left, binary);
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
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
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
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        final boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.mode ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.expr);
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.expr);
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case FOR_EACH:
                increaseLoopCounter();
                generateCodeFromStmtCondition("for", repeatStmt.expr);
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.list.accept(this);
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
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("_uBit.sleep(");
        waitTimeStmt.time.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        String ending = ")";
        final String varType = displayTextAction.msg.getVarType().toString();
        this.sb.append("_uBit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !varType.equals("STRING") ) {
            ending = wrapInManageStringToDisplay(displayTextAction, ending);
        } else {
            displayTextAction.msg.accept(this);
        }

        this.sb.append(ending + ";");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.sb.append("_uBit.display.clear();");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        String port = lightStatusAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : "0";
        switch ( pin1 ) {
            case "0":
                this.sb.append("_uBit.rgb.off();");
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                this.sb.append("_cbSetRGBLed(_buf, &_i2c, ").append(pin1).append(", 0);");
                break;
            default:
                throw new DbcException("LedOffAction; invalid port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        toneAction.frequency.accept(this);
        this.sb.append("); ").append("_uBit.sleep(");
        toneAction.duration.accept(this);
        this.sb.append("); ").append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        this.sb.append(playNoteAction.frequency);
        this.sb.append("); ").append("_uBit.sleep(");
        this.sb.append(playNoteAction.duration);
        this.sb.append("); ").append("_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        switch ( pin1 ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(pin1).append(", ");
                motorOnAction.param.getSpeed().accept(this);
                this.sb.append(");");
                break;
            case "A":
            case "B":
                this.sb.append("_uBit.soundmotor.motor");
                if ( isDualMode() ) {
                    this.sb.append(pin1);
                }
                this.sb.append("On(");
                motorOnAction.param.getSpeed().accept(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("visitMotorOnAction; Invalid motor port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        switch ( pin1 ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(pin1).append(", 0);");
                break;
            case "A":
            case "B":
                if ( isDualMode() ) {
                    this.sb.append("_uBit.soundmotor.motor").append(pin1).append("Off();"); // Coast vs OFF
                    this.sb.append("//float, break and sleep doesn't work with more than one motor connected");
                } else {
                    this.sb.append("_uBit.soundmotor.motor");
                    switch ( (MotorStopMode) motorStopAction.mode ) {
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
                            throw new DbcException("Invalide stop mode " + motorStopAction.mode);
                    }
                }
                break;
            default:
                throw new DbcException("visitMotorStopAction; Invalide motor port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.sb.append("round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = keysSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.sb.append("_uBit.button").append(pin1).append(".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        String mode = gestureSensor.getMode();
        if ( mode.equals("SHAKE") ) {
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.IS_GESTURE_SHAKE));
            this.sb.append("()");
        } else {
            this.sb.append("(_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
            if ( mode.equals(SC.UP) || mode.equals(SC.DOWN) || mode.equals(SC.LEFT) || mode.equals(SC.RIGHT) ) {
                this.sb.append("TILT_");
            }
            this.sb.append(mode + ")");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.sb.append("_uBit.thermometer.getTemperature()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        this.sb.append("_uBit.compass.heading()");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor microphoneSensor) {
        this.sb.append("_uBit.io.P21.getMicrophoneValue()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String port = ultrasonicSensor.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        boolean isCallibot = confComp.componentType.equals("CALLIBOT");
        if ( isCallibot ) {
            this.sb.append("_cbGetSampleUltrasonic(_buf, &_i2c)");
        } else {
            this.sb.append("(_uBit.io.P2.readPulseHigh() * 0.017)");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        if ( pin1.equals("1") || pin1.contentEquals("2") ) {
            this.sb.append("_cbGetSampleInfrared(_buf, &_i2c, ").append(pin1).append(")");
        } else {
            throw new DbcException("InfraredSensor; Invalid infrared port: " + port);
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        String slot = gyroSensor.getSlot();
        if ( slot.equals("X") ) { // TODO rename to Pitch and Roll in the configuration?
            this.sb.append("_uBit.accelerometer.getPitch()");
        } else if ( slot.equals("Y") ) {
            this.sb.append("_uBit.accelerometer.getRoll()");
        } else {
            throw new DbcException("Slot " + slot + " is not valid!");
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.sb.append("( _uBit.systemTime() - _initTime )");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.sb.append("_initTime = _uBit.systemTime();");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        this.sb.append("_uBit.io." + PIN_MAP.get(pinTouchSensor.getUserDefinedPort()) + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        String port = pinValueSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String mode = pinValueSensor.getMode();
        this.sb.append("_uBit.io." + PIN_MAP.get(pin1));
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
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        String port = mbedPinWriteValueAction.port;
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String valueType = mbedPinWriteValueAction.pinValue.equals(SC.DIGITAL) ? "DigitalValue(" : "AnalogValue(";
        this.sb.append("_uBit.io.").append(PIN_MAP.get(pin1)).append(".set").append(valueType);
        mbedPinWriteValueAction.value.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            this.sb.append("int _initTime = _uBit.systemTime();");
        }
        mainTask.variables.accept(this);
        nlIndent();
        nlIndent();
        this.sb.append("int main()");
        nlIndent();
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        // Initialise the micro:bit runtime.
        this.sb.append("_uBit.init();");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.CALLIBOT) ) {
            nlIndent();
            this.sb.append("_cbInit(_buf, &_i2c, &_uBit);");
        }
        if ( this.robotConfiguration.isComponentTypePresent(SC.DIGITAL_PIN) ) {
            for ( ConfigurationComponent usedConfigurationBlock : this.robotConfiguration.getConfigurationComponentsValues() ) {
                if ( usedConfigurationBlock.componentType.equals(SC.DIGITAL_PIN) ) {
                    String pin1 = usedConfigurationBlock.getProperty("PIN1");
                    String mode = usedConfigurationBlock.getProperty("PIN_PULL");
                    if ( mode.equals("PIN_PULL_UP") ) {
                        mode = "UP";
                    } else if ( mode.equals("PIN_PULL_DOWN") ) {
                        mode = "DOWN";
                    } else {
                        continue;
                    }
                    nlIndent();
                    this.sb.append("_uBit.io." + PIN_MAP.get(pin1) + ".setPull(Pull").append(WordUtils.capitalizeFully(mode)).append(");");
                }
            }
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COLOR) ) {
            String integrationTime = "2_4MS";
            String gain = "1X";
            for ( ConfigurationComponent usedConfigurationBlock : this.robotConfiguration.getConfigurationComponentsValues() ) {
                if ( usedConfigurationBlock.componentType.equals(SC.COLOUR) ) {
                    integrationTime = usedConfigurationBlock.getProperty("I_TIME");
                    gain = usedConfigurationBlock.getProperty("GAIN");
                    break;
                }
            }
            nlIndent();
            this.sb.append("_TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_").append(integrationTime).append(", TCS3472_GAIN_").append(gain).append(");");
            nlIndent();
            this.sb.append("_TCS3472_time = TCS3472_INTEGRATIONTIME_").append(integrationTime).append(";");
        }

        generateUsedVars();

        nlIndent();

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DISPLAY_GRAYSCALE) ) {
            this.sb.append("_uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            nlIndent();
            this.sb.append("_uBit.radio.enable();");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.ACCELEROMETER) || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COMPASS) ) {
            nlIndent();
            this.sb.append("_uBit.accelerometer.updateSample();");
        }
        if ( this.isDualMode() && this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            this.sb.append("nrf_gpiote_task_configure(0, CALLIOPE_PIN_MOTOR_IN2, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_HIGH);");
            nlIndent();
            this.sb.append("nrf_gpiote_task_configure(1, CALLIOPE_PIN_MOTOR_IN1, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_LOW);");
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.sb.append("min(max(");
        mathConstrainFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.param.get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.param.get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.sb.append("(_uBit.random(");
        mathRandomIntFunct.param.get(1).accept(this);
        this.sb.append(" - ");
        mathRandomIntFunct.param.get(0).accept(this);
        this.sb.append(" + 1)");
        this.sb.append(" + ");
        mathRandomIntFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.sb.append("ManagedString(");
        mathCastStringFunct.param.get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.sb.append("ManagedString((char)(");
        mathCastCharFunct.param.get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.sb.append("std::atof((");
        textStringCastNumberFunct.param.get(0).accept(this);
        this.sb.append(").toCharArray())");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.sb.append("(int)(");
        textCharCastNumberFunct.param.get(0).accept(this);
        this.sb.append(".charAt(");
        textCharCastNumberFunct.param.get(1).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        final List<Expr> parameters = textJoinFunct.param.get();
        final int numberOfParameters = parameters.size();
        for ( int i = 0; i < numberOfParameters; i++ ) {
            this.sb.append("ManagedString(");
            parameters.get(i).accept(this);
            this.sb.append(")");
            if ( i < numberOfParameters - 1 ) {
                this.sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        this.sb.append("MicroBitImage(\"" + predefinedImage.getImageName().getImageString() + "\")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        String end = ");";
        if ( displayImageAction.displayImageMode.equals("ANIMATION") ) {
            try {
                Expr values = displayImageAction.valuesToDisplay;
                int valuesSize = ((ListCreate) values).exprList.get().size();
                this.sb.append("std::array<MicroBitImage, " + valuesSize + "> _animation = _convertToArray<MicroBitImage, " + valuesSize + ">(");
                displayImageAction.valuesToDisplay.accept(this);
                this.sb.append(");");
                nlIndent();
            } catch ( Exception e ) {
                this.sb.append("for (MicroBitImage& image : ");
                displayImageAction.valuesToDisplay.accept(this);
                this.sb.append(") {");
                this.sb.append("_uBit.display.print(image, 0, 0, 255, 200);");
                this.sb.append("_uBit.display.clear();");
                this.sb.append("}");
                return null;
            }
        }
        this.sb.append("_uBit.display.");
        if ( displayImageAction.displayImageMode.equals("ANIMATION") ) {
            this.sb.append("animateImages(_animation, 200);");
            return null;
        } else {
            this.sb.append("print(");
        }
        displayImageAction.valuesToDisplay.accept(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        imageShiftFunction.image.accept(this);
        this.sb.append(".shiftImage" + capitalizeFirstLetter(imageShiftFunction.shiftDirection.toString()) + "(");
        imageShiftFunction.positions.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        imageInvertFunction.image.accept(this);
        this.sb.append(".invert()");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        this.sb.append("MicroBitImage(\"");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
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
    public Void visitColorConst(ColorConst colorConst) {
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
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        String port = ledOnAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : "0";
        switch ( pin1 ) {
            case "0":
                this.sb.append("_uBit.rgb.setColour(");
                ledOnAction.ledColor.accept(this);
                this.sb.append(");");
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                this.sb.append("_cbSetRGBLed(_buf, &_i2c, ").append(pin1).append(", ");
                ledOnAction.ledColor.accept(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("LedOnAction; invalid port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        String port = lightAction.port;
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.componentType.equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        String mode = lightAction.mode.getValues()[0];
        if ( mode.equals(BlocklyConstants.HIGH) ) {
            mode = "1";
        } else if ( mode.equals("LOW") ) {
            mode = "0";
        } else {
            throw new DbcException("LightAction; invalid mode: " + mode);
        }
        this.sb.append("_cbSetLed(_buf, &_i2c, _cbLedState, ").append(pin1).append(", ").append(mode).append(");");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        this.sb.append("_uBit.radio.setTransmitPower(" + radioSendAction.power + ");");
        nlIndent();
        switch ( radioSendAction.type ) {
            case "Number":
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.message.accept(this);
                this.sb.append("))");
                break;
            case "Boolean":
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.message.accept(this);
                this.sb.append(")?true:false)");
                break;
            case "String":
                this.sb.append("_uBit.radio.datagram.send(ManagedString((");
                radioSendAction.message.accept(this);
                this.sb.append("))");
                break;

            default:
                throw new IllegalArgumentException("unhandled type");
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        switch ( radioReceiveAction.type ) {
            case "Boolean":
            case "Number":
                this.sb.append("atoi((char*)_uBit.radio.datagram.recv().getBytes())");
                break;
            case "String":
                this.sb.append("ManagedString(_uBit.radio.datagram.recv())");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        this.sb.append("_uBit.radio.setGroup(");
        radioSetChannelAction.channel.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        this.sb.append("_uBit.radio.getRSSI()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.sb.append("_uBit.accelerometer.get");
        if ( accelerometerSensor.getSlot().equals("STRENGTH") ) {
            this.sb.append("Strength");
        } else {
            this.sb.append(accelerometerSensor.getSlot());
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.sb.append("MicroBitColor(");
        rgbColor.R.accept(this);
        this.sb.append(", ");
        rgbColor.G.accept(this);
        this.sb.append(", ");
        rgbColor.B.accept(this);
        this.sb.append(", ");
        rgbColor.A.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        this.sb.append("_uBit.display.setBrightness((");
        displaySetBrightnessAction.brightness.accept(this);
        this.sb.append(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        this.sb.append("round(_uBit.display.getBrightness() * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        this.sb.append("_uBit.display.image.setPixelValue(");
        displaySetPixelAction.x.accept(this);
        this.sb.append(", ");
        displaySetPixelAction.y.accept(this);
        this.sb.append(", (");
        displaySetPixelAction.brightness.accept(this);
        this.sb.append(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        this.sb.append("round(_uBit.display.image.getPixelValue(");
        displayGetPixelAction.x.accept(this);
        this.sb.append(", ");
        displayGetPixelAction.y.accept(this);
        this.sb.append(") * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        this.sb.append("_fdd.show(");
        fourDigitDisplayShowAction.value.accept(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.position.accept(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.colon.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        this.sb.append("_fdd.clear();");
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        this.sb.append("_ledBar.setLed(");
        ledBarSetAction.x.accept(this);
        this.sb.append(", ");
        ledBarSetAction.brightness.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        addIncludes();
        super.generateProgramPrefix(withWrapping);
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.CALLIBOT) ) {
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

        super.generateProgramSuffix(withWrapping);
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
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private void appendTextDisplayType(DisplayTextAction displayTextAction) {
        if ( Objects.equals(displayTextAction.mode, "TEXT") ) {
            this.sb.append("scroll(");
        } else {
            this.sb.append("print(");
        }
    }

    private String wrapInManageStringToDisplay(DisplayTextAction displayTextAction, String ending) {
        this.sb.append("ManagedString(");
        displayTextAction.msg.accept(this);
        ending += ")";
        return ending;
    }

    private String capitalizeFirstLetter(String original) {
        if ( original == null || original.length() == 0 ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    private void addIncludes() {
        this.sb.append("#define _GNU_SOURCE\n\n");
        this.sb.append("#include \"MicroBit.h\"\n");
        this.sb.append("#include \"NEPODefs.h\"\n");

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.FOUR_DIGIT_DISPLAY) ) {
            this.sb.append("#include \"FourDigitDisplay.h\"\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.LED_BAR) ) {
            this.sb.append("#include \"Grove_LED_Bar.h\"\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.HUMIDITY) ) {
            this.sb.append("#include \"Sht31.h\"\n");
        }
        if ( this.isDualMode() && this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            this.sb.append("#include \"nrf_gpiote.h\"\n");
        }
        this.sb.append("#include <list>\n");
        this.sb.append("#include <array>\n");
        this.sb.append("#include <stdlib.h>\n");
        this.sb.append("MicroBit _uBit;\n");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.FOUR_DIGIT_DISPLAY) ) {
            this.sb.append("FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);\n"); // Only works on the right UART Grove connector
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.LED_BAR) ) {
            this.sb.append("Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n"); // Only works on the right UART Grove connector; Clock/Data pins are swapped compared to 4DigitDisplay
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.HUMIDITY) ) {
            this.sb.append("Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.CALLIBOT) ) {
            this.sb.append("MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);");
            nlIndent();
            this.sb.append("char _buf[5] = { 0, 0, 0, 0, 0 };");
            nlIndent();
            this.sb.append("uint8_t _cbLedState = 0x00;");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COLOR) ) {
            this.sb.append("MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);");
            nlIndent();
            this.sb.append("char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };");
            nlIndent();
            this.sb.append("std::list<double> _TCS3472_rgb;");
            nlIndent();
            this.sb.append("MicroBitColor _TCS3472_color;");
            nlIndent();
            this.sb.append("char _TCS3472_time = 0xff;");
        }
    }

    @Override
    protected void generateSignaturesOfUserDefinedMethods() {
        for ( final Method phrase : this.getBean(UsedHardwareBean.class).getUserDefinedMethods() ) {
            nlIndent();
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()));
            this.sb.append(" " + phrase.getCodeSafeMethodName() + "(");
            phrase.getParameters().accept(this);
            this.sb.append(");");
            nlIndent();
        }
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        String portA = bothMotorsOnAction.portA;
        String portB = bothMotorsOnAction.portB;
        ConfigurationComponent confCompA = this.robotConfiguration.getConfigurationComponent(portA);
        ConfigurationComponent confCompB = this.robotConfiguration.getConfigurationComponent(portB);

        if ( confCompA.componentType.equals("CALLIBOT") ) {
            this.sb.append("_cbSetMotors(_buf, &_i2c, ");
            if ( confCompA.getProperty("MOTOR_L").equals(portA) ) {
                bothMotorsOnAction.speedA.accept(this);
                this.sb.append(", ");
                bothMotorsOnAction.speedB.accept(this);
            } else {
                bothMotorsOnAction.speedB.accept(this);
                this.sb.append(", ");
                bothMotorsOnAction.speedA.accept(this);
            }
            this.sb.append(");");
        } else {
            this.sb.append("_uBit.soundmotor.motor").append(confCompA.getProperty("PIN1")).append("On(");
            bothMotorsOnAction.speedA.accept(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motor").append(confCompB.getProperty("PIN1")).append("On(");
            bothMotorsOnAction.speedB.accept(this);
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        Set<String> motorPorts = getMotorPins();
        boolean newLine = false;
        if ( motorPorts.contains("A") ) { // Internal motors
            this.sb.append("_uBit.soundmotor.motorAOff();");
            newLine = true;
        }
        if ( motorPorts.contains("B") ) {
            if ( newLine ) {
                nlIndent();
            } else {
                newLine = true;
            }
            this.sb.append("_uBit.soundmotor.motorBOff();");
        }
        if ( motorPorts.contains("0") ) { // Calli:bot motors
            if ( newLine ) {
                nlIndent();
            }
            this.sb.append("_cbSetMotors(_buf, &_i2c, 0, 0);");
        }
        return null;
    }

    /*
     * TODO: I don't know why I am doing this, but it seems that without this a semicolon is lost, somehow... Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        this.sb.append(";");
        return null;
    }

    /*
     * TODO: There is something wrong with semicolon generation for calliope. Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( listGetIndex.getElementOperation().equals(ListElementOperations.REMOVE) ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        writeToSerial(serialWriteAction.value);
        return null;
    }

    private void writeToSerial(Expr valueToWrite) {
        if ( valueToWrite instanceof RgbColor
            || valueToWrite instanceof ColorConst
            || valueToWrite instanceof Var && valueToWrite.getVarType().equals(BlocklyType.COLOR) ) {
            this.sb.append("_uBit.serial.setTxBufferSize(ManagedString(_castColorToString(");
            valueToWrite.accept(this);
            this.sb.append(")).length() + 2);");
            nlIndent();
            this.sb.append("_uBit.serial.send(");
            this.sb.append("_castColorToString(");
            valueToWrite.accept(this);
            this.sb.append(")");
        } else {
            this.sb.append("_uBit.serial.setTxBufferSize(ManagedString((");
            valueToWrite.accept(this);
            this.sb.append(")).length() + 2);");
            nlIndent();
            this.sb.append("_uBit.serial.send(");
            this.sb.append("ManagedString(");
            valueToWrite.accept(this);
            this.sb.append(")");
        }
        this.sb.append(" + \"\\r\\n\", MicroBitSerialMode::ASYNC);");
        nlIndent();
        // give serial some time
        this.sb.append("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
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
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        if ( switchLedMatrixAction.activated.equals("ON") ) {
            this.sb.append("_uBit.display.enable();");
        } else {
            this.sb.append("_uBit.display.disable();");
        }
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        writeToSerial(debugAction.value);
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        if ( ((Binary) assertStmt.asserts).left.getVarType().equals(BlocklyType.COLOR) ) {
            this.sb.append("assertNepo((");
            assertStmt.asserts.accept(this);
            this.sb.append("), \"").append(assertStmt.msg).append("\", \"");
            ((Binary) assertStmt.asserts).left.accept(this);
            this.sb.append("\", \"").append(((Binary) assertStmt.asserts).op.toString()).append("\", \"");
            ((Binary) assertStmt.asserts).getRight().accept(this);
            this.sb.append("\");");
        } else {
            super.visitAssertStmt(assertStmt);
        }
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        String port = servoSetAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        if ( confComp.componentType.equals("CALLIBOT") ) {
            this.sb.append("_cbSetServo(_buf, &_i2c, ");
            String i2cAddress = getCallibotPin(confComp, port);
            this.sb.append(i2cAddress);
            this.sb.append(", ");
        } else {
            String pin = PIN_MAP.get(confComp.getProperty("PIN1"));
            this.sb.append("_uBit.io.").append(pin).append(".setServoValue(");
        }
        servoSetAction.value.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        String userDefinedName = motionKitSingleSetAction.port;
        String currentPort = PIN_MAP.get(userDefinedName);
        String rightMotorPort = PIN_MAP.get("C16"); // C16 is the right motor
        String leftMotorPort = PIN_MAP.get("C17"); // C17 is the left motor
        String direction = motionKitSingleSetAction.direction;
        // for the right motor (C16) 0 is forwards and 180 is backwards
        // for the left  motor (C17) 180 is forwards and 0 is backwards
        if ( userDefinedName.equals(SC.BOTH) ) {
            switch ( direction ) {
                case SC.FOREWARD:
                    this.sb.append("_uBit.io.").append(rightMotorPort).append(".setServoValue(0);");
                    nlIndent();
                    this.sb.append("_uBit.io.").append(leftMotorPort).append(".setServoValue(180);");
                    break;
                case SC.BACKWARD:
                    this.sb.append("_uBit.io.").append(rightMotorPort).append(".setServoValue(180);");
                    nlIndent();
                    this.sb.append("_uBit.io.").append(leftMotorPort).append(".setServoValue(0);");
                    break;
                case SC.OFF:
                    this.sb.append("_uBit.io.").append(rightMotorPort).append(".setAnalogValue(0);");
                    nlIndent();
                    this.sb.append("_uBit.io.").append(leftMotorPort).append(".setAnalogValue(0);");
                    break;
                default:
                    throw new DbcException("Invalid direction!");
            }
        } else {
            switch ( motionKitSingleSetAction.direction ) {
                case SC.FOREWARD:
                    this.sb.append("_uBit.io.").append(currentPort).append(".setServoValue(");
                    this.sb.append(currentPort.equals(rightMotorPort) ? 0 : 180);
                    this.sb.append(");");
                    break;
                case SC.BACKWARD:
                    this.sb.append("_uBit.io.").append(currentPort).append(".setServoValue(");
                    this.sb.append(currentPort.equals(rightMotorPort) ? 180 : 0);
                    this.sb.append(");");
                    break;
                case SC.OFF:
                    this.sb.append("_uBit.io.").append(currentPort).append(".setAnalogValue(0);");
                    break;
                default:
                    throw new DbcException("Invalid direction!");
            }
        }
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        String rightMotorPort = PIN_MAP.get("C16"); // C16 is the right motor
        String leftMotorPort = PIN_MAP.get("C17"); // C17 is the left motor
        // for the right motor (C16) 0 is forwards and 180 is backwards
        // for the left  motor (C17) 180 is forwards and 0 is backwards
        switch ( motionKitDualSetAction.directionRight ) {
            case SC.FOREWARD:
                this.sb.append("_uBit.io.").append(rightMotorPort).append(".setServoValue(0);");
                break;
            case SC.BACKWARD:
                this.sb.append("_uBit.io.").append(rightMotorPort).append(".setServoValue(180);");
                break;
            case SC.OFF:
                this.sb.append("_uBit.io.").append(rightMotorPort).append(".setAnalogValue(0);");
                break;
            default:
                throw new DbcException("Invalid direction!");
        }
        nlIndent();
        switch ( motionKitDualSetAction.directionLeft ) {
            case SC.FOREWARD:
                this.sb.append("_uBit.io.").append(leftMotorPort).append(".setServoValue(180);");
                break;
            case SC.BACKWARD:
                this.sb.append("_uBit.io.").append(leftMotorPort).append(".setServoValue(0);");
                break;
            case SC.OFF:
                this.sb.append("_uBit.io.").append(leftMotorPort).append(".setAnalogValue(0);");
                break;
            default:
                throw new DbcException("Invalid direction!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {

        switch ( colorSensor.getMode() ) {
            case SC.COLOUR:
                this.sb.append("_TCS3472_getColor(_buf, _TCS3472_color, &_i2c, &_uBit, _TCS3472_time)");
                break;
            case SC.LIGHT:
                this.sb.append("_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time)");
                break;
            case SC.RGB:
                this.sb.append("_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time)");
                break;
            default:
                throw new UnsupportedOperationException("Mode " + colorSensor.getMode() + " not supported!");
        }
        return null;
    }

    private Set<String> getMotorPins() {
        Set<String> motorPins = new HashSet<>();
        for ( ConfigurationComponent confComp : this.robotConfiguration.getConfigurationComponentsValues() ) {
            String componentType = confComp.componentType;
            if ( componentType.equals("MOTOR") ) {
                motorPins.add(confComp.getProperty("PIN1"));
            } else if ( componentType.equals("CALLIBOT") ) {
                for ( Map.Entry<String, String> entry : confComp.getComponentProperties().entrySet() ) {
                    if ( entry.getKey().startsWith("MOTOR_") ) {
                        motorPins.add(CALLIBOT_TO_PIN_MAP.get(entry.getKey()));
                    }
                }
            }
        }
        return Collections.unmodifiableSet(motorPins);
    }

    private boolean isDualMode() {
        Set<String> motorPins = getMotorPins();
        return motorPins.stream().filter(s -> s.equals("A") || s.equals("B")).count() > 1;
    }
}