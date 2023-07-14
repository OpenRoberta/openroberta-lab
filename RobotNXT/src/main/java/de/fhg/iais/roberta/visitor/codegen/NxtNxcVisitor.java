package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathModuloFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.dbc.VisitorException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.INxtVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;
import de.fhg.iais.roberta.visitor.syntax.light.NxtRgbLedOnAction;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable NXC code representation of a phrase to a
 * StringBuilder. <b>This representation is correct NXC code for NXT.</b> <br>
 */
public final class NxtNxcVisitor extends AbstractCppVisitor implements INxtVisitor<Void> {

    private final ConfigurationAst brickConfiguration;

    /**
     * initialize the Nxc code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public NxtNxcVisitor(List<List<Phrase>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color;
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#000000":
                color = "BLACK";
                break;
            case "#0057A6":
                color = "BLUE";
                break;
            case "#00642E":
                color = "GREEN";
                break;
            case "#F7D117":
                color = "YELLOW";
                break;
            case "#B30006":
                color = "RED";
                break;
            case "#FFFFFF":
                color = "WHITE";
                break;
            case "#532115":
                color = "BROWN";
                break;
            case "#EE82EE":
                color = "VIOLET";
                break;
            case "#800080":
                color = "PURPLE";
                break;
            case "#00FF00":
                color = "LIME";
                break;
            case "#FFA500":
                color = "ORANGE";
                break;
            case "#FF00FF":
                color = "MAGENTA";
                break;
            case "#DC143C":
                color = "CRIMSON";
                break;
            case "#585858":
                color = "NULL";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        if ( !color.equals("NULL") ) {
            color = "INPUT_" + color + "COLOR";
        }
        this.src.add(color);
        return null;
    }

    private Void generateUsedVars() {
        for ( VarDeclaration var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                if ( var.typeVar.isArray() ) {
                    this.src.add(getLanguageVarTypeFromBlocklyType(var.typeVar), " ", "__");
                }
                this.src.add("___", var.name, var.typeVar.isArray() ? "[]" : "", " = ");
                var.value.accept(this);
                this.src.add(";");
                if ( var.typeVar.isArray() ) {
                    nlIndent();
                    this.src.add("___", var.name, " = _____", var.name, ";");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.src.add(getLanguageVarTypeFromBlocklyType(var.typeVar), " ", var.getCodeSafeName());
        if ( var.typeVar.isArray() ) {
            this.src.add("[");
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                ListCreate list = (ListCreate) var.value;
                this.src.add(list.exprList.get().size());
            }
            this.src.add("]");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        boolean isOpBasicMaths = op == Op.ADD || op == Op.MINUS || op == Op.DIVIDE || op == Op.MULTIPLY;
        if ( isOpBasicMaths ) {
            this.src.add("(");
        }
        generateSubExpr(this.src, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(" ", sym, " ");
        switch ( op ) {
            case DIVIDE:
                this.src.add("((");
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
                this.src.add(")*1.0)");
                break;
            default:
                generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        }
        if ( isOpBasicMaths ) {
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += ");
        String numToStr = textAppendStmt.text.getVarType().toString().contains("NUMBER") ? "NumToStr(" : "";
        src.add(numToStr);
        textAppendStmt.text.accept(this);
        if ( numToStr != "" ) {
            src.add(" )");
        }
        src.add(";");
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
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
                ((Binary) repeatStmt.expr).left.accept(this);
                this.src.add(";");
                nlIndent();
                this.src.add("for(int ___i = 0; ___i < ArrayLen(___", ((Var) ((Binary) repeatStmt.expr).getRight()).name, "); ++___i) {");
                incrIndentation();
                nlIndent();
                this.src.add("___", ((VarDeclaration) ((Binary) repeatStmt.expr).left).name, " = ___", ((Var) ((Binary) repeatStmt.expr).getRight()).name, "[___i];");
                decrIndentation();
                break;
            case FOREVER_ARDU:
                throw new DbcException("FOREVER_ARDU is invalid with nxt");
        }
        incrIndentation();
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
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
        this.src.add("Wait(15);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("Wait(");
        waitTimeStmt.time.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.add(NxtNxcVisitor.getMethodForShowText(showTextAction));
        this.src.add("(");
        showTextAction.x.accept(this);
        this.src.add(", (MAXLINES - ");
        showTextAction.y.accept(this);
        this.src.add(") * MAXLINES, ");
        showTextAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    public static String getMethodForShowText(ShowTextAction showTextAction) {
        String methodName;
        switch ( showTextAction.msg.getVarType() ) {
            case ARRAY_STRING:
            case STRING:
                methodName = "TextOut";
                break;
            case ARRAY_BOOLEAN:
            case BOOLEAN:
                methodName = "BoolOut";
                break;
            case COLOR:
                methodName = "ColorOut";
                break;
            case NOTHING:
                if ( showTextAction.msg.getProperty().getBlockType().contains("isPressed")
                    || showTextAction.msg.getProperty().getBlockType().contains("logic_ternary") ) {
                    methodName = "BoolOut";
                } else if ( showTextAction.msg.getProperty().getBlockType().contains("colour") ) {
                    methodName = "ColorOut";
                } else if ( showTextAction.msg.getProperty().getBlockType().contains("robSensors")
                    || showTextAction.msg.getProperty().getBlockType().contains("robActions")
                    || showTextAction.msg.toString().contains("POWER") ) {
                    methodName = "NumOut";
                } else {
                    methodName = "TextOut";
                }
                break;
            case CAPTURED_TYPE:
                if ( showTextAction.msg.toString().contains("Number")
                    || showTextAction.msg.toString().contains("ADD")
                    || showTextAction.msg.toString().contains("MINUS")
                    || showTextAction.msg.toString().contains("MULTIPLY")
                    || showTextAction.msg.toString().contains("DIVIDE")
                    || showTextAction.msg.toString().contains("MOD")
                    || showTextAction.msg.toString().contains("NEG")
                    || showTextAction.msg.toString().contains("LISTS_LENGTH")
                    || showTextAction.msg.toString().contains("IndexOfFunct")
                    || showTextAction.msg.toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [NUMBER")
                    || showTextAction.msg.toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [CONNECTION")
                    || showTextAction.msg.toString().contains("MotorGetPower")
                    || showTextAction.msg.toString().contains("VolumeAction") ) {
                    methodName = "NumOut";
                } else if ( showTextAction.msg.toString().contains("Boolean")
                    || showTextAction.msg.toString().contains("EQ")
                    || showTextAction.msg.toString().contains("NEQ")
                    || showTextAction.msg.toString().contains("LT")
                    || showTextAction.msg.toString().contains("LTE")
                    || showTextAction.msg.toString().contains("GT")
                    || showTextAction.msg.toString().contains("GTE")
                    || showTextAction.msg.toString().contains("LIST_IS_EMPTY")
                    || showTextAction.msg.toString().contains("AND")
                    || showTextAction.msg.toString().contains("OR")
                    || showTextAction.msg.toString().contains("NOT")
                    || showTextAction.msg.toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [BOOLEAN")
                    || showTextAction.msg.toString().contains("BluetoothCheckConnectAction") ) {
                    methodName = "BoolOut";
                } else {
                    methodName = "TextOut";
                }
                break;
            default:
                methodName = "NumOut";
                break;
        }
        return methodName;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("ClearScreen();");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        this.src.add("volume * 100 / 4");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.add("volume = (");
        setVolumeAction.volume.accept(this);
        this.src.add(") * 4 / 100.0;");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.src.add("PlayFile(", playFileAction.fileName, ");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("PlayToneEx(");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(", volume, false);");
        nlIndent();
        this.src.add("Wait(");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("PlayToneEx(", playNoteAction.frequency, ", ", playNoteAction.duration, ", volume, false);");
        nlIndent();
        this.src.add("Wait(", playNoteAction.duration, ");");
        return null;
    }

    private boolean isActorOnPort(String port) {
        if ( port != null ) {
            for ( UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                if ( actor.getPort().equals(port) ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String userDefinedPort = motorOnAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            final boolean reverse;
            if ( this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REVERSE) == null ) {
                reverse = false;
            } else {
                reverse = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REVERSE).equals("ON");
            }
            final boolean isDuration = motorOnAction.param.getDuration() != null;
            final boolean isRegulatedDrive;
            if ( this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REGULATION) == null ) {
                isRegulatedDrive = false;
            } else {
                isRegulatedDrive = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REGULATION).equals("TRUE");
            }
            String sign = reverse ? "-" : "";
            String methodNamePart = reverse ? "OnRev" : "OnFwd";
            if ( isDuration ) {
                this.src.add("RotateMotor(OUT_", userDefinedPort, ", ", sign, "MIN(MAX(");
                motorOnAction.param.getSpeed().accept(this);
                this.src.add(", -100), 100)");
                if ( motorOnAction.getDurationMode() == MotorMoveMode.ROTATIONS ) {
                    this.src.add(", 360 * ");
                } else {
                    this.src.add(", ");
                }
                motorOnAction.param.getDuration().getValue().accept(this);
            } else {
                if ( isRegulatedDrive ) {
                    this.src.add(methodNamePart, "RegEx(OUT_", userDefinedPort, ", MIN(MAX(");
                    motorOnAction.param.getSpeed().accept(this);
                    this.src.add(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE");
                } else {
                    this.src.add(methodNamePart, "(OUT_", userDefinedPort, ", MIN(MAX(");
                    motorOnAction.param.getSpeed().accept(this);
                    this.src.add(", -100), 100)");
                }
            }
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        String userDefinedPort = motorSetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            final boolean reverse = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
            String sign = reverse ? "-" : "";
            final String methodName = "OnFwdRegEx";
            this.src.add(methodName, "(OUT_", userDefinedPort, ", ", sign, "MIN(MAX(");
            motorSetPowerAction.power.accept(this);
            this.src.add(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE", ");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        if ( isActorOnPort(motorGetPowerAction.getUserDefinedPort()) ) {
            final String methodName = "MotorPower";
            this.src.add(methodName, "(OUT_", motorGetPowerAction.getUserDefinedPort(), ")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        if ( motorStopAction.mode == MotorStopMode.FLOAT ) {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.src.add("Float(OUT_", motorStopAction.getUserDefinedPort(), ");");
            }
        } else {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.src.add("Off(OUT_", motorStopAction.getUserDefinedPort(), ");");
            }
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;

        final boolean isDuration = driveAction.param.getDuration() != null;
        final boolean reverse = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON) || rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        final boolean localReverse = driveAction.direction == DriveDirection.BACKWARD;

        String methodName = isDuration ? "RotateMotorEx" : "OnFwdRegEx";
        this.src.add(methodName, "(OUT_");
        String port = createSortedPorts(leftMotorPort, rightMotorPort);
        this.src.add(port);
        if ( !reverse && localReverse || !localReverse && reverse ) {
            this.src.add(", -1 * ");
        } else {
            this.src.add(", ");
        }
        this.src.add("MIN(MAX(");
        driveAction.param.getSpeed().accept(this);
        this.src.add(", -100), 100)", ", ");
        if ( isDuration ) {
            this.src.add("(");
            driveAction.param.getDuration().getValue().accept(this);
            this.src.add(" * 360 / (PI * WHEELDIAMETER)), 0, true, true);");
            nlIndent();
            this.src.add("Wait(1");
        } else {
            this.src.add("OUT_REGMODE_SYNC, RESET_NONE");
        }
        this.src.add(");");

        return null;
    }

    private String createSortedPorts(String port1, String port2) {
        Assert.isTrue(port1.length() == 1 && port2.length() == 1);
        char[] charArray = (port1 + port2).toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;

        final boolean isDuration = turnAction.param.getDuration() != null;
        final boolean reverse = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON) || rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);

        int turnpct = 100;
        String methodName = isDuration ? "RotateMotorEx" : "OnFwdSync";
        this.src.add(methodName, "(OUT_");
        if ( leftMotorPort.charAt(0) < rightMotorPort.charAt(0) ) {
            turnpct *= -1;
        }
        if ( reverse ) {
            turnpct *= -1;
        }
        String sortedPort = createSortedPorts(leftMotorPort, rightMotorPort);
        this.src.add(sortedPort, ", MIN(MAX(");
        turnAction.param.getSpeed().accept(this);
        this.src.add(", -100), 100)");
        if ( turnAction.direction == TurnDirection.LEFT ) {
            turnpct *= -1;
        }
        this.src.add(", ");
        if ( isDuration ) {
            this.src.add("(");
            turnAction.param.getDuration().getValue().accept(this);
            this.src.add(" * TRACKWIDTH / WHEELDIAMETER), ", turnpct, ", true, true);");
            nlIndent();
            this.src.add("Wait(1");
        } else {
            this.src.add(turnpct);
        }
        this.src.add(");");

        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;

        final boolean isDuration = curveAction.paramLeft.getDuration() != null;
        final boolean confForward = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.OFF);
        final boolean blockForward = curveAction.direction == DriveDirection.FOREWARD;

        String methodName = isDuration ? "SteerDriveEx" : "SteerDrive";

        this.src.add(methodName, "(OUT_", leftMotorPort, ", OUT_", rightMotorPort, ", MIN(MAX(");
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", -100), 100), MIN(MAX(");
        curveAction.paramRight.getSpeed().accept(this);
        this.src.add(", -100), 100), ", confForward == blockForward);
        if ( isDuration ) {
            this.src.add(", ");
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        this.src.add(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;

        String sortedPorts = createSortedPorts(leftMotorPort, rightMotorPort);
        this.src.add("Off(OUT_", sortedPorts, ");");

        return null;
    }

    @Override
    public Void visitNxtRgbLedOnAction(NxtRgbLedOnAction nxtRgbLedOnAction) {
        this.src.add("SetSensorColor", nxtRgbLedOnAction.colour.getValues()[0], "(");
        String port = this.brickConfiguration.getConfigurationComponent(nxtRgbLedOnAction.port).internalPortName;
        this.src.add(port, ");");
        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        this.src.add("SetSensorColorNone(");
        String port = this.brickConfiguration.getConfigurationComponent(rgbLedOffAction.port).internalPortName;
        this.src.add(port, ");");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(lightSensor.getUserDefinedPort()).internalPortName;
        this.src.add("_readLightSensor(", portName, ", ");
        switch ( lightSensor.getMode() ) {
            case "LIGHT":
                this.src.add("1");
                break;
            case "AMBIENTLIGHT":
                this.src.add("2");
                break;
            default:
                throw new DbcException("Wrong mode provided for light sensor, must be AMBIENTLIGHT or LIGHT");
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("ButtonPressed(", getCodeName(keysSensor.getUserDefinedPort()), ", false)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        this.src.add("SensorColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(colorSensor.getUserDefinedPort()).internalPortName;
        this.src.add(portName, ", \"", colorSensor.getMode(), "\")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        this.src.add("SensorHtColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(htColorSensor.getUserDefinedPort()).internalPortName;
        this.src.add(portName, ", \"", htColorSensor.getMode(), "\")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(soundSensor.getUserDefinedPort()).internalPortName;
        this.src.add("Sensor(", portName, ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String userDefinedPort = encoderSensor.getUserDefinedPort();
        String mode = encoderSensor.getMode();
        switch ( mode ) {
            case SC.ROTATION:
                this.src.add("MotorTachoCount(OUT_", userDefinedPort, ") / 360.0");
                break;
            case SC.DEGREE:
                this.src.add("MotorTachoCount(OUT_", userDefinedPort, ")");
                break;
            case SC.DISTANCE:
                this.src.add("MotorTachoCount(OUT_", userDefinedPort, ") * PI / 360.0 * WHEELDIAMETER");
                break;
            default:
                throw new DbcException("Invalide encoder sensor mode:" + mode + "!");

        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String userDefinedPort = encoderReset.sensorPort;
        this.src.add("ResetTachoCount(OUT_", userDefinedPort, ");");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.src.add("(CurrentTick() - timer", timerNumber, ")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.src.add("timer", timerNumber, " = CurrentTick();");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(touchSensor.getUserDefinedPort()).internalPortName;
        this.src.add("Sensor(", portName, ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(ultrasonicSensor.getUserDefinedPort()).internalPortName;
        this.src.add("SensorUS(", portName, ")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SOUND) ) {
            this.src.add("byte volume = 0x02;");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            nlIndent();
            this.src.add("long timer1;");
        }
        //this.src.add(this.tmpArr);
        mainTask.variables.accept(this);
        nlIndent();
        this.src.add("task main() {");
        incrIndentation();
        generateUsedVars();
        generateSensors();
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        BlocklyType arrayType = indexOfFunct.value.getVarType();
        String methodName = "ArrFindFirst";
        if ( indexOfFunct.location == IndexLocation.LAST ) {
            methodName = "ArrFindLast";
        }
        switch ( arrayType ) {
            case NUMBER:
            case ARRAY_CONNECTION:
            case ARRAY_NUMBER:
            case ARRAY_COLOUR:
                methodName += "Num(";
                break;
            case STRING:
            case ARRAY_STRING:
                methodName += "Str(";
                break;
            case BOOLEAN:
            case ARRAY_BOOLEAN:
                methodName += "Bool(";
                break;
            default:
                throw new DbcException("Invalid array type: " + arrayType);
        }

        this.src.add(methodName);
        indexOfFunct.value.accept(this);
        this.src.add(", ");
        indexOfFunct.find.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        this.src.add("ArrayLen(");
        lengthOfListFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        this.src.add("ArrayLen(");
        isListEmptyFunct.value.accept(this);
        this.src.add(") == 0");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        ListElementOperations operation = (ListElementOperations) listGetIndex.getElementOperation();
        if ( !operation.equals(ListElementOperations.GET) ) {
            throw new VisitorException("Unsupported get method: " + operation);
        }
        IndexLocation location = (IndexLocation) listGetIndex.location;
        listGetIndex.param.get(0).accept(this);
        this.src.add("[");
        switch ( location ) {
            case FIRST:
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.src.add("sanitiseFromStart(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.src.add("), 0");
                break;
            case FROM_END:
                this.src.add("sanitiseFromEnd(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.src.add("), -1 - ");
                listGetIndex.param.get(1).accept(this);
                break;
            case FROM_START:
                this.src.add("sanitiseFromStart(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.src.add("), ");
                listGetIndex.param.get(1).accept(this);
                break;
            case LAST:
                this.src.add("sanitiseFromEnd(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.src.add("), -1");
                break;
            default:
                break;
        }
        this.src.add(")", "]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        ListElementOperations operation = (ListElementOperations) listSetIndex.mode;
        if ( !operation.equals(ListElementOperations.SET) ) {
            throw new VisitorException("Unsupported set method: " + operation);
        }
        IndexLocation location = (IndexLocation) listSetIndex.location;
        listSetIndex.param.get(0).accept(this);
        this.src.add("[");
        switch ( location ) {
            case FIRST:
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.src.add("sanitiseFromStart(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.src.add("), 0");
                break;
            case FROM_END:
                this.src.add("sanitiseFromEnd(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.src.add("), -1 - ");
                listSetIndex.param.get(2).accept(this);
                break;
            case FROM_START:
                this.src.add("sanitiseFromStart(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.src.add("), ");
                listSetIndex.param.get(2).accept(this);
                break;
            case LAST:
                this.src.add("sanitiseFromEnd(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.src.add("), -1");
                break;
            default:
                break;
        }

        this.src.add(")", "]", " = ");
        listSetIndex.param.get(1).accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("MIN(MAX(");
        mathConstrainFunct.value.accept(this);
        this.src.add(", ");
        mathConstrainFunct.lowerBound.accept(this);
        this.src.add("), ");
        mathConstrainFunct.upperBound.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2 == 0)");
                break;
            case ODD:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2 != 0)");
                break;
            case PRIME:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME), "(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            // % in nxc % doesn't leave a a fractional residual, e.g. 5.2%1 = 0, so it is not possible to check the wholeness by "%1", that is why
            //an additional function is used
            case WHOLE:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.WHOLE), "(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case POSITIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" > 0)");
                break;
            case NEGATIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" < 0)");
                break;
            //it would work only for whole numbers, however, I think that it makes sense to talk about being divisible only for the whole numbers
            case DIVISIBLE_BY:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add(" == 0)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case SUM:
                this.src.add("ArraySum(");
                break;
            case MIN:
                this.src.add("ArrayMin(");
                break;
            case MAX:
                this.src.add("ArrayMax(");
                break;
            case AVERAGE:
                this.src.add("ArrayMean(");
                break;
            case MEDIAN:
                this.src.add("ArrayMedian(");
                break;
            case STD_DEV:
                this.src.add("ArrayStdDev(");
                break;
            default:
                break;
        }
        mathOnListFunct.list.accept(this);
        if ( mathOnListFunct.functName == FunctionNames.RANDOM ) {
            this.src.add("[0]");
        } else {
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.src.add("Random(100) / 100");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("Random((");
        mathRandomIntFunct.to.accept(this);
        this.src.add(") - (");
        mathRandomIntFunct.from.accept(this);
        this.src.add(")) + (");
        mathRandomIntFunct.from.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case ROOT:
                this.src.add("sqrt(");
                break;
            case ABS:
                this.src.add("abs(");
                break;
            case LN:
            case LOG10:
            case SIN:
            case COS:
            case TAN:
            case ASIN:
            case ATAN:
            case ACOS:
            case ROUND:
            case ROUNDUP:
            case ROUNDDOWN:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.functName), "(");
                break;
            case EXP:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER), "(M_E, ");
                break;
            case POW10:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER), "(10, ");
                break;
            default:
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER), "(");
        mathPowerFunct.param.get(0).accept(this);
        this.src.add(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        this.src.add("( ( ");
        mathModuloFunct.dividend.accept(this);
        this.src.add(" ) % ( ");
        mathModuloFunct.divisor.accept(this);
        this.src.add(" ) )");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        // not supported by NXC
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReadAction) {
        String methodName;
        switch ( bluetoothReadAction.dataType ) {
            case "Boolean":
                methodName = "BluetoothGetBoolean(";
                break;
            case "String":
                methodName = "BluetoothGetString(";
                break;
            default:
                methodName = "BluetoothGetNumber(";
        }
        this.src.add(methodName, bluetoothReadAction.channel, ")");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        this.src.add("(BluetoothStatus(");
        bluetoothCheckConnectAction.connection.accept(this);
        this.src.add(")==NO_ERR)");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        String methodName;

        switch ( bluetoothSendAction.dataType ) {
            case "Boolean":
                methodName = "SendRemoteBool(";
                break;
            case "String":
                methodName = "SendRemoteString(";
                break;
            default:
                methodName = "SendRemoteNumber(";
        }

        this.src.add(methodName);
        bluetoothSendAction.connection.accept(this);
        this.src.add(", ", bluetoothSendAction.channel, ", ");
        bluetoothSendAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        this.src.add(connectConst.value);
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("#define WHEELDIAMETER ", this.brickConfiguration.getWheelDiameter());
        nlIndent();
        this.src.add("#define TRACKWIDTH ", this.brickConfiguration.getTrackWidth());
        nlIndent();
        this.src.add("#define MAXLINES 8");
        nlIndent();
        this.src.add("#define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))");
        nlIndent();
        this.src.add("#define MAX(X, Y) (((X) > (Y)) ? (X) : (Y))");
        nlIndent();
        this.src.add("#define M_PI PI");
        nlIndent();
        this.src.add("#define M_E 2.718281828459045");
        nlIndent();
        this.src.add("#define M_GOLDEN_RATIO 1.61803398875");
        nlIndent();
        this.src.add("#define M_SQRT2 1.41421356237");
        nlIndent();
        this.src.add("#define M_SQRT1_2 0.707106781187");
        nlIndent();
        this.src.add("#define M_INFINITY 0x7f800000");
        nlIndent();
        this.src.add("#include \"NEPODefs.h\" // contains NEPO declarations for the NXC NXT API resources");
        nlIndent();
        nlIndent();
        super.generateProgramPrefix(withWrapping);
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        decrIndentation();
        if ( withWrapping ) {
            nlIndent();
            this.src.add("}");
            nlIndent();
        }
        generateUserDefinedMethods();
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
            case ARRAY_COLOUR:
            case ARRAY_CONNECTION:
            case NUMBER_INT:
            case COLOR:
            case CONNECTION:
                return "int";
            case ARRAY_NUMBER:
            case NUMBER:
                return "float";
            case ARRAY_STRING:
            case STRING:
                return "string";
            case ARRAY_BOOLEAN:
            case BOOLEAN:
                return "bool";
            case VOID:
                return "void";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            if ( usedSensor.getType().equals(SC.TIMER) ) {
                if ( !usedSensor.getMode().equals(SC.RESET) ) {
                    nlIndent();
                    this.src.add("timer1 = CurrentTick();");
                }
                continue;
            }
            ConfigurationComponent configurationComponent = this.brickConfiguration.getConfigurationComponent(usedSensor.getPort());
            String sensorType = configurationComponent.componentType;
            nlIndent();
            if ( sensorType.equals(SC.LIGHT) ) {
                this.src.add("SetSensorLight(");
            } else {
                this.src.add("SetSensor(");
            }
            this.src.add(configurationComponent.internalPortName, ", ");

            switch ( sensorType ) {
                case SC.COLOR:
                    this.src.add("SENSOR_COLORFULL);");
                    break;
                case SC.HT_COLOR:
                case SC.ULTRASONIC:
                    this.src.add("SENSOR_LOWSPEED);");
                    break;
                case SC.LIGHT:
                    if ( usedSensor.getMode().equals("LIGHT") ) {
                        this.src.add("true);");
                    } else {
                        this.src.add("false);");
                    }
                    break;
                case SC.TOUCH:
                    this.src.add("SENSOR_TOUCH);");
                    break;
                case SC.SOUND:
                    this.src.add("SENSOR_SOUND);");
                    break;
                default:
                    break;
            }
        }
    }

    private String getCodeName(String userDefinedName) {
        switch ( userDefinedName ) {
            case "A":
            case "B":
            case "C":
                return "OUT_" + userDefinedName;
            case "1":
            case "2":
            case "3":
                return "S" + userDefinedName;
            case "ENTER":
                return "BTNCENTER";
            case "LEFT":
            case "RIGHT":
                return "BTN" + userDefinedName;
            default:
                throw new DbcException("Invalid hardware component " + userDefinedName);
        }
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
