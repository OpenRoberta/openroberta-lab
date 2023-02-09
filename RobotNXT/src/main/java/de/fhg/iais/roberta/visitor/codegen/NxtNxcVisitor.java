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
import de.fhg.iais.roberta.syntax.action.light.LightAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
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
        this.sb.append(color);
        return null;
    }

    private Void generateUsedVars() {
        for ( VarDeclaration var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                if ( var.typeVar.isArray() ) {
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.typeVar)).append(" ").append("__");
                }
                this.sb.append("___").append(var.name).append(var.typeVar.isArray() ? "[]" : "").append(" = ");
                var.value.accept(this);
                this.sb.append(";");
                if ( var.typeVar.isArray() ) {
                    nlIndent();
                    this.sb.append("___").append(var.name).append(" = _____").append(var.name).append(";");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.typeVar)).append(" ").append(var.getCodeSafeName());
        if ( var.typeVar.isArray() ) {
            this.sb.append("[");
            if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
                ListCreate list = (ListCreate) var.value;
                this.sb.append(list.exprList.get().size());
            }
            this.sb.append("]");
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        boolean isOpBasicMaths = op == Op.ADD || op == Op.MINUS || op == Op.DIVIDE || op == Op.MULTIPLY;
        if ( isOpBasicMaths ) {
            this.sb.append("(");
        }
        generateSubExpr(this.sb, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(" ").append(sym).append(" ");
        switch ( op ) {
            case TEXT_APPEND:
                if ( binary.getRight().getVarType().toString().contains("NUMBER") ) {
                    this.sb.append("NumToStr(");
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                    this.sb.append(")");
                } else {
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                }
                break;
            case DIVIDE:
                this.sb.append("((");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")*1.0)");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        if ( isOpBasicMaths ) {
            this.sb.append(")");
        }
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
                this.sb.append(";");
                nlIndent();
                this.sb.append("for(int ___i = 0; ___i < ArrayLen(___").append(((Var) ((Binary) repeatStmt.expr).getRight()).name).append("); ++___i) {");
                incrIndentation();
                nlIndent();
                this.sb.append("___").append(((VarDeclaration) ((Binary) repeatStmt.expr).left).name).append(" = ___").append(((Var) ((Binary) repeatStmt.expr).getRight()).name).append("[___i];");
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
        this.sb.append("Wait(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("Wait(");
        waitTimeStmt.time.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.sb.append(NxtNxcVisitor.getMethodForShowText(showTextAction));
        this.sb.append("(");
        showTextAction.x.accept(this);
        this.sb.append(", (MAXLINES - ");
        showTextAction.y.accept(this);
        this.sb.append(") * MAXLINES, ");
        showTextAction.msg.accept(this);
        this.sb.append(");");
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
        this.sb.append("ClearScreen();");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        this.sb.append("volume * 100 / 4");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.sb.append("volume = (");
        setVolumeAction.volume.accept(this);
        this.sb.append(") * 4 / 100.0;");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.sb.append("PlayFile(").append(playFileAction.fileName).append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.sb.append("PlayToneEx(");
        toneAction.frequency.accept(this);
        this.sb.append(", ");
        toneAction.duration.accept(this);
        this.sb.append(", volume, false);");
        nlIndent();
        this.sb.append("Wait(");
        toneAction.duration.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.sb.append("PlayToneEx(").append(playNoteAction.frequency).append(", ").append(playNoteAction.duration).append(", volume, false);");
        nlIndent();
        this.sb.append("Wait(").append(playNoteAction.duration).append(");");
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
                this.sb.append("RotateMotor(OUT_").append(userDefinedPort).append(", ").append(sign).append("MIN(MAX(");
                motorOnAction.param.getSpeed().accept(this);
                this.sb.append(", -100), 100)");
                if ( motorOnAction.getDurationMode() == MotorMoveMode.ROTATIONS ) {
                    this.sb.append(", 360 * ");
                } else {
                    this.sb.append(", ");
                }
                motorOnAction.param.getDuration().getValue().accept(this);
            } else {
                if ( isRegulatedDrive ) {
                    this.sb.append(methodNamePart).append("RegEx(OUT_").append(userDefinedPort).append(", MIN(MAX(");
                    motorOnAction.param.getSpeed().accept(this);
                    this.sb.append(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE");
                } else {
                    this.sb.append(methodNamePart).append("(OUT_").append(userDefinedPort).append(", MIN(MAX(");
                    motorOnAction.param.getSpeed().accept(this);
                    this.sb.append(", -100), 100)");
                }
            }
            this.sb.append(");");
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
            this.sb.append(methodName + "(OUT_").append(userDefinedPort).append(", ").append(sign).append("MIN(MAX(");
            motorSetPowerAction.power.accept(this);
            this.sb.append(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE").append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        if ( isActorOnPort(motorGetPowerAction.getUserDefinedPort()) ) {
            final String methodName = "MotorPower";
            this.sb.append(methodName + "(OUT_").append(motorGetPowerAction.getUserDefinedPort()).append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        if ( motorStopAction.mode == MotorStopMode.FLOAT ) {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.sb.append("Float(OUT_").append(motorStopAction.getUserDefinedPort()).append(");");
            }
        } else {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.sb.append("Off(OUT_").append(motorStopAction.getUserDefinedPort()).append(");");
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
        this.sb.append(methodName).append("(OUT_");
        String port = createSortedPorts(leftMotorPort, rightMotorPort);
        this.sb.append(port);
        if ( !reverse && localReverse || !localReverse && reverse ) {
            this.sb.append(", -1 * ");
        } else {
            this.sb.append(", ");
        }
        this.sb.append("MIN(MAX(");
        driveAction.param.getSpeed().accept(this);
        this.sb.append(", -100), 100)").append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            driveAction.param.getDuration().getValue().accept(this);
            this.sb.append(" * 360 / (PI * WHEELDIAMETER)), 0, true, true);");
            nlIndent();
            this.sb.append("Wait(1");
        } else {
            this.sb.append("OUT_REGMODE_SYNC, RESET_NONE");
        }
        this.sb.append(");");

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
        this.sb.append(methodName).append("(OUT_");
        if ( leftMotorPort.charAt(0) < rightMotorPort.charAt(0) ) {
            turnpct *= -1;
        }
        if ( reverse ) {
            turnpct *= -1;
        }
        String sortedPort = createSortedPorts(leftMotorPort, rightMotorPort);
        this.sb.append(sortedPort).append(", MIN(MAX(");
        turnAction.param.getSpeed().accept(this);
        this.sb.append(", -100), 100)");
        if ( turnAction.direction == TurnDirection.LEFT ) {
            turnpct *= -1;
        }
        this.sb.append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            turnAction.param.getDuration().getValue().accept(this);
            this.sb.append(" * TRACKWIDTH / WHEELDIAMETER), ").append(turnpct).append(", true, true);");
            nlIndent();
            this.sb.append("Wait(1");
        } else {
            this.sb.append(turnpct);
        }
        this.sb.append(");");

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

        this.sb.append(methodName).append("(OUT_").append(leftMotorPort).append(", OUT_").append(rightMotorPort).append(", MIN(MAX(");
        curveAction.paramLeft.getSpeed().accept(this);
        this.sb.append(", -100), 100), MIN(MAX(");
        curveAction.paramRight.getSpeed().accept(this);
        this.sb.append(", -100), 100), ").append(confForward == blockForward);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;

        String sortedPorts = createSortedPorts(leftMotorPort, rightMotorPort);
        this.sb.append("Off(OUT_").append(sortedPorts).append(");");

        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        if ( lightAction.mode.toString().equals("ON") ) {
            this.sb.append("SetSensorColor").append(lightAction.color.getValues()[0]).append("(");
        } else {
            this.sb.append("SetSensorColorNone(");
        }
        String port = this.brickConfiguration.getConfigurationComponent(lightAction.port).internalPortName;
        this.sb.append(port).append(");");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(lightSensor.getUserDefinedPort()).internalPortName;
        this.sb.append("_readLightSensor(").append(portName).append(", ");
        switch ( lightSensor.getMode() ) {
            case "LIGHT":
                this.sb.append("1");
                break;
            case "AMBIENTLIGHT":
                this.sb.append("2");
                break;
            default:
                throw new DbcException("Wrong mode provided for light sensor, must be AMBIENTLIGHT or LIGHT");
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.sb.append("ButtonPressed(").append(getCodeName(keysSensor.getUserDefinedPort())).append(", false)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        this.sb.append("SensorColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(colorSensor.getUserDefinedPort()).internalPortName;
        this.sb.append(portName).append(", \"").append(colorSensor.getMode()).append("\")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        this.sb.append("SensorHtColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(htColorSensor.getUserDefinedPort()).internalPortName;
        this.sb.append(portName).append(", \"").append(htColorSensor.getMode()).append("\")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(soundSensor.getUserDefinedPort()).internalPortName;
        this.sb.append("Sensor(").append(portName).append(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String userDefinedPort = encoderSensor.getUserDefinedPort();
        String mode = encoderSensor.getMode();
        switch ( mode ) {
            case SC.ROTATION:
                this.sb.append("MotorTachoCount(OUT_").append(userDefinedPort).append(") / 360.0");
                break;
            case SC.DEGREE:
                this.sb.append("MotorTachoCount(OUT_").append(userDefinedPort).append(")");
                break;
            case SC.DISTANCE:
                this.sb.append("MotorTachoCount(OUT_").append(userDefinedPort).append(") * PI / 360.0 * WHEELDIAMETER");
                break;
            default:
                throw new DbcException("Invalide encoder sensor mode:" + mode + "!");

        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String userDefinedPort = encoderReset.sensorPort;
        this.sb.append("ResetTachoCount(OUT_").append(userDefinedPort).append(");");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.sb.append("(CurrentTick() - timer").append(timerNumber).append(")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.sb.append("timer").append(timerNumber).append(" = CurrentTick();");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(touchSensor.getUserDefinedPort()).internalPortName;
        this.sb.append("Sensor(").append(portName).append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(ultrasonicSensor.getUserDefinedPort()).internalPortName;
        this.sb.append("SensorUS(").append(portName).append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SOUND) ) {
            this.sb.append("byte volume = 0x02;");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            nlIndent();
            this.sb.append("long timer1;");
        }
        //this.sb.append(this.tmpArr);
        mainTask.variables.accept(this);
        nlIndent();
        this.sb.append("task main() {");
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
        BlocklyType arrayType = indexOfFunct.param.get(0).getVarType();
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

        this.sb.append(methodName);
        indexOfFunct.param.get(0).accept(this);
        this.sb.append(", ");
        indexOfFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.functName == FunctionNames.LIST_IS_EMPTY ) {
            String methodName = "ArrayLen(";
            this.sb.append(methodName);
            lengthOfIsEmptyFunct.param.get(0).accept(this);
            this.sb.append(") == 0");
        } else {
            String methodName = "ArrayLen(";
            this.sb.append(methodName);
            lengthOfIsEmptyFunct.param.get(0).accept(this);
            this.sb.append(")");
        }
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
        this.sb.append("[");
        switch ( location ) {
            case FIRST:
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.sb.append("), 0");
                break;
            case FROM_END:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.sb.append("), -1 - ");
                listGetIndex.param.get(1).accept(this);
                break;
            case FROM_START:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.sb.append("), ");
                listGetIndex.param.get(1).accept(this);
                break;
            case LAST:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listGetIndex.param.get(0).accept(this);
                this.sb.append("), -1");
                break;
            default:
                break;
        }
        this.sb.append(")").append("]");
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
        this.sb.append("[");
        switch ( location ) {
            case FIRST:
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.sb.append("), 0");
                break;
            case FROM_END:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.sb.append("), -1 - ");
                listSetIndex.param.get(2).accept(this);
                break;
            case FROM_START:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.sb.append("), ");
                listSetIndex.param.get(2).accept(this);
                break;
            case LAST:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listSetIndex.param.get(0).accept(this);
                this.sb.append("), -1");
                break;
            default:
                break;
        }

        this.sb.append(")").append("]").append(" = ");
        listSetIndex.param.get(1).accept(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.sb.append("MIN(MAX(");
        mathConstrainFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.param.get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.param.get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % 2 == 0)");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % 2 != 0)");
                break;
            case PRIME:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME)).append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            // % in nxc % doesn't leave a a fractional residual, e.g. 5.2%1 = 0, so it is not possible to check the wholeness by "%1", that is why
            //an additional function is used
            case WHOLE:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.WHOLE)).append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" > 0)");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" < 0)");
                break;
            //it would work only for whole numbers, however, I think that it makes sense to talk about being divisible only for the whole numbers
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.sb.append(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.sb.append(" == 0)");
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
                this.sb.append("ArraySum(");
                break;
            case MIN:
                this.sb.append("ArrayMin(");
                break;
            case MAX:
                this.sb.append("ArrayMax(");
                break;
            case AVERAGE:
                this.sb.append("ArrayMean(");
                break;
            case MEDIAN:
                this.sb.append("ArrayMedian(");
                break;
            case STD_DEV:
                this.sb.append("ArrayStdDev(");
                break;
            default:
                break;
        }
        mathOnListFunct.param.get(0).accept(this);
        if ( mathOnListFunct.functName == FunctionNames.RANDOM ) {
            this.sb.append("[0]");
        } else {
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.sb.append("Random(100) / 100");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.sb.append("Random(");
        mathRandomIntFunct.param.get(1).accept(this);
        this.sb.append(" - ");
        mathRandomIntFunct.param.get(0).accept(this);
        this.sb.append(") + ");
        mathRandomIntFunct.param.get(0).accept(this);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case ROOT:
                this.sb.append("sqrt(");
                break;
            case ABS:
                this.sb.append("abs(");
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
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.functName)).append("(");
                break;
            case EXP:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER)).append("(M_E, ");
                break;
            case POW10:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER)).append("(10, ");
                break;
            default:
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER)).append("(");
        mathPowerFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.sb.append(")");
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
        this.sb.append(methodName).append(bluetoothReadAction.channel).append(")");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        this.sb.append("(BluetoothStatus(");
        bluetoothCheckConnectAction.connection.accept(this);
        this.sb.append(")==NO_ERR)");
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

        this.sb.append(methodName);
        bluetoothSendAction.connection.accept(this);
        this.sb.append(", ").append(bluetoothSendAction.channel).append(", ");
        bluetoothSendAction.msg.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#define WHEELDIAMETER ").append(this.brickConfiguration.getWheelDiameter());
        nlIndent();
        this.sb.append("#define TRACKWIDTH ").append(this.brickConfiguration.getTrackWidth());
        nlIndent();
        this.sb.append("#define MAXLINES 8");
        nlIndent();
        this.sb.append("#define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))");
        nlIndent();
        this.sb.append("#define MAX(X, Y) (((X) > (Y)) ? (X) : (Y))");
        nlIndent();
        this.sb.append("#define M_PI PI");
        nlIndent();
        this.sb.append("#define M_E 2.718281828459045");
        nlIndent();
        this.sb.append("#define M_GOLDEN_RATIO 1.61803398875");
        nlIndent();
        this.sb.append("#define M_SQRT2 1.41421356237");
        nlIndent();
        this.sb.append("#define M_SQRT1_2 0.707106781187");
        nlIndent();
        this.sb.append("#define M_INFINITY 0x7f800000");
        nlIndent();
        this.sb.append("#include \"NEPODefs.h\" // contains NEPO declarations for the NXC NXT API resources");
        nlIndent();
        nlIndent();
        super.generateProgramPrefix(withWrapping);
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        decrIndentation();
        if ( withWrapping ) {
            nlIndent();
            this.sb.append("}");
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
                    this.sb.append("timer1 = CurrentTick();");
                }
                continue;
            }
            ConfigurationComponent configurationComponent = this.brickConfiguration.getConfigurationComponent(usedSensor.getPort());
            String sensorType = configurationComponent.componentType;
            nlIndent();
            if ( sensorType.equals(SC.LIGHT) ) {
                this.sb.append("SetSensorLight(");
            } else {
                this.sb.append("SetSensor(");
            }
            this.sb.append(configurationComponent.internalPortName).append(", ");

            switch ( sensorType ) {
                case SC.COLOR:
                    this.sb.append("SENSOR_COLORFULL);");
                    break;
                case SC.HT_COLOR:
                case SC.ULTRASONIC:
                    this.sb.append("SENSOR_LOWSPEED);");
                    break;
                case SC.LIGHT:
                    if ( usedSensor.getMode().equals("LIGHT") ) {
                        this.sb.append("true);");
                    } else {
                        this.sb.append("false);");
                    }
                    break;
                case SC.TOUCH:
                    this.sb.append("SENSOR_TOUCH);");
                    break;
                case SC.SOUND:
                    this.sb.append("SENSOR_SOUND);");
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
