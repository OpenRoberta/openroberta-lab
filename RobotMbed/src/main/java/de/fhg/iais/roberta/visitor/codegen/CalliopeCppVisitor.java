package de.fhg.iais.roberta.visitor.codegen;

import com.google.common.collect.ClassToInstanceMap;

import org.apache.commons.text.WordUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
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
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C++ code representation of a phrase to a
 * StringBuilder. <b>This representation is correct C++ code for Calliope systems.</b> <br>
 */
public final class CalliopeCppVisitor extends AbstractCppVisitor implements IMbedVisitor<Void> {
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
    public CalliopeCppVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean> beans) {
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
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("ManagedString(");
        super.visitStringConst(stringConst);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
        if ( var.getTypeVar().isArray() && var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(" &");
        }
        this.sb.append(whitespace() + var.getCodeSafeName());
        return null;
    }

    protected Void generateUsedVars() {
        for ( final VarDeclaration<Void> var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                this.sb.append("___" + var.getName());
                this.sb.append(whitespace() + "=" + whitespace());
                var.getValue().accept(this);
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
        repeatStmt.getList().accept(this);
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
        waitTimeStmt.getTime().accept(this);
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
            displayTextAction.getMsg().accept(this);
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
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : "0";
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
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("_uBit.soundmotor.soundOn(");
        toneAction.getFrequency().accept(this);
        this.sb.append("); ").append("_uBit.sleep(");
        toneAction.getDuration().accept(this);
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
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        switch ( pin1 ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(pin1).append(", ");
                motorOnAction.getParam().getSpeed().accept(this);
                this.sb.append(");");
                break;
            case "A":
            case "B":
                this.sb.append("_uBit.soundmotor.motor");
                if ( isDualMode() ) {
                    this.sb.append(pin1);
                }
                this.sb.append("On(");
                motorOnAction.getParam().getSpeed().accept(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("visitMotorOnAction; Invalid motor port: " + pin1);
        }
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
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        switch ( pin1 ) {
            case "0":
            case "2":
                this.sb.append("_cbSetMotor(_buf, &_i2c, ").append(pin1).append(", 0);");
                break;
            case "A":
            case "B":
                if ( isDualMode() ) {
                    this.sb.append("_uBit.soundmotor.motor").append(pin1).append("Off();"); // Coast vs OFF
                } else {
                    this.sb.append("_uBit.soundmotor.motor");
                    switch ( (MotorStopMode) motorStopAction.getMode() ) {
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
                            throw new DbcException("Invalide stop mode " + motorStopAction.getMode());
                    }
                }
                break;
            default:
                throw new DbcException("visitMotorStopAction; Invalide motor port: " + pin1);
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
        String port = keysSensor.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.sb.append("_uBit.button").append(pin1).append(".isPressed()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("(_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_");
        String mode = gestureSensor.getMode();
        if ( mode.equals(SC.UP) || mode.equals(SC.DOWN) || mode.equals(SC.LEFT) || mode.equals(SC.RIGHT) ) {
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
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        switch ( pin1 ) {
            case "1":
                this.sb.append("(_uBit.io.P2.readPulseHigh() * 0.017)");
                break;
            case "2":
                this.sb.append("_cbGetSampleUltrasonic(_buf, &_i2c)");
                break;
            default:
                throw new DbcException("UltrasonicSensor; Invalid ultrasonic port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        if ( pin1.equals("1") || pin1.contentEquals("2") ) {
            this.sb.append("_cbGetSampleInfrared(_buf, &_i2c, ").append(pin1).append(")");
        } else {
            throw new DbcException("InfraredSensor; Invalid infrared port: " + port);
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
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
        this.sb.append("_uBit.io." + PIN_MAP.get(pinTouchSensor.getPort()) + ".isTouched()");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        String port = pinValueSensor.getPort();
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
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        String port = pinWriteValueSensor.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String valueType = pinWriteValueSensor.getMode().equals(SC.DIGITAL) ? "DigitalValue(" : "AnalogValue(";
        this.sb.append("_uBit.io.").append(PIN_MAP.get(pin1)).append(".set").append(valueType);
        pinWriteValueSensor.getValue().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        String port = pinSetPullAction.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String mode = pinSetPullAction.getMode();
        this.sb.append("_uBit.io." + PIN_MAP.get(pin1) + ".setPull(Pull").append(WordUtils.capitalizeFully(mode)).append(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            this.sb.append("int _initTime = _uBit.systemTime();");
        }
        mainTask.getVariables().accept(this);
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
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.COLOR) ) {
            String integrationTime = "2_4MS";
            String gain = "1X";
            for ( ConfigurationComponent usedConfigurationBlock : this.robotConfiguration.getConfigurationComponentsValues() ) {
                if ( usedConfigurationBlock.getComponentType().equals(SC.COLOUR) ) {
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
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.ACCELEROMETER) ) {
            nlIndent();
            this.sb.append("_uBit.accelerometer.updateSample();");
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("min(max(");
        mathConstrainFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.getParam().get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("(_uBit.random(");
        mathRandomIntFunct.getParam().get(1).accept(this);
        this.sb.append(" - ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(" + 1)");
        this.sb.append(" + ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        this.sb.append("ManagedString(");
        mathCastStringFunct.getParam().get(0).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.sb.append("ManagedString((char)(");
        mathCastCharFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.sb.append("(int)(");
        textCharCastNumberFunct.getParam().get(0).accept(this);
        this.sb.append(".charAt(");
        textCharCastNumberFunct.getParam().get(1).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        final List<Expr<Void>> parameters = textJoinFunct.getParam().get();
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
                displayImageAction.getValuesToDisplay().accept(this);
                this.sb.append(");");
                nlIndent();
            } catch ( Exception e ) {
                this.sb.append("for (MicroBitImage& image : ");
                displayImageAction.getValuesToDisplay().accept(this);
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
        displayImageAction.getValuesToDisplay().accept(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        this.sb.append(".shiftImage" + capitalizeFirstLetter(imageShiftFunction.getShiftDirection().toString()) + "(");
        imageShiftFunction.getPositions().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
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
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : "0";
        switch ( pin1 ) {
            case "0":
                this.sb.append("_uBit.rgb.setColour(");
                ledOnAction.getLedColor().accept(this);
                this.sb.append(");");
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                this.sb.append("_cbSetRGBLed(_buf, &_i2c, ").append(pin1).append(", ");
                ledOnAction.getLedColor().accept(this);
                this.sb.append(");");
                break;
            default:
                throw new DbcException("LedOnAction; invalid port: " + pin1);
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String port = lightAction.getPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        String mode = lightAction.getMode().getValues()[0];
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
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        this.sb.append("_uBit.radio.setTransmitPower(" + radioSendAction.getPower() + ");");
        nlIndent();
        switch ( radioSendAction.getType() ) {
            case NUMBER:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.getMsg().accept(this);
                this.sb.append("))");
                break;
            case BOOLEAN:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((int)(");
                radioSendAction.getMsg().accept(this);
                this.sb.append(")?true:false)");
                break;
            case STRING:
                this.sb.append("_uBit.radio.datagram.send(ManagedString((");
                radioSendAction.getMsg().accept(this);
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
        radioSetChannelAction.getChannel().accept(this);
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
        if ( accelerometerSensor.getSlot().equals("STRENGTH") ) {
            this.sb.append("Strength");
        } else {
            this.sb.append(accelerometerSensor.getSlot());
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("MicroBitColor(");
        rgbColor.getR().accept(this);
        this.sb.append(", ");
        rgbColor.getG().accept(this);
        this.sb.append(", ");
        rgbColor.getB().accept(this);
        this.sb.append(", ");
        rgbColor.getA().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<Void> displaySetBrightnessAction) {
        this.sb.append("_uBit.display.setBrightness((");
        displaySetBrightnessAction.getBrightness().accept(this);
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
        displaySetPixelAction.getX().accept(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().accept(this);
        this.sb.append(", (");
        displaySetPixelAction.getBrightness().accept(this);
        this.sb.append(") * _SET_BRIGHTNESS_MULTIPLIER);");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("round(_uBit.display.image.getPixelValue(");
        displayGetPixelAction.getX().accept(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().accept(this);
        this.sb.append(") * _GET_BRIGHTNESS_MULTIPLIER)");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Void> fourDigitDisplayShowAction) {
        this.sb.append("_fdd.show(");
        fourDigitDisplayShowAction.getValue().accept(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.getPosition().accept(this);
        this.sb.append(", ");
        fourDigitDisplayShowAction.getColon().accept(this);
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
        ledBarSetAction.getX().accept(this);
        this.sb.append(", ");
        ledBarSetAction.getBrightness().accept(this);
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

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            this.sb.append("scroll(");
        } else {
            this.sb.append("print(");
        }
    }

    private String wrapInManageStringToDisplay(DisplayTextAction<Void> displayTextAction, String ending) {
        this.sb.append("ManagedString(");
        displayTextAction.getMsg().accept(this);
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
        for ( final Method<Void> phrase : this.getBean(UsedHardwareBean.class).getUserDefinedMethods() ) {
            nlIndent();
            this.sb.append(getLanguageVarTypeFromBlocklyType(phrase.getReturnType()));
            this.sb.append(" " + phrase.getMethodName() + "(");
            phrase.getParameters().accept(this);
            this.sb.append(");");
            nlIndent();
        }
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction<Void> bothMotorsOnAction) {
        String portA = bothMotorsOnAction.getPortA();
        String portB = bothMotorsOnAction.getPortB();
        ConfigurationComponent confCompA = this.robotConfiguration.getConfigurationComponent(portA);
        ConfigurationComponent confCompB = this.robotConfiguration.getConfigurationComponent(portB);
        String pin1A = confCompA.getComponentType().equals("CALLIBOT") ? getCallibotPin(confCompA, portA) : confCompA.getProperty("PIN1");
        String pin1B = confCompB.getComponentType().equals("CALLIBOT") ? getCallibotPin(confCompB, portB) : confCompB.getProperty("PIN1");
        if ( pin1A.equals("A") && pin1B.equals("B") ) {
            this.sb.append("_uBit.soundmotor.motorAOn(");
            bothMotorsOnAction.getSpeedA().accept(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorBOn(");
            bothMotorsOnAction.getSpeedB().accept(this);
            this.sb.append(");");
        } else if ( pin1A.equals("B") && pin1B.equals("A") ) {
            this.sb.append("_uBit.soundmotor.motorBOn(");
            bothMotorsOnAction.getSpeedA().accept(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorAOn(");
            bothMotorsOnAction.getSpeedB().accept(this);
            this.sb.append(");");
        }
        if ( pin1A.equals("0") ) {
            this.sb.append("_cbSetMotors(_buf, &_i2c, ");
            bothMotorsOnAction.getSpeedA().accept(this);
            this.sb.append(", ");
            bothMotorsOnAction.getSpeedB().accept(this);
            this.sb.append(");");
        }

        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction<Void> bothMotorsStopAction) {
        Set<String> motorPorts = getMotorPins();
        boolean both = false;
        if ( motorPorts.contains("A") || motorPorts.contains("B") ) { // Internal motors
            this.sb.append("_uBit.soundmotor.motorAOff();");
            nlIndent();
            this.sb.append("_uBit.soundmotor.motorBOff();");
            both = true;
        }
        if ( motorPorts.contains("0") || motorPorts.contains("2") ) { // Calli:bot motors
            if ( both ) {
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
            || valueToWrite instanceof Var && ((Var<Void>) valueToWrite).getVarType().equals(BlocklyType.COLOR) ) {
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
            assertStmt.getAssert().accept(this);
            this.sb.append("), \"").append(assertStmt.getMsg()).append("\", \"");
            ((Binary<Void>) assertStmt.getAssert()).getLeft().accept(this);
            this.sb.append("\", \"").append(((Binary<Void>) assertStmt.getAssert()).getOp().toString()).append("\", \"");
            ((Binary<Void>) assertStmt.getAssert()).getRight().accept(this);
            this.sb.append("\");");
        } else {
            super.visitAssertStmt(assertStmt);
        }
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction<Void> servoSetAction) {
        String port = servoSetAction.getPort();
        ConfigurationComponent confComp = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = confComp.getComponentType().equals("CALLIBOT") ? getCallibotPin(confComp, port) : confComp.getProperty("PIN1");
        if (pin1.equals("0x14") || pin1.equals("0x15")) {
            this.sb.append("_cbSetServo(_buf, &_i2c, ");
            this.sb.append(pin1);
            this.sb.append(", ");
        } else {
            this.sb.append("_uBit.io.").append(PIN_MAP.get(pin1)).append(".setServoValue(");
        }
        servoSetAction.getValue().accept(this);
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction<Void> motionKitSingleSetAction) {
        String userDefinedName = motionKitSingleSetAction.getPort();
        String currentPort = PIN_MAP.get(userDefinedName);
        String rightMotorPort = PIN_MAP.get("C16"); // C16 is the right motor
        String leftMotorPort = PIN_MAP.get("C17"); // C17 is the left motor
        String direction = motionKitSingleSetAction.getDirection();
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
            switch ( motionKitSingleSetAction.getDirection() ) {
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
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction<Void> motionKitDualSetAction) {
        String rightMotorPort = PIN_MAP.get("C16"); // C16 is the right motor
        String leftMotorPort = PIN_MAP.get("C17"); // C17 is the left motor
        // for the right motor (C16) 0 is forwards and 180 is backwards
        // for the left  motor (C17) 180 is forwards and 0 is backwards
        switch ( motionKitDualSetAction.getDirectionRight() ) {
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
        switch ( motionKitDualSetAction.getDirectionLeft() ) {
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
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {

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
            String componentType = confComp.getComponentType();
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