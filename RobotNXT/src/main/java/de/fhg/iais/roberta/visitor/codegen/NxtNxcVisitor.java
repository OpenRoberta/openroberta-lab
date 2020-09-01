package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
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
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.dbc.VisitorException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INxtVisitor;
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
    public NxtNxcVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        String color = "";
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

    protected Void generateUsedVars() {
        for ( VarDeclaration<Void> var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                if ( var.getTypeVar().isArray() ) {
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
                    this.sb.append("__");
                }
                this.sb.append("___" + var.getName());
                this.sb.append(var.getTypeVar().isArray() ? "[]" : "");
                this.sb.append(" = ");
                var.getValue().accept(this);
                this.sb.append(";");
                if ( var.getTypeVar().isArray() ) {
                    nlIndent();
                    //this.sb.append("for(int i = 0; i < ArrayLen(" + var.getName() + "); i++) {");
                    //incrIndentation();
                    //nlIndent();
                    this.sb.append("___" + var.getName()).append(" = _____" + var.getName() + ";");
                    //decrIndentation();
                    //nlIndent();
                    //this.sb.append("}");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
        this.sb.append(var.getCodeSafeName());
        if ( var.getTypeVar().isArray() ) {
            this.sb.append("[");
            if ( var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                // nothing to do
            } else {
                ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                this.sb.append(list.getValue().get().size());
            }
            this.sb.append("]");
        }
        return null;
    }

    //TODO: fix the string concatenation
    @Override
    public Void visitBinary(Binary<Void> binary) {
        Op op = binary.getOp();
        if ( op == Op.ADD || op == Op.MINUS || op == Op.DIVIDE || op == Op.MULTIPLY ) {
            this.sb.append("(");
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
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
        if ( op == Op.ADD || op == Op.MINUS || op == Op.DIVIDE || op == Op.MULTIPLY ) {
            this.sb.append(")");
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
                ((VarDeclaration<Void>) ((Binary<Void>) repeatStmt.getExpr()).getLeft()).accept(this);
                this.sb.append(";");
                nlIndent();
                this.sb.append("for(int ___i = 0; ___i < ArrayLen(___");
                this.sb.append(((Var<Void>) ((Binary<Void>) repeatStmt.getExpr()).getRight()).getValue());
                this.sb.append("); ++___i) {");
                incrIndentation();
                nlIndent();
                this.sb.append("___");
                this.sb.append(((VarDeclaration<Void>) ((Binary<Void>) repeatStmt.getExpr()).getLeft()).getName());
                this.sb.append(" = ___");
                this.sb.append(((Var<Void>) ((Binary<Void>) repeatStmt.getExpr()).getRight()).getValue());
                this.sb.append("[___i];");
                decrIndentation();
        }
        incrIndentation();
        repeatStmt.getList().accept(this);
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
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("Wait(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Wait(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append(getMethodForShowText(showTextAction));
        this.sb.append("(");
        showTextAction.getX().accept(this);
        this.sb.append(", (MAXLINES - ");
        showTextAction.getY().accept(this);

        this.sb.append(") * MAXLINES, ");
        showTextAction.getMsg().accept(this);
        this.sb.append(");");
        return null;
    }

    public static String getMethodForShowText(ShowTextAction<Void> showTextAction) {
        String methodName;
        switch ( showTextAction.getMsg().getVarType() ) {
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
                if ( showTextAction.getMsg().getProperty().getBlockType().contains("isPressed")
                    || showTextAction.getMsg().getProperty().getBlockType().contains("logic_ternary") ) {
                    methodName = "BoolOut";
                } else if ( showTextAction.getMsg().getProperty().getBlockType().contains("colour") ) {
                    methodName = "ColorOut";
                } else if ( showTextAction.getMsg().getProperty().getBlockType().contains("robSensors")
                    || showTextAction.getMsg().getProperty().getBlockType().contains("robActions")
                    || showTextAction.getMsg().toString().contains("POWER") ) {
                    methodName = "NumOut";
                } else {
                    methodName = "TextOut";
                }
                break;
            case CAPTURED_TYPE:
                if ( showTextAction.getMsg().toString().contains("Number")
                    || showTextAction.getMsg().toString().contains("ADD")
                    || showTextAction.getMsg().toString().contains("MINUS")
                    || showTextAction.getMsg().toString().contains("MULTIPLY")
                    || showTextAction.getMsg().toString().contains("DIVIDE")
                    || showTextAction.getMsg().toString().contains("MOD")
                    || showTextAction.getMsg().toString().contains("NEG")
                    || showTextAction.getMsg().toString().contains("LISTS_LENGTH")
                    || showTextAction.getMsg().toString().contains("IndexOfFunct")
                    || showTextAction.getMsg().toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [NUMBER")
                    || showTextAction.getMsg().toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [CONNECTION")
                    || showTextAction.getMsg().toString().contains("MotorGetPower")
                    || showTextAction.getMsg().toString().contains("VolumeAction") ) {
                    methodName = "NumOut";
                } else if ( showTextAction.getMsg().toString().contains("Boolean")
                    || showTextAction.getMsg().toString().contains("EQ")
                    || showTextAction.getMsg().toString().contains("NEQ")
                    || showTextAction.getMsg().toString().contains("LT")
                    || showTextAction.getMsg().toString().contains("LTE")
                    || showTextAction.getMsg().toString().contains("GT")
                    || showTextAction.getMsg().toString().contains("GTE")
                    || showTextAction.getMsg().toString().contains("LIST_IS_EMPTY")
                    || showTextAction.getMsg().toString().contains("AND")
                    || showTextAction.getMsg().toString().contains("OR")
                    || showTextAction.getMsg().toString().contains("NOT")
                    || showTextAction.getMsg().toString().contains("[ListGetIndex [GET, FROM_START, [ListCreate [BOOLEAN")
                    || showTextAction.getMsg().toString().contains("BluetoothConnectAction") ) {
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
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("ClearScreen();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("volume = (");
                volumeAction.getVolume().accept(this);
                this.sb.append(") * 4 / 100.0;");
                break;
            case GET:
                this.sb.append("volume * 100 / 4");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("PlayFile(" + playFileAction.getFileName() + ");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("PlayToneEx(");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(", volume, false);");
        nlIndent();
        this.sb.append("Wait(");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("PlayToneEx(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append(", ");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append(", volume, false);");
        nlIndent();
        this.sb.append("Wait(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append(");");
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
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String userDefinedPort = motorOnAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            final boolean reverse;
            if ( this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REVERSE) == null ) {
                reverse = false;
            } else {
                reverse = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REVERSE).equals("ON");
            }
            final boolean isDuration = motorOnAction.getParam().getDuration() != null;
            final boolean isRegulatedDrive;
            if ( this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REGULATION) == null ) {
                isRegulatedDrive = false;
            } else {
                isRegulatedDrive = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getOptProperty(SC.MOTOR_REGULATION).equals("TRUE");
            }
            String sign = reverse ? "-" : "";
            String methodNamePart = reverse ? "OnRev" : "OnFwd";
            if ( isDuration ) {
                this.sb.append("RotateMotor(OUT_" + userDefinedPort + ", " + sign + "MIN(MAX(");
                motorOnAction.getParam().getSpeed().accept(this);
                this.sb.append(", -100), 100)");
                if ( motorOnAction.getDurationMode() == MotorMoveMode.ROTATIONS ) {
                    this.sb.append(", 360 * ");
                } else {
                    this.sb.append(", ");
                }
                motorOnAction.getParam().getDuration().getValue().accept(this);
            } else {
                if ( isRegulatedDrive ) {
                    this.sb.append(methodNamePart + "RegEx(OUT_" + userDefinedPort + ", MIN(MAX(");
                    motorOnAction.getParam().getSpeed().accept(this);
                    this.sb.append(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE");
                } else {
                    this.sb.append(methodNamePart + "(OUT_" + userDefinedPort + ", MIN(MAX(");
                    motorOnAction.getParam().getSpeed().accept(this);
                    this.sb.append(", -100), 100)");
                }
            }
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String userDefinedPort = motorSetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            final boolean reverse = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
            String sign = reverse ? "-" : "";
            final String methodName = "OnFwdRegEx";
            //final boolean isRegulated = brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
            this.sb.append(methodName + "(OUT_" + userDefinedPort + ", " + sign + "MIN(MAX(");
            motorSetPowerAction.getPower().accept(this);
            this.sb.append(", -100), 100), OUT_REGMODE_SPEED, RESET_NONE");
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( isActorOnPort(motorGetPowerAction.getUserDefinedPort()) ) {
            final String methodName = "MotorPower";
            this.sb.append(methodName + "(OUT_" + motorGetPowerAction.getUserDefinedPort());
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.sb.append("Float(OUT_" + motorStopAction.getUserDefinedPort());
                this.sb.append(");");
            }
        } else {
            if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
                this.sb.append("Off(OUT_" + motorStopAction.getUserDefinedPort());
                this.sb.append(");");
            }
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();

        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON) || rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;

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
        driveAction.getParam().getSpeed().accept(this);
        this.sb.append(", -100), 100)").append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            driveAction.getParam().getDuration().getValue().accept(this);
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
        String port = new String(charArray);
        return port;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();

        final boolean isDuration = turnAction.getParam().getDuration() != null;
        final boolean reverse = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON) || rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);

        int turnpct = 100;
        String methodName = isDuration ? "RotateMotorEx" : "OnFwdSync";
        this.sb.append(methodName + "(OUT_");
        if ( leftMotorPort.charAt(0) < rightMotorPort.charAt(0) ) {
            turnpct *= -1;
        }
        if ( reverse ) {
            turnpct *= -1;
        }
        String sortedPort = createSortedPorts(leftMotorPort, rightMotorPort);
        this.sb.append(sortedPort).append(", MIN(MAX(");
        turnAction.getParam().getSpeed().accept(this);
        this.sb.append(", -100), 100)");
        if ( turnAction.getDirection() == TurnDirection.LEFT ) {
            turnpct *= -1;
        }
        this.sb.append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            turnAction.getParam().getDuration().getValue().accept(this);
            this.sb.append(" * TRACKWIDTH / WHEELDIAMETER), " + turnpct + ", true, true);");
            nlIndent();
            this.sb.append("Wait(1");
        } else {
            this.sb.append(turnpct);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();

        final boolean isDuration = curveAction.getParamLeft().getDuration() != null;
        final boolean confForward = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.OFF);
        final boolean blockForward = curveAction.getDirection() == DriveDirection.FOREWARD;

        String methodName = isDuration ? "SteerDriveEx" : "SteerDrive";

        this.sb.append(methodName).append("(OUT_").append(leftMotorPort);
        this.sb.append(", OUT_").append(rightMotorPort);
        this.sb.append(", MIN(MAX(");
        curveAction.getParamLeft().getSpeed().accept(this);
        this.sb.append(", -100), 100), MIN(MAX(");
        curveAction.getParamRight().getSpeed().accept(this);
        this.sb.append(", -100), 100), ");
        this.sb.append(confForward == blockForward);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();

        String sortedPorts = createSortedPorts(leftMotorPort, rightMotorPort);
        this.sb.append("Off(OUT_").append(sortedPorts).append(");");

        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( lightAction.getMode().toString().equals("ON") ) {
            this.sb.append("SetSensorColor" + lightAction.getColor().getValues()[0] + "(");
        } else {
            this.sb.append("SetSensorColorNone(");
        }
        this.sb.append(lightAction.getPort());
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(lightSensor.getPort()).getInternalPortName();
        this.sb.append("_readLightSensor(");
        this.sb.append(portName);
        this.sb.append(", ");
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
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("ButtonPressed(" + getCodeName(keysSensor.getPort()) + ", false)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.sb.append("SensorColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(colorSensor.getPort()).getInternalPortName();
        this.sb.append(portName).append(", \"").append(colorSensor.getMode()).append("\")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        this.sb.append("SensorHtColor(");
        String portName = this.brickConfiguration.getConfigurationComponent(htColorSensor.getPort()).getInternalPortName();
        this.sb.append(portName).append(", \"").append(htColorSensor.getMode()).append("\")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(soundSensor.getPort()).getInternalPortName();
        this.sb.append("Sensor(");
        this.sb.append(portName);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String userDefinedPort = encoderSensor.getPort();
        String mode = encoderSensor.getMode();
        switch ( mode ) {
            case SC.RESET:
                this.sb.append("ResetTachoCount(OUT_" + userDefinedPort + ");");
                break;
            case SC.ROTATION:
                this.sb.append("MotorTachoCount(OUT_" + userDefinedPort + ") / 360.0");
                break;
            case SC.DEGREE:
                this.sb.append("MotorTachoCount(OUT_" + userDefinedPort + ")");
                break;
            case SC.DISTANCE:
                this.sb.append("MotorTachoCount(OUT_" + userDefinedPort + ") * PI / 360.0 * WHEELDIAMETER");
                break;
            default:
                throw new DbcException("Invalide encoder sensor mode:" + mode + "!");

        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort();
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("(CurrentTick() - timer" + timerNumber + ")");
                break;
            case SC.RESET:
                this.sb.append("timer" + timerNumber + " = CurrentTick();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(touchSensor.getPort()).getInternalPortName();
        this.sb.append("Sensor(" + portName);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String portName = this.brickConfiguration.getConfigurationComponent(ultrasonicSensor.getPort()).getInternalPortName();
        this.sb.append("SensorUS(" + portName + ")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SOUND) ) {
            this.sb.append("byte volume = 0x02;");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            nlIndent();
            this.sb.append("long timer1;");
        }
        //this.sb.append(this.tmpArr);
        mainTask.getVariables().accept(this);
        nlIndent();
        this.sb.append("task main() {");
        incrIndentation();
        generateUsedVars();
        generateSensors();
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        BlocklyType arrayType = indexOfFunct.getParam().get(0).getVarType();
        String methodName = "ArrFindFirst";
        if ( indexOfFunct.getLocation() == IndexLocation.LAST ) {
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
        indexOfFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            String methodName = "ArrayLen(";
            this.sb.append(methodName);
            lengthOfIsEmptyFunct.getParam().get(0).accept(this);
            this.sb.append(") == 0");
        } else {
            String methodName = "ArrayLen(";
            this.sb.append(methodName);
            lengthOfIsEmptyFunct.getParam().get(0).accept(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        ListElementOperations operation = (ListElementOperations) listGetIndex.getElementOperation();
        if ( !operation.equals(ListElementOperations.GET) ) {
            throw new VisitorException("Unsupported get method: " + operation.toString());
        }
        IndexLocation location = (IndexLocation) listGetIndex.getLocation();
        listGetIndex.getParam().get(0).accept(this);
        this.sb.append("[");
        switch ( location ) {
            case FIRST:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append("), 0");
                break;
            case FROM_END:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append("), -1 - ");
                listGetIndex.getParam().get(1).accept(this);
                break;
            case FROM_START:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append("), ");
                listGetIndex.getParam().get(1).accept(this);
                break;
            case LAST:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append("), -1");
                break;
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append("), 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        ListElementOperations operation = (ListElementOperations) listSetIndex.getElementOperation();
        if ( !operation.equals(ListElementOperations.SET) ) {
            throw new VisitorException("Unsupported set method: " + operation.toString());
        }
        IndexLocation location = (IndexLocation) listSetIndex.getLocation();
        listSetIndex.getParam().get(0).accept(this);
        this.sb.append("[");
        switch ( location ) {
            case FIRST:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append("), 0");
                break;
            case FROM_END:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append("), -1 - ");
                listSetIndex.getParam().get(2).accept(this);
                break;
            case FROM_START:
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append("), ");
                listSetIndex.getParam().get(2).accept(this);
                break;
            case LAST:
                this.sb.append("sanitiseFromEnd(ArrayLen(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append("), -1");
                break;
            case RANDOM:
                // provided for backwards compatibility,
                // frontend does not have an option to choose this
                // but old programs may contain this option
                this.sb.append("sanitiseFromStart(ArrayLen(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append("), 0");
                break;
            default:
                break;
        }

        this.sb.append(")");
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(1).accept(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("MIN(MAX(");
        mathConstrainFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).accept(this);
        this.sb.append("), ");
        mathConstrainFunct.getParam().get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 2 == 0)");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % 2 != 0)");
                break;
            case PRIME:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.PRIME));
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            // % in nxc % doesn't leave a a fractional residual, e.g. 5.2%1 = 0, so it is not possible to check the wholeness by "%1", that is why
            //an additional function is used
            case WHOLE:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.WHOLE));
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" > 0)");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" < 0)");
                break;
            //it would work only for whole numbers, however, I think that it makes sense to talk about being divisible only for the whole numbers
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).accept(this);
                this.sb.append(" % ");
                mathNumPropFunct.getParam().get(1).accept(this);
                this.sb.append(" == 0)");
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
        mathOnListFunct.getParam().get(0).accept(this);
        if ( mathOnListFunct.getFunctName() == FunctionNames.RANDOM ) {
            this.sb.append("[0]");
        } else {
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("Random(100) / 100");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("Random(");
        mathRandomIntFunct.getParam().get(1).accept(this);
        this.sb.append(" - ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(") + ");
        mathRandomIntFunct.getParam().get(0).accept(this);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
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
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.getFunctName()));
                this.sb.append("(");
                break;
            case EXP:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
                this.sb.append("(M_E, ");
                break;
            case POW10:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
                this.sb.append("(10, ");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
        this.sb.append("(");
        mathPowerFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        // not supported by NXC
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        String methodName;
        switch ( bluetoothReadAction.getDataType() ) {
            case "Boolean":
                methodName = "BluetoothGetBoolean(";
                break;
            case "String":
                methodName = "BluetoothGetString(";
                break;
            default:
                methodName = "BluetoothGetNumber(";
        }
        this.sb.append(methodName);
        this.sb.append(bluetoothReadAction.getChannel());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        this.sb.append("(BluetoothStatus(");
        bluetoothCheckConnectAction.getConnection().accept(this);
        this.sb.append(")==NO_ERR)");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        String methodName;

        switch ( bluetoothSendAction.getDataType() ) {
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
        bluetoothSendAction.getConnection().accept(this);
        this.sb.append(", ");
        this.sb.append(bluetoothSendAction.getChannel());
        this.sb.append(", ");
        bluetoothSendAction.getMsg().accept(this);
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
                return "int";
            case ARRAY_NUMBER:
                return "float";
            case ARRAY_STRING:
                return "string";
            case ARRAY_BOOLEAN:
                return "bool";
            case ARRAY_COLOUR:
                return "int";
            case ARRAY_CONNECTION:
                return "int";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "float";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "string";
            case VOID:
                return "void";
            case COLOR:
                return "int";
            case CONNECTION:
                return "int";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private void generateSensors() {
        Map<String, UsedSensor> usedSensorMap = new HashMap<>();
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            nlIndent();
            this.sb.append("SetSensor(");
            ConfigurationComponent configurationComponent = this.brickConfiguration.getConfigurationComponent(usedSensor.getPort());
            String sensorType = configurationComponent.getComponentType();
            this.sb.append(configurationComponent.getInternalPortName()).append(", ");

            switch ( sensorType ) {
                case SC.COLOR:
                    this.sb.append("SENSOR_COLORFULL);");
                    break;
                case SC.HT_COLOR:
                    this.sb.append("SENSOR_LOWSPEED);");
                    break;
                case SC.LIGHT:
                    this.sb.append("SENSOR_LIGHT);");
                    break;
                case SC.TOUCH:
                    this.sb.append("SENSOR_TOUCH);");
                    break;
                case SC.ULTRASONIC:
                    this.sb.append("SENSOR_LOWSPEED);");
                    break;
                case SC.SOUND:
                    this.sb.append("SENSOR_SOUND);");
                    break;
                default:
                    break;
            }
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER) ) {
            nlIndent();
            this.sb.append("timer1 = CurrentTick();");
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
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
