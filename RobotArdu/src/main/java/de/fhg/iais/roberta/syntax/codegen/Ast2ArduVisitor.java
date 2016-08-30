package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.ArduConfiguration;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.arduino.ActorPort;
import de.fhg.iais.roberta.mode.action.arduino.BlinkMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.arduino.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.arduino.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.arduino.TimerSensorMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class Ast2ArduVisitor implements AstVisitor<Void> {
    public static final String INDENT = "    ";

    private final ArduConfiguration brickConfiguration;
    private final StringBuilder sb = new StringBuilder();
    private int indentation;
    private boolean timeSensorUsed;

    /**
     * initialize the Java code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param usedFunctions in the current program
     * @param indentation to start with. Will be incr/decr depending on block structure
     */

    public Ast2ArduVisitor(ArduConfiguration brickConfiguration, int indentation, boolean timeSensorUsed) {
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
        this.timeSensorUsed = timeSensorUsed;
    }

    /**
     * factory method to generate Java code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(ArduConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) //
    {
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);
        boolean timeSensorUsed = UsedTimerVisitorArdu.check(phrasesSet);

        final Ast2ArduVisitor astVisitor = new Ast2ArduVisitor(brickConfiguration, withWrapping ? 1 : 0, timeSensorUsed);
        astVisitor.generatePrefix(withWrapping);

        generateCodeFromPhrases(phrasesSet, withWrapping, astVisitor);

        return astVisitor.sb.toString();
    }

    private static void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, Ast2ArduVisitor astVisitor) {
        boolean mainBlock = false;
        for ( final ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( final Phrase<Void> phrase : phrases ) {
                mainBlock = handleMainBlocks(astVisitor, mainBlock, phrase);
                phrase.visit(astVisitor);
            }
        }
        generateSuffix(withWrapping, astVisitor);
    }

    private static boolean handleMainBlocks(Ast2ArduVisitor astVisitor, boolean mainBlock, Phrase<Void> phrase) {
        if ( phrase.getKind().getCategory() != Category.TASK ) {
            astVisitor.nlIndent();
        } else if ( phrase.getKind() != BlockType.LOCATION ) {
            mainBlock = true;
        }
        return mainBlock;
    }

    private static void generateSuffix(boolean withWrapping, Ast2ArduVisitor astVisitor) {
        if ( withWrapping ) {
            astVisitor.sb.append("\n}\n");
        }
    }

    private static String getBlocklyTypeCode(BlocklyType type) {
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
                return "int";
            case ARRAY_STRING:
                return "string";
            case ARRAY_BOOLEAN:
                return "bool";
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
        }
        throw new IllegalArgumentException("unhandled type");
    }

    private static String getEnumCode(IMode value) {
        return value.getClass().getSimpleName() + "." + value;
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     *
     * @return indentation value of the visitor.
     */
    int getIndentation() {
        return indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return sb;
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        sb.append(numConst.getValue());
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        sb.append(boolConst.isValue());
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                sb.append("PI");
                break;
            case E:
                sb.append("M_E");
                break;
            case GOLDEN_RATIO:
                sb.append("GOLDEN_RATIO");
                break;
            case SQRT2:
                sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                sb.append("M_SQRT1_2");
                break;
            // IEEE 754 floating point representation
            case INFINITY:
                sb.append("INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    //TODO: decide on color mode- so far there is only raw RGB mode, no explicit colors
    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        String value;
        switch ( getEnumCode(colorConst.getValue()) ) {
            case "PickColor.BLACK":
                value = "INPUT_BLACKCOLOR";
                break;
            case "PickColor.BLUE":
                value = "INPUT_BLUECOLOR";
                break;
            case "PickColor.GREEN":
                value = "INPUT_GREENCOLOR";
                break;
            case "PickColor.YELLOW":
                value = "INPUT_YELLOWCOLOR";
                break;
            case "PickColor.RED":
                value = "INPUT_REDCOLOR";
                break;
            case "PickColor.WHITE":
                value = "INPUT_WHITECOLOR";
                break;
            default:
                value = "NULL";
        }
        sb.append(value);
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        sb.append("NULL");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        sb.append(getBlocklyTypeCode(var.getTypeVar())).append(" ");
        sb.append(var.getName());
        if ( var.getTypeVar().isArray() ) {
            sb.append("[]");
            if ( var.getValue().getKind() == BlockType.LIST_CREATE ) {
                ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                if ( list.getValue().get().size() == 0 ) {
                    return null;
                }
            }

        }

        if ( var.getValue().getKind() != BlockType.EMPTY_EXPR ) {
            sb.append(" = ");
            if ( var.getValue().getKind() == BlockType.EXPR_LIST ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).visit(this);
                } else {
                    list.get().get(0).visit(this);
                }
            } else {
                var.getValue().visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, sb);
            sb.append(unary.getOp().getOpSymbol());
        } else {
            sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, sb);
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        if ( binary.getOp() == Op.EQ || binary.getOp() == Op.NEQ ) {
            if ( isStringExpr(binary.getLeft()) && isStringExpr(binary.getRight()) ) {
                if ( binary.getOp() == Op.NEQ ) {
                    sb.append("!");
                }
                generateSubExpr(sb, false, binary.getLeft(), binary);
                sb.append(".equals(");
                generateSubExpr(sb, false, binary.getRight(), binary);
                sb.append(")");
                return null;
            }
        }
        generateSubExpr(sb, false, binary.getLeft(), binary);
        sb.append(whitespace() + binary.getOp().getOpSymbol() + whitespace());
        if ( binary.getOp() == Op.TEXT_APPEND ) {
            sb.append("String(");
            generateSubExpr(sb, false, binary.getRight(), binary);
            sb.append(")");
        } else {
            generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        return null;
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                sb.append("\"\"");
                break;
            case "java.lang.Boolean":
                sb.append("true");
                break;
            case "java.lang.Integer":
                sb.append("0");
                break;
            case "java.util.ArrayList":
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                break;
            default:
                sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( final Expr<Void> expr : exprList.get() ) {
            if ( expr.getKind() != BlockType.EMPTY_EXPR ) {
                if ( first ) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getName().visit(this);
        sb.append(" = ");
        assignStmt.getExpr().visit(this);
        sb.append(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
        sb.append(";");
        return null;
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        generateCodeFromIfElse(ifStmt);
        generateCodeFromElse(ifStmt);
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        //boolean additionalClosingBracket = false;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        appendBreakStmt(repeatStmt);
        decrIndentation();
        nlIndent();
        sb.append("}");
        //if ( additionalClosingBracket ) {
        //    decrIndentation();
        //    nlIndent();
        //    this.sb.append("}");
        //}
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        for ( final Stmt<Void> stmt : stmtList.get() ) {
            nlIndent();
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        sb.append("delay(15);");
        decrIndentation();
        nlIndent();
        sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        sb.append("delay(");
        waitTimeStmt.getTime().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        sb.append("rob.lcdClear();");
        return null;
    }

    //TODO: implement. Perhaps- remove, since it is quite complicated for Arduino
    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                break;
            case GET:
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    //TODO: add block and test
    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        switch ( (BlinkMode) lightAction.getBlinkMode() ) {
            case ON:
                sb.append("one.led(HIGH);");
                break;
            case OFF:
                sb.append("one.led(LOW);");
                break;
        }
        return null;

    }

    //TODO: no such block
    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    //will be implemented much later
    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    //won't be used
    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    //TODO: add lcd2 to blocks
    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {

        sb.append("one.lcd");
        showTextAction.getY().visit(this);
        sb.append("(");
        showTextAction.getMsg().visit(this);
        sb.append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //9 - sound port
        sb.append("tone(9, ");
        toneAction.getFrequency().visit(this);
        sb.append(", ");
        toneAction.getDuration().visit(this);
        sb.append(");");
        return null;
    }

    //TODO: add reverse/forward to all drive and turn actions

    //TODO Not implemented. Wait for the function
    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        final boolean isRegulatedDrive = brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).isRegulated();
        String methodName;
        if ( isDuration ) {
            methodName = "one.moveMotorRotation(";
            sb.append(methodName);
            sb.append(motorOnAction.getPort());
            sb.append(", ");
            motorOnAction.getParam().getSpeed().visit(this);
            sb.append(", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
            if ( motorOnAction.getDurationMode() == MotorMoveMode.DEGREE ) {
                sb.append("/2/PI");
            }
        } else {
            //there is no regulated drive function for the robot, the closest function if PID controlled
            //movement. The coefficients are default, they seem to make movement of the robot
            //much smoother.
            methodName = isRegulatedDrive ? "one.move1mPID(" : "one.move1m(";
            sb.append(methodName);
            sb.append(motorOnAction.getPort());
            sb.append(", ");
            motorOnAction.getParam().getSpeed().visit(this);
        }
        sb.append(");");
        return null;
    }

    // not needed
    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    //can't implement it without encoder
    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    //TODO: so far this function can be implemented in a nice way only for two motors. Wait for
    // a new function
    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            sb.append("");
        } else {
            sb.append("");
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse =
            brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || brickConfiguration.getActorOnPort(brickConfiguration.getRightMotorPort()).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "rob.moveTime(";
        } else {
            methodName = "one.move(";
        }
        sb.append(methodName);
        if ( (!reverse && localReverse) || (reverse && !localReverse) ) {
            sign = "-";
        }
        sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        sb.append(", ");
        sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {

            sb.append(", ");
            //here will be duration in seconds
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        sb.append(");");
        return null;
    }

    // TURN ACTIONS
    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        final boolean isDuration = turnAction.getParam().getDuration() != null;
        final boolean reverse =
            brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || brickConfiguration.getActorOnPort(brickConfiguration.getRightMotorPort()).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean turnRight = turnAction.getDirection() == TurnDirection.RIGHT;
        final boolean turnLeft = turnAction.getDirection() == TurnDirection.LEFT;
        String methodName;
        String sign1 = "";
        String sign2 = "";
        if ( (turnRight && !reverse) || (turnLeft && reverse) ) {
            sign1 = "-";
        } else {
            sign2 = "-";
        }

        if ( isDuration ) {
            methodName = "rob.moveTime(";
        } else {
            methodName = "one.move(";
        }
        sb.append(methodName);
        sb.append(sign1);
        turnAction.getParam().getSpeed().visit(this);
        sb.append(",");
        sb.append(sign2);
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        sb.append("one.stop();");
        return null;
    }

    //no light sensor
    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        //switch ( ) {
        //case :
        //sb.append("SetSensorColorRed( IN_");
        //break;
        //case :
        //sb.append("SetSensorColorGreen( IN_");
        //break;
        //sb.append("SetSensorColorBlue ( IN_");
        //case :
        //break;
        //}
        //sb.append(lightSensor.getPort().getPortNumber());
        //sb.append(" )");
        return null;
    }

    //no light sensor
    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }

    //TODO: change block so it would just return a button value?
    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String button = null;
        switch ( getEnumCode(brickSensor.getKey()) ) {
            case "BrickKey.ENTER":
                button = "1";
                break;
            case "BrickKey.LEFT":
                button = "2";
                break;
            case "BrickKey.RIGHT":
                button = "3";
                break;
        }
        sb.append("one.readButton() == " + button);
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        switch ( getEnumCode(colorSensor.getMode()) ) {
            case "ColorSensorMode.COLOUR":
                sb.append("\"COLOR\"");
                break;
            case "ColorSensorMode.RGB":
                sb.append(" {rob.colorSensorRGB(colors, 1)[0], rob.colorSensorRGB(colors, 1)[1], rob.colorSensorRGB(colors, 1)[2]}");
                break;
            case "ColorSensorMode.RED":
                sb.append("rob.colorSensorLight(colors, 1");
                break;
            /*default:
                throw new DbcException("Invalide mode for Color Sensor!");*/
        }
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        sb.append("Sensor( IN_");
        sb.append(soundSensor.getPort().getPortNumber());
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        ActorPort encoderMotorPort = (ActorPort) encoderSensor.getMotorPort();
        switch ( (MotorTachoMode) encoderSensor.getMode() ) {
            case RESET:
                sb.append("ResetTachoCount( OUT_" + encoderMotorPort + " );");
                break;
            case ROTATION:
                sb.append("NumberOfRotations( OUT_" + encoderMotorPort + " )");
                break;
            case DEGREE:
                sb.append("MotorTachoCount( OUT_" + encoderMotorPort + " )");
                break;
            case DISTANCE:
                sb.append("MotorDistance( OUT_" + encoderMotorPort + ", WHEELDIAMETER )");
                break;
        }
        return null;
    }

    //TODO: here will be compass
    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    //TODO: add block and test
    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            //TODO: that should be called not distance, but obstacle avoidance
            case DISTANCE:
                //returns 0-3 (no obstacle, left, right, both)
                sb.append("one.obstacleSensors()");
                break;
            case SEEK:
                //returns value 0 or 1
                sb.append("one.readIRSensors()");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }
        return null;
    }

    //TODO: hide timer
    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                sb.append("T.ShowSeconds()");
                break;
            case RESET:
                sb.append("T.ResetTimer();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        sb.append("Sensor( IN_" + touchSensor.getPort().getPortNumber());
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        Integer port = Integer.parseInt(ultrasonicSensor.getPort().getPortNumber()) - 1;
        sb.append("rob.ultrasonicDistance(" + port.toString() + ")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().visit(this);
    }

    //not used
    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        functionStmt.getFunction().visit(this);
        sb.append(";");
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        /*this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        final IndexLocation where1 = IndexLocation.get(getSubFunct.getStrParam().get(0));
        this.sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        final IndexLocation where2 = IndexLocation.get(getSubFunct.getStrParam().get(1));
        this.sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");*/
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        //final BlocklyType typeArr = indexOfFunct.getParam().get(0).getVarType();
        String methodName = null;
        if ( indexOfFunct.getLocation() == IndexLocation.LAST ) {
            switch ( indexOfFunct.getParam().get(0).getVarType() ) {
                case ARRAY_NUMBER:
                    methodName = "ArrFindLastNum( ";
                    break;
                case ARRAY_STRING:
                    methodName = "ArrFindLastStr( ";
                    break;
                case ARRAY_BOOLEAN:
                    methodName = "ArrFindLastBool( ";
                    break;
            }
        } else {
            switch ( indexOfFunct.getParam().get(0).getVarType() ) {
                case ARRAY_NUMBER:
                    methodName = "ArrFindFirstNum( ";
                    break;
                case ARRAY_STRING:
                    methodName = "ArrFindFirstStr( ";
                    break;
                case ARRAY_BOOLEAN:
                    methodName = "ArrFindFirstBool( ";
                    break;
            }
        }
        sb.append(methodName);
        indexOfFunct.getParam().get(0).visit(this);
        sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        String methodName = "ArrayLen( ";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "ArrIsEmpty( ";
        }
        sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        sb.append(" )");
        //this.sb.append(methodName);
        //lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        //this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        //this.sb.append("");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        sb.append("{");
        listCreate.getValue().visit(this);
        sb.append("}");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        /*this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");*/
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).visit(this);
        sb.append("[");
        listGetIndex.getParam().get(1).visit(this);
        sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().get(0).visit(this);
        sb.append("[");
        listSetIndex.getParam().get(1).visit(this);
        sb.append("]");
        sb.append(" = ");
        listSetIndex.getParam().get(2).visit(this);
        sb.append(";");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        sb.append("Constrain( ");
        mathConstrainFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                sb.append("( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % 2 == 0 )");
                break;
            case ODD:
                sb.append("( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % 2 == 1 )");
                break;
            case PRIME:
                sb.append("MathPrime( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" )");
                break;
            // % in nxc doesn't leave a a fractional residual, e.g. 5.2%1 = 0, so it is not possible to cheack the wholeness by "%1", that is why
            //an additional function is used
            case WHOLE:
                sb.append("MathIsWhole( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" )");
                break;
            case POSITIVE:
                sb.append("( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" > 0 )");
                break;
            case NEGATIVE:
                sb.append("( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" < 0 )");
                break;
            //it would work only for whole numbers, however, I think that it makes sense to talk about being divisible only for the whole numbers
            case DIVISIBLE_BY:
                sb.append("( ");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(" % ");
                mathNumPropFunct.getParam().get(1).visit(this);
                sb.append(" == 0 )");
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
                sb.append("ArrSum( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                sb.append("ArrMin( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                sb.append("ArrMax( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                sb.append("ArrMean( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                sb.append("ArrMedian( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                sb.append("ArrStandardDeviatioin( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                sb.append("ArrRand(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                sb.append("ArrMode( ");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        sb.append("RandomFloat()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        sb.append("RandomIntegerInRange( ");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                sb.append("sqrt( ");
                break;
            case ABS:
                sb.append("abs( ");
                break;
            //Taylor Series converge only when value is less than one. Larger values are calculated
            //using a table.
            case LN:
                sb.append("MathLn( ");
                break;
            case LOG10:
                sb.append("MathLog( ");
                break;
            case EXP:
                sb.append("MathPow( E, ");
                break;
            case POW10:
                sb.append("MathPow( 10, ");
                break;
            //the 3 functions below accept degrees
            case SIN:
                sb.append("MathSin( ");
                break;
            case COS:
                sb.append("MathCos( ");
                break;
            case TAN:
                sb.append("MathTan( ");
                break;
            case ASIN:
                sb.append("MathAsin( ");
                break;
            //Taylor Series converge only when value is less than one. Larger values are calculated
            //using a table.
            case ATAN:
                sb.append("MathAtan( ");
                break;
            case ACOS:
                sb.append("MathAcos( ");
                break;
            case ROUND:
                sb.append("MathRound( ");
                break;
            case ROUNDUP:
                sb.append("MathRoundUp( ");
                break;
            //check why there are double brackets
            case ROUNDDOWN:
                sb.append("MathFloor( ");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        sb.append(" )");

        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        sb.append("MathPow( ");
        mathPowerFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        sb.append(" )");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        //smthToString(textJoinFunct.getParam());
        //this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        sb.append("\n").append(INDENT).append("void ");
        sb.append(methodVoid.getMethodName() + "(");
        methodVoid.getParameters().visit(this);
        sb.append(") {");
        methodVoid.getBody().visit(this);
        sb.append("\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        sb.append("\n").append(INDENT).append(getBlocklyTypeCode(methodReturn.getReturnType()));
        sb.append(" " + methodReturn.getMethodName() + "( ");
        methodReturn.getParameters().visit(this);
        sb.append(" ) {");
        methodReturn.getBody().visit(this);
        this.nlIndent();
        sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        sb.append(";\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        sb.append("if ( ");
        methodIfReturn.getCondition().visit(this);
        sb.append(" ) ");
        sb.append("return ");
        methodIfReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        sb.append(";");
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        sb.append(methodCall.getMethodName() + "( ");
        methodCall.getParametersValues().visit(this);
        sb.append(" )");
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            sb.append(";");
        }
        return null;
    }

    // TODO: fix calling
    // the function is in hal.h
    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        sb.append("BluetoothGetNumber( ");
        //TODO: add these block options:
        //this.sb.append("BluetoothGetString( ");
        //this.sb.append("BluetoothGetBoolean( ");
        // the function accepts inbox address (int)
        bluetoothReadAction.getConnection().visit(this);
        sb.append(" )");
        return null;
    }

    // not needed for Ardu. Use a block that calls BTCheck(int conn) function instead
    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        sb.append("BTCheck(");

        /*this.sb.append("hal.establishConnectionTo(");
        if ( bluetoothConnectAction.get_address().getKind() != BlockType.STRING_CONST ) {
            this.sb.append("String.valueOf(");
            bluetoothConnectAction.get_address().visit(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.get_address().visit(this);
        }*/
        sb.append(")");
        return null;
    }

    // the function is built-in
    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        sb.append("SendRemoteNumber(");
        //TODO: add these block options: output variable (string, boolean or number. Need to create an enumeration), connection (int, 1-3 for master, always
        // 0 for slave), outbox address (int)
        //this.sb.append("SendRemoteString(");
        //this.sb.append("SendRemoteBool(");
        // the function accepts the following: inbox address

        //if ( bluetoothSendAction.getMsg().getKind() != BlockType.STRING_CONST ) {
        //    String.valueOf(bluetoothSendAction.getMsg().visit(this));
        //} else {
        //    bluetoothSendAction.getMsg().visit(this);
        //}
        //this.sb.append(", ");
        //bluetoothSendAction.getConnection().visit(this);
        sb.append(");");
        return null;
    }

    //TODO: add SysCommBTOn
    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    private void incrIndentation() {
        indentation += 1;
    }

    private void decrIndentation() {
        indentation -= 1;
    }

    private void indent() {
        if ( indentation <= 0 ) {
            return;
        } else {
            for ( int i = 0; i < indentation; i++ ) {
                sb.append(INDENT);
            }
        }
    }

    private void nlIndent() {
        sb.append("\n");
        indent();
    }

    private String whitespace() {
        return " ";
    }

    private boolean isStringExpr(Expr<Void> e) {
        switch ( e.getKind() ) {
            case STRING_CONST:
                return true;
            case VAR:
                return ((Var<?>) e).getTypeVar() == BlocklyType.STRING;
            case FUNCTION_EXPR:
                final BlockType functionKind = ((FunctionExpr<?>) e).getFunction().getKind();
                return functionKind == BlockType.TEXT_JOIN_FUNCT || functionKind == BlockType.LIST_INDEX_OF;
            case METHOD_EXPR:
                final MethodCall<?> methodCall = (MethodCall<?>) ((MethodExpr<?>) e).getMethod();
                return methodCall.getKind() == BlockType.METHOD_CALL && methodCall.getReturnType() == BlocklyType.STRING;
            case ACTION_EXPR:
                final Action<?> action = ((ActionExpr<?>) e).getAction();
                return action.getKind() == BlockType.BLUETOOTH_RECEIVED_ACTION;

            default:
                return false;
        }
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == BlockType.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && expr.getKind() != BlockType.BINARY ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("(" + whitespace());
            expr.visit(this);
            sb.append(whitespace() + ")");
        }
    }

    private void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("( ");
            unary.getExpr().visit(this);
            sb.append(" )");
        } else {
            unary.getExpr().visit(this);
        }
    }

    private void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                generateCodeFromStmtCondition("else if", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            ifStmt.getThenList().get(i).visit(this);
            decrIndentation();
            if ( i + 1 < ifStmt.getExpr().size() ) {
                nlIndent();
                sb.append("}").append(whitespace());
            }
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
        nlIndent();
        sb.append("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.visit(this);
        sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        sb.append(stmtType + whitespace() + "(" + whitespace() + "float" + whitespace());
        final ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        sb.append(whitespace() + "=" + whitespace());
        expressions.get().get(1).visit(this);
        sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        sb.append(whitespace());
        sb.append("<" + whitespace());
        expressions.get().get(2).visit(this);
        sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        sb.append(whitespace());
        sb.append("+=" + whitespace());
        expressions.get().get(3).visit(this);
        sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            nlIndent();
            sb.append("break;");
        }
    }

    //WHEELDIAMETER = 6.5
    //TRACKWIDTH = 16.5
    private void addConstants() {
        sb.append("#define WHEELDIAMETER " + brickConfiguration.getWheelDiameterCM() + "\n");
        sb.append("#define TRACKWIDTH " + brickConfiguration.getTrackWidthCM() + "\n");
        sb.append("#include <math.h> \n");
        sb.append("#include <CountUpDownTimer.h> \n");
        // Bot'n Roll ONE A library:
        sb.append("#include <BnrOneA.h> \n");
        //Bot'n Roll CoSpace Rescue Module library (for the additional sonar kit):
        sb.append("#include <BnrRescue.h> \n");
        //additional Roberta functions:
        sb.append("#include <RobertaFunctions.h> \n");
        // SPI communication library required by BnrOne.cpp"
        sb.append("#include <SPI.h> \n");
        // required by BnrRescue.cpp (for the additional sonar kit):
        sb.append("#include <Wire.h> \n");
        // declaration of object variable to control the Bot'n Roll ONE A and Rescue:
        sb.append("BnrOneA one; \n");
        sb.append("BnrRescue brm; \n");
        sb.append("RobertaFunctions rob; \n");
        if ( timeSensorUsed ) {
            sb.append("CountUpDownTimer T(UP, HIGH); \n");
        }
        sb.append("#define SSPIN  2 \n");
        sb.append("#define MODULE_ADDRESS 0x2C \n");
        sb.append("byte colors[3]={0,0,0}; \n");
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.addConstants();

        sb.append("void setup() \n");
        sb.append("{");
        nlIndent();
        //set baud rate to 57600bps for printing values at serial monitor:
        sb.append("Serial.begin(9600);");
        nlIndent();
        // start the communication module:
        sb.append("one.spiConnect(SSPIN);");
        nlIndent();
        sb.append("brm.i2cConnect(MODULE_ADDRESS);");
        nlIndent();
        sb.append("brm.setModuleAddress(0x2C);");
        nlIndent();
        // stop motors:
        sb.append("one.stop();");
        if ( timeSensorUsed ) {
            nlIndent();
            sb.append("T.StartTimer();");
        }
        for ( final Entry<ISensorPort, Sensor> entry : brickConfiguration.getSensors().entrySet() ) {
            switch ( entry.getValue().getType() ) {
                //TODO: add infrared (basic and line following), compas (that also works like gyro),
                // additional head sonar
                case COLOR:
                    nlIndent();
                    sb.append("brm.setRgbStatus(ENABLE);");
                    break;
                case ULTRASONIC:
                    nlIndent();
                    sb.append("brm.setSonarStatus(ENABLE);");
                    break;
                //TODO: add a function for sonar
                case ULTRASONIC_HEAD:
                    nlIndent();
                    sb.append("one.spiConnect(SSPIN);");
                    //sonar setup:
                    sb.append("pinMode(trigPin, OUTPUT);");
                    sb.append("pinMode(echoPin, INPUT);");
                    sb.append("pinMode(LEDPin,  OUTPUT);");
                    break;
                case INFRARED:
                    nlIndent();
                    sb.append("one.obstacleEmitters(ON);");
                    break;
                //case INFRARED_LINE:
                //break;
                case GYRO:
                    /*nlIndent();
                    sb.append("Wire.begin();");
                    nlIndent();
                    sb.append("one.spiConnect(SSPIN);");
                    break;*/
                default:
                    break;
            }
        }
        sb.append("\n");
        sb.append("} \n");
        sb.append("void loop() \n");
        sb.append("{");
        if ( timeSensorUsed ) {
            nlIndent();
            sb.append("T.Timer();");
        }
    }
}