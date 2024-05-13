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
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
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
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathModuloFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
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
import de.fhg.iais.roberta.syntax.sensor.mbed.CallibotKeysSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
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
        CALLIBOT_TO_PIN_MAP.put("LED_L", "1");
        CALLIBOT_TO_PIN_MAP.put("LED_R", "2");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_L", "2");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_R", "1");
        CALLIBOT_TO_PIN_MAP.put("ULTRASONIC", "2");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S1", "0x14");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S2", "0x15");
        CALLIBOT_TO_PIN_MAP.put("KEY_FL", "1");
        CALLIBOT_TO_PIN_MAP.put("KEY_FR", "2");

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
        String portName = "";
        for ( List<ConfigurationComponent> ccList : confComp.getSubComponents().values() ) {
            for ( ConfigurationComponent cc : ccList ) {
                if ( port.equals(cc.userDefinedPortName) ) {
                    portName = cc.componentProperties.get("PORT");
                    break;
                }
            }
        }
        if ( !portName.equals("") ) {
            return CALLIBOT_TO_PIN_MAP.get(portName);
        } else {
            throw new DbcException("Invalid port!");
        }
    }

    private ConfigurationComponent getBusSubComponentForCallibot(ConfigurationComponent callibot, String userDefinedName) {
        for ( ConfigurationComponent subComponent : callibot.getSubComponents().get("BUS") ) {
            if ( subComponent.userDefinedPortName.equals(userDefinedName) ) {
                return subComponent;
            }
        }
        throw new DbcException("Invalid structure for Callibot encountered.");
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        this.src.add("ManagedString(");
        super.visitStringConst(stringConst);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.src.add(getLanguageVarTypeFromBlocklyType(var.getBlocklyType()));
        if ( var.getBlocklyType().isArray() && var.value.getKind().hasName("EMPTY_EXPR") ) {
            this.src.add(" &");
        }
        this.src.add(" ", var.getCodeSafeName());
        return null;
    }

    private Void generateUsedVars() {
        for ( final VarDeclaration var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                this.src.add("___", var.name);
                this.src.add(" = ");
                var.value.accept(this);
                this.src.add(";");
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        final Op op = binary.op;
        if ( op == Op.MOD ) {
            this.src.add("(int) ");
        } else if ( op == Op.NEQ ) {
            this.src.add("!( ");
        }
        generateSubExpr(this.src, false, binary.left, binary);
        final String sym;
        if ( op.equals(Op.NEQ) ) {
            sym = "==";
        } else {
            sym = getBinaryOperatorSymbol(op);
        }

        this.src.add(" ", sym, " ");
        switch ( op ) {
            case DIVIDE:
                this.src.add("((float) ");
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(")");
                break;
            case MOD:
                this.src.add("((int) ");
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(")");
                break;
            case NEQ:
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(" )");
                break;
            default:
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        }

        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.src.add("\"\"");
                break;
            case BOOLEAN:
                this.src.add("true");
                break;
            case NUMBER:
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                this.src.add("MicroBitColor()");
                break;
            case PREDEFINED_IMAGE:
                this.src.add("MicroBitImage()");
                break;
            case IMAGE:
                this.src.add("MicroBitImage()");
                break;
            default:
                this.src.add("NULL");
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
            this.src.add("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
        } else {
            appendBreakStmt();
        }

        decrIndentation();
        nlIndent();
        this.src.add("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("_uBit.sleep(");
        waitTimeStmt.time.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        String ending = ")";
        final String varType = displayTextAction.msg.getBlocklyType().toString();
        this.src.add("_uBit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !varType.equals("STRING") ) {
            ending = wrapInManageStringToDisplay(displayTextAction, ending);
        } else {
            displayTextAction.msg.accept(this);
        }

        this.src.add(ending, ";");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("_uBit.display.clear();");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("_uBit.soundmotor.soundOn(");
        toneAction.frequency.accept(this);
        this.src.add("); ", "_uBit.sleep(");
        toneAction.duration.accept(this);
        this.src.add("); ", "_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("_uBit.soundmotor.soundOn(");
        this.src.add(playNoteAction.frequency);
        this.src.add("); ", "_uBit.sleep(");
        this.src.add(playNoteAction.duration);
        this.src.add("); ", "_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        String pin;
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external motor and Callibot.");

        if ( isCallibotMotor ) {
            pin = getCallibotPin(confCompCallibot, port);
        } else {
            pin = confComp.getProperty("PIN1");
        }
        switch ( pin ) {
            case "0":
            case "2":
                this.src.add("_cbSetMotor(_buf, &_i2c, ", pin, ", ");
                motorOnAction.param.getSpeed().accept(this);
                this.src.add(");");
                break;
            case "A":
            case "B":
                this.src.add("_uBit.soundmotor.motor");
                if ( isDualMode() ) {
                    this.src.add(pin);
                }
                this.src.add("On(");
                motorOnAction.param.getSpeed().accept(this);
                this.src.add(");");
                break;
            default:
                throw new DbcException("visitMotorOnAction; Invalid motor port: " + pin);
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        String pin;
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external motor and Callibot.");

        if ( isCallibotMotor ) {
            pin = getCallibotPin(confCompCallibot, port);
        } else {
            pin = confComp.getProperty("PIN1");
        }
        switch ( pin ) {
            case "0":
            case "2":
                this.src.add("_cbSetMotor(_buf, &_i2c, ", pin, ", 0);");
                break;
            case "A":
            case "B":
                if ( isDualMode() ) {
                    this.src.add("_uBit.soundmotor.motor", pin, "Off();"); // Coast vs OFF
                    this.src.add("//float, break and sleep doesn't work with more than one motor connected");
                } else {
                    this.src.add("_uBit.soundmotor.motor");
                    switch ( (MotorStopMode) motorStopAction.mode ) {
                        case FLOAT:
                            this.src.add("Coast();");
                            break;
                        case NONFLOAT:
                            this.src.add("Break();");
                            break;
                        case SLEEP:
                            this.src.add("Sleep();");
                            break;
                        default:
                            throw new DbcException("Invalide stop mode " + motorStopAction.mode);
                    }
                }
                break;
            default:
                throw new DbcException("visitMotorStopAction; Invalide motor port: " + pin);
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = keysSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.src.add("_uBit.button", pin1, ".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        String mode = gestureSensor.getMode();
        if ( mode.equals("SHAKE") ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.IS_GESTURE_SHAKE));
            this.src.add("()");
        } else {
            this.src.add("(_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
            if ( mode.equals(SC.UP) || mode.equals(SC.DOWN) || mode.equals(SC.LEFT) || mode.equals(SC.RIGHT) ) {
                this.src.add("TILT_");
            }
            this.src.add(mode, ")");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("_uBit.thermometer.getTemperature()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        this.src.add("_uBit.compass.heading()");
        return null;
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor microphoneSensor) {
        this.src.add("_uBit.io.P21.getMicrophoneValue()");
        return null;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        return null;
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        return null;
    }

    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( confCompCallibot != null ) {
            this.src.add("_cbGetSampleUltrasonic(_buf, &_i2c)");
        } else {
            // Safe to say that an external ultrasonic sensor is being used in the configuration
            this.src.add("(_uBit.io.P2.readPulseHigh() * 0.017)");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( confComp != null ) {
            String port = infraredSensor.getUserDefinedPort();
            String pin = getCallibotPin(confComp, port);
            if ( pin.equals("1") || pin.contentEquals("2") ) {
                this.src.add("_cbGetSampleInfrared(_buf, &_i2c, ", pin, ")");
            } else {
                throw new DbcException("InfraredSensor; Invalid infrared port: " + port);
            }
        } else {
            throw new DbcException("Infrared sensor only supported with Callibot block.");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        String slot = gyroSensor.getSlot();
        if ( slot.equals("X") ) { // TODO rename to Pitch and Roll in the configuration?
            this.src.add("_uBit.accelerometer.getPitch()");
        } else if ( slot.equals("Y") ) {
            this.src.add("_uBit.accelerometer.getRoll()");
        } else {
            throw new DbcException("Slot " + slot + " is not valid!");
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("( _uBit.systemTime() - _initTime )");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("_initTime = _uBit.systemTime();");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        this.src.add("_uBit.io.", PIN_MAP.get(pinTouchSensor.getUserDefinedPort()), ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        String port = pinValueSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String mode = pinValueSensor.getMode();
        this.src.add("_uBit.io.", PIN_MAP.get(pin1));
        switch ( mode ) {
            case SC.DIGITAL:
                this.src.add(".getDigitalValue()");
                break;
            case SC.ANALOG:
                this.src.add(".getAnalogValue()");
                break;
            case SC.PULSEHIGH:
                this.src.add(".readPulseHigh()");
                break;
            case SC.PULSELOW:
                this.src.add(".readPulseLow()");
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
        this.src.add("_uBit.io.", PIN_MAP.get(pin1), ".set", valueType);
        mbedPinWriteValueAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            this.src.add("int _initTime = _uBit.systemTime();");
        }
        mainTask.variables.accept(this);
        nlIndent();
        nlIndent();
        this.src.add("int main()");
        nlIndent();
        this.src.add("{");
        incrIndentation();
        nlIndent();
        // Initialise the micro:bit runtime.
        this.src.add("_uBit.init();");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.CALLIBOT) ) {
            nlIndent();
            this.src.add("_cbInit(_buf, &_i2c, &_uBit);");
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
                    this.src.add("_uBit.io.", PIN_MAP.get(pin1), ".setPull(Pull", WordUtils.capitalizeFully(mode), ");");
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
            this.src.add("_TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_", integrationTime, ", TCS3472_GAIN_", gain, ");");
            nlIndent();
            this.src.add("_TCS3472_time = TCS3472_INTEGRATIONTIME_", integrationTime, ";");
        }

        generateUsedVars();

        nlIndent();

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DISPLAY_GRAYSCALE) ) {
            this.src.add("_uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            nlIndent();
            this.src.add("_uBit.radio.enable();");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.ACCELEROMETER) || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COMPASS) ) {
            nlIndent();
            this.src.add("_uBit.accelerometer.updateSample();");
        }
        if ( this.isDualMode() && this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            this.src.add("nrf_gpiote_task_configure(0, CALLIOPE_PIN_MOTOR_IN2, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_HIGH);");
            nlIndent();
            this.src.add("nrf_gpiote_task_configure(1, CALLIOPE_PIN_MOTOR_IN1, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_LOW);");
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("min(max(");
        mathConstrainFunct.value.accept(this);
        this.src.add(", ");
        mathConstrainFunct.lowerBound.accept(this);
        this.src.add("), ");
        mathConstrainFunct.upperBound.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("(_uBit.random((");
        mathRandomIntFunct.to.accept(this);
        this.src.add(") - (");
        mathRandomIntFunct.from.accept(this);
        this.src.add(") + 1)");
        this.src.add(" + (");
        mathRandomIntFunct.from.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.src.add("ManagedString(");
        mathCastStringFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.src.add("ManagedString((char)(");
        mathCastCharFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        this.src.add("( (int) ( ");
        mathModuloFunct.dividend.accept(this);
        this.src.add(" ) % (int) ( ");
        mathModuloFunct.divisor.accept(this);
        this.src.add(" ) )");
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" = ");
        textAppendStmt.var.accept(this);
        this.src.add(" + ManagedString(");
        textAppendStmt.text.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.src.add("std::atof((");
        textStringCastNumberFunct.value.accept(this);
        this.src.add(").toCharArray())");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("(int)(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add(".charAt(");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        final List<Expr> parameters = textJoinFunct.param.get();
        final int numberOfParameters = parameters.size();
        for ( int i = 0; i < numberOfParameters; i++ ) {
            this.src.add("ManagedString(");
            parameters.get(i).accept(this);
            this.src.add(")");
            if ( i < numberOfParameters - 1 ) {
                this.src.add(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        this.src.add("MicroBitImage(\"", predefinedImage.getImageName().getImageString(), "\")");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        String end = ");";
        if ( displayImageAction.displayImageMode.equals("ANIMATION") ) {
            try {
                Expr values = displayImageAction.valuesToDisplay;
                int valuesSize = ((ListCreate) values).exprList.get().size();
                this.src.add("std::array<MicroBitImage, ", valuesSize, "> _animation = _convertToArray<MicroBitImage, ", valuesSize, ">(");
                displayImageAction.valuesToDisplay.accept(this);
                this.src.add(");");
                nlIndent();
            } catch ( Exception e ) {
                this.src.add("for (MicroBitImage& image : ");
                displayImageAction.valuesToDisplay.accept(this);
                this.src.add(") {");
                this.src.add("_uBit.display.print(image, 0, 0, 255, 200);");
                this.src.add("_uBit.display.clear();");
                this.src.add("}");
                return null;
            }
        }
        this.src.add("_uBit.display.");
        if ( displayImageAction.displayImageMode.equals("ANIMATION") ) {
            this.src.add("animateImages(_animation, 200);");
            return null;
        } else {
            this.src.add("print(");
        }
        displayImageAction.valuesToDisplay.accept(this);
        this.src.add(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        imageShiftFunction.image.accept(this);
        this.src.add(".shiftImage", capitalizeFirstLetter(imageShiftFunction.shiftDirection.toString()), "(");
        imageShiftFunction.positions.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        imageInvertFunction.image.accept(this);
        this.src.add(".invert()");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        this.src.add("MicroBitImage(\"");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.src.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
                if ( j < 4 ) {
                    this.src.add(",");
                }
            }
            this.src.add("\\n");
        }
        this.src.add("\")");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src
            .add("MicroBitColor(", colorConst.getRedChannelInt(), ", ", colorConst.getGreenChannelInt(), ", ", colorConst.getBlueChannelInt(), ", ", 255, ")");
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, rgbLedOnAction.port);
        this.src.add("_cbSetRGBLed(_buf, &_i2c, ", pin1, ", ");
        rgbLedOnAction.colour.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, rgbLedOffAction.port);
        this.src.add("_cbSetRGBLed(_buf, &_i2c, ", pin1, ", 0);");
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        this.src.add("_uBit.rgb.setColour(");
        rgbLedOnHiddenAction.colour.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        this.src.add("_uBit.rgb.off();");
        return null;
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, ledAction.port);
        String mode = "";
        switch ( ledAction.mode ) {
            case "OFF":
                mode = "0";
                break;
            case "ON":
                mode = "1";
                break;
            default:
                throw new DbcException("Invalid MODE encountered in LedAction: " + ledAction.mode);
        }
        this.src.add("_cbSetLed(_buf, &_i2c, _cbLedState, ", pin1, ", ", mode, ");");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        this.src.add("_uBit.radio.setTransmitPower(", radioSendAction.power, ");");
        nlIndent();
        switch ( radioSendAction.type ) {
            case "Number":
                this.src.add("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.message.accept(this);
                this.src.add("))");
                break;
            case "Boolean":
                this.src.add("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.message.accept(this);
                this.src.add(")?true:false)");
                break;
            case "String":
                this.src.add("_uBit.radio.datagram.send(ManagedString((");
                radioSendAction.message.accept(this);
                this.src.add("))");
                break;

            default:
                throw new IllegalArgumentException("unhandled type");
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        switch ( radioReceiveAction.type ) {
            case "Boolean":
            case "Number":
                this.src.add("atoi((char*)_uBit.radio.datagram.recv().getBytes())");
                break;
            case "String":
                this.src.add("ManagedString(_uBit.radio.datagram.recv())");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        this.src.add("_uBit.radio.setGroup(");
        radioSetChannelAction.channel.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        this.src.add("_uBit.radio.getRSSI()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.src.add("_uBit.accelerometer.get");
        if ( accelerometerSensor.getSlot().equals("STRENGTH") ) {
            this.src.add("Strength");
        } else {
            this.src.add(accelerometerSensor.getSlot());
        }
        this.src.add("()");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("MicroBitColor(");
        rgbColor.R.accept(this);
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(", ");
        rgbColor.A.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        this.src.add("_uBit.display.setBrightness((");
        displaySetBrightnessAction.brightness.accept(this);
        this.src.add(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        this.src.add("round(_uBit.display.getBrightness() * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        this.src.add("_uBit.display.image.setPixelValue(");
        displaySetPixelAction.x.accept(this);
        this.src.add(", ");
        displaySetPixelAction.y.accept(this);
        this.src.add(", (");
        displaySetPixelAction.brightness.accept(this);
        this.src.add(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        this.src.add("round(_uBit.display.image.getPixelValue(");
        displayGetPixelAction.x.accept(this);
        this.src.add(", ");
        displayGetPixelAction.y.accept(this);
        this.src.add(") * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        this.src.add("_fdd.show(");
        fourDigitDisplayShowAction.value.accept(this);
        this.src.add(", ");
        fourDigitDisplayShowAction.position.accept(this);
        this.src.add(", ");
        fourDigitDisplayShowAction.colon.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        this.src.add("_fdd.clear();");
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        this.src.add("_ledBar.setLed(");
        ledBarSetAction.x.accept(this);
        this.src.add(", ");
        ledBarSetAction.brightness.accept(this);
        this.src.add(");");
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
                this.src.add("_cbStop(_buf, &_i2c);");
            }
            nlIndent();
            this.src.add("release_fiber();");
            decrIndentation();
            nlIndent();
            this.src.add("}");
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
            case CAPTURED_TYPE_ARRAY_ITEM:
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
            this.src.add("scroll(");
        } else {
            this.src.add("print(");
        }
    }

    private String wrapInManageStringToDisplay(DisplayTextAction displayTextAction, String ending) {
        this.src.add("ManagedString(");
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
        this.src.add("#define _GNU_SOURCE\n\n");
        this.src.add("#include \"MicroBit.h\"\n");
        this.src.add("#include \"NEPODefs.h\"\n");

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.FOUR_DIGIT_DISPLAY) ) {
            this.src.add("#include \"FourDigitDisplay.h\"\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.LED_BAR) ) {
            this.src.add("#include \"Grove_LED_Bar.h\"\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.HUMIDITY) ) {
            this.src.add("#include \"Sht31.h\"\n");
        }
        if ( this.isDualMode() && this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            this.src.add("#include \"nrf_gpiote.h\"\n");
        }
        this.src.add("#include <list>\n");
        this.src.add("#include <array>\n");
        this.src.add("#include <stdlib.h>\n");
        this.src.add("MicroBit _uBit;\n");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.FOUR_DIGIT_DISPLAY) ) {
            this.src.add("FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);\n"); // Only works on the right UART Grove connector
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.LED_BAR) ) {
            this.src.add("Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n"); // Only works on the right UART Grove connector; Clock/Data pins are swapped compared to 4DigitDisplay
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.HUMIDITY) ) {
            this.src.add("Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);\n");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.CALLIBOT) ) {
            this.src.add("MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);");
            nlIndent();
            this.src.add("char _buf[5] = { 0, 0, 0, 0, 0 };");
            nlIndent();
            this.src.add("uint8_t _cbLedState = 0x00;");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COLOR) ) {
            this.src.add("MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);");
            nlIndent();
            this.src.add("char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };");
            nlIndent();
            this.src.add("std::list<double> _TCS3472_rgb;");
            nlIndent();
            this.src.add("MicroBitColor _TCS3472_color;");
            nlIndent();
            this.src.add("char _TCS3472_time = 0xff;");
        }
    }

    @Override
    protected void generateSignaturesOfUserDefinedMethods() {
        for ( final Method phrase : this.getBean(UsedHardwareBean.class).getUserDefinedMethods() ) {
            nlIndent();
            this.src.add(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()));
            this.src.add(" ", phrase.getCodeSafeMethodName(), "(");
            phrase.getParameters().accept(this);
            this.src.add(");");
            nlIndent();
        }
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        String portA = bothMotorsOnAction.portA;
        String portB = bothMotorsOnAction.portB;
        ConfigurationComponent confCompA = this.robotConfiguration.optConfigurationComponent(portA);
        ConfigurationComponent confCompB = this.robotConfiguration.optConfigurationComponent(portB);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confCompA == null && confCompB == null && confCompCallibot != null;
        if ( isCallibotMotor ) {
            ConfigurationComponent subComponentForCallibot = getBusSubComponentForCallibot(confCompCallibot, portA);
            this.src.add("_cbSetMotors(_buf, &_i2c, ");
            if ( subComponentForCallibot.componentProperties.get("PORT").equals("MOTOR_L") ) {
                bothMotorsOnAction.speedA.accept(this);
                this.src.add(", ");
                bothMotorsOnAction.speedB.accept(this);
            } else {
                bothMotorsOnAction.speedB.accept(this);
                this.src.add(", ");
                bothMotorsOnAction.speedA.accept(this);
            }
            this.src.add(");");
        } else {
            this.src.add("_uBit.soundmotor.motor", confCompA.getProperty("PIN1"), "On(");
            bothMotorsOnAction.speedA.accept(this);
            this.src.add(");");
            nlIndent();
            this.src.add("_uBit.soundmotor.motor", confCompB.getProperty("PIN1"), "On(");
            bothMotorsOnAction.speedB.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        Set<String> motorPorts = getMotorPins();
        boolean newLine = false;
        if ( motorPorts.contains("A") ) { // Internal motors
            this.src.add("_uBit.soundmotor.motorAOff();");
            newLine = true;
        }
        if ( motorPorts.contains("B") ) {
            if ( newLine ) {
                nlIndent();
            } else {
                newLine = true;
            }
            this.src.add("_uBit.soundmotor.motorBOff();");
        }
        if ( motorPorts.contains("0") ) { // Calli:bot motors
            if ( newLine ) {
                nlIndent();
            }
            this.src.add("_cbSetMotors(_buf, &_i2c, 0, 0);");
        }
        return null;
    }

    /*
     * TODO: I don't know why I am doing this, but it seems that without this a semicolon is lost, somehow... Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        this.src.add(";");
        return null;
    }

    /*
     * TODO: There is something wrong with semicolon generation for calliope. Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( listGetIndex.getElementOperation().equals(ListElementOperations.REMOVE) ) {
            this.src.add(";");
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
            || valueToWrite instanceof Var && valueToWrite.getBlocklyType().equals(BlocklyType.COLOR) ) {
            this.src.add("_uBit.serial.setTxBufferSize(ManagedString(_castColorToString(");
            valueToWrite.accept(this);
            this.src.add(")).length() + 2);");
            nlIndent();
            this.src.add("_uBit.serial.send(");
            this.src.add("_castColorToString(");
            valueToWrite.accept(this);
            this.src.add(")");
        } else {
            this.src.add("_uBit.serial.setTxBufferSize(ManagedString((");
            valueToWrite.accept(this);
            this.src.add(")).length() + 2);");
            nlIndent();
            this.src.add("_uBit.serial.send(");
            this.src.add("ManagedString(");
            valueToWrite.accept(this);
            this.src.add(")");
        }
        this.src.add(" + \"\\r\\n\", MicroBitSerialMode::ASYNC);");
        nlIndent();
        // give serial some time
        this.src.add("_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);");
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        if ( humiditySensor.getMode().equals(SC.HUMIDITY) ) {
            this.src.add("_sht31.readHumidity()");
        } else if ( humiditySensor.getMode().equals(SC.TEMPERATURE) ) {
            this.src.add("_sht31.readTemperature()");
        } else {
            throw new UnsupportedOperationException("Mode " + humiditySensor.getMode() + " not supported!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        if ( switchLedMatrixAction.activated.equals("ON") ) {
            this.src.add("_uBit.display.enable();");
        } else {
            this.src.add("_uBit.display.disable();");
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
        if ( ((Binary) assertStmt.asserts).left.getBlocklyType().equals(BlocklyType.COLOR) ) {
            this.src.add("assertNepo((");
            assertStmt.asserts.accept(this);
            this.src.add("), \"", assertStmt.msg, "\", \"");
            ((Binary) assertStmt.asserts).left.accept(this);
            this.src.add("\", \"", ((Binary) assertStmt.asserts).op.toString(), "\", \"");
            ((Binary) assertStmt.asserts).getRight().accept(this);
            this.src.add("\");");
        } else {
            super.visitAssertStmt(assertStmt);
        }
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        String port = servoSetAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotServoMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external servo motor and Callibot.");
        if ( isCallibotServoMotor ) {
            this.src.add("_cbSetServo(_buf, &_i2c, ");
            String i2cAddress = getCallibotPin(confCompCallibot, port);
            this.src.add(i2cAddress);
            this.src.add(", ");
        } else {
            String pin = PIN_MAP.get(confComp.getProperty("PIN1"));
            this.src.add("_uBit.io.", pin, ".setServoValue(");
        }
        servoSetAction.value.accept(this);
        this.src.add(");");
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
                    this.src.add("_uBit.io.", rightMotorPort, ".setServoValue(0);");
                    nlIndent();
                    this.src.add("_uBit.io.", leftMotorPort, ".setServoValue(180);");
                    break;
                case SC.BACKWARD:
                    this.src.add("_uBit.io.", rightMotorPort, ".setServoValue(180);");
                    nlIndent();
                    this.src.add("_uBit.io.", leftMotorPort, ".setServoValue(0);");
                    break;
                case SC.OFF:
                    this.src.add("_uBit.io.", rightMotorPort, ".setAnalogValue(0);");
                    nlIndent();
                    this.src.add("_uBit.io.", leftMotorPort, ".setAnalogValue(0);");
                    break;
                default:
                    throw new DbcException("Invalid direction!");
            }
        } else {
            switch ( motionKitSingleSetAction.direction ) {
                case SC.FOREWARD:
                    this.src.add("_uBit.io.", currentPort, ".setServoValue(");
                    this.src.add(currentPort.equals(rightMotorPort) ? 0 : 180);
                    this.src.add(");");
                    break;
                case SC.BACKWARD:
                    this.src.add("_uBit.io.", currentPort, ".setServoValue(");
                    this.src.add(currentPort.equals(rightMotorPort) ? 180 : 0);
                    this.src.add(");");
                    break;
                case SC.OFF:
                    this.src.add("_uBit.io.", currentPort, ".setAnalogValue(0);");
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
                this.src.add("_uBit.io.", rightMotorPort, ".setServoValue(0);");
                break;
            case SC.BACKWARD:
                this.src.add("_uBit.io.", rightMotorPort, ".setServoValue(180);");
                break;
            case SC.OFF:
                this.src.add("_uBit.io.", rightMotorPort, ".setAnalogValue(0);");
                break;
            default:
                throw new DbcException("Invalid direction!");
        }
        nlIndent();
        switch ( motionKitDualSetAction.directionLeft ) {
            case SC.FOREWARD:
                this.src.add("_uBit.io.", leftMotorPort, ".setServoValue(180);");
                break;
            case SC.BACKWARD:
                this.src.add("_uBit.io.", leftMotorPort, ".setServoValue(0);");
                break;
            case SC.OFF:
                this.src.add("_uBit.io.", leftMotorPort, ".setAnalogValue(0);");
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
                this.src.add("_TCS3472_getColor(_buf, _TCS3472_color, &_i2c, &_uBit, _TCS3472_time)");
                break;
            case SC.LIGHT:
                this.src.add("_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time)");
                break;
            case SC.RGB:
                this.src.add("_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time)");
                break;
            default:
                throw new UnsupportedOperationException("Mode " + colorSensor.getMode() + " not supported!");
        }
        return null;
    }

    @Override
    public Void visitRgbLedsOnHiddenAction(RgbLedsOnHiddenAction rgbLedsOnHiddenAction) {
        this.src.add("_uBit.rgb.setColour(");
        rgbLedsOnHiddenAction.colour.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRgbLedsOffHiddenAction(RgbLedsOffHiddenAction rgbLedsOffHiddenAction) {
        this.src.add("_uBit.rgb.off();");
        return null;
    }

    @Override
    public Void visitCallibotKeysSensor(CallibotKeysSensor callibotKeysSensor) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( confComp != null ) {
            String port = callibotKeysSensor.getUserDefinedPort();
            String pin = getCallibotPin(confComp, port);
            if ( pin.equals("1") || pin.contentEquals("2") ) {
                this.src.add("_cbGetSampleBumperKey(_buf, &_i2c, ", pin, ")");
            } else {
                throw new DbcException("InfraredSensor; Key port: " + port);
            }
        } else {
            throw new DbcException("Callibot key sensor only supported with Callibot block.");
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
                for ( List<ConfigurationComponent> ccList : confComp.getSubComponents().values() ) {
                    for ( ConfigurationComponent cc : ccList ) {
                        if ( cc.componentType.equals("MOTOR") ) {
                            motorPins.add(CALLIBOT_TO_PIN_MAP.get(cc.componentProperties.get("PORT")));
                        }
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