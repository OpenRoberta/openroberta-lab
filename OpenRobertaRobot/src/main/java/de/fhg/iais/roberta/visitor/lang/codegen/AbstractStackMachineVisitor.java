package de.fhg.iais.roberta.visitor.lang.codegen;

import com.google.common.collect.Lists;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.*;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.*;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractStackMachineVisitor<V> implements ILanguageVisitor<V> {
    private static final Predicate<String> IS_INVALID_BLOCK_ID = s -> s.equals("1");

    public static final int JUMP_END_MARKER = -2;
    public static final int JUMP_THEN_MARKER = -1;
    public static final int BREAK_MARKER = -1;
    public static final int CONTINUE_MARKER = -2;
    private static final int METHOD_END = -3;

    private List<JSONObject> opArray = new ArrayList<>();
    protected final ConfigurationAst configuration;
    private final List<JSONObject> flowControlStatements = new ArrayList<>();
    private final Map<String, List<JSONObject>> methodCalls = new HashMap<>();
    private final Map<String, Integer> methodDeclarations = new HashMap<>();

    private final List<String> initiateList = new ArrayList<>();
    private final List<String> terminateList = new ArrayList<>();
    private final List<String> notYetTerminatedList = new ArrayList<>();

    protected boolean debugger = true;

    protected AbstractStackMachineVisitor(ConfigurationAst configuration) {
        this.configuration = configuration;
    }

    @Override
    public final V visitNumConst(NumConst<V> numConst) {
        JSONObject o = makeLeaf(C.EXPR, numConst).put(C.EXPR, numConst.getKind().getName()).put(C.VALUE, numConst.getValue());
        return app(o);
    }

    @Override
    public final V visitMathConst(MathConst<V> mathConst) {
        JSONObject o = makeLeaf(C.EXPR, mathConst).put(C.EXPR, C.MATH_CONST).put(C.VALUE, mathConst.getMathConst());
        return app(o);
    }

    @Override
    public final V visitBoolConst(BoolConst<V> boolConst) {
        JSONObject o = makeLeaf(C.EXPR, boolConst).put(C.EXPR, boolConst.getKind().getName()).put(C.VALUE, boolConst.getValue());
        return app(o);
    }

    @Override
    public final V visitStringConst(StringConst<V> stringConst) {
        JSONObject o = makeLeaf(C.EXPR, stringConst).put(C.EXPR, stringConst.getKind().getName());
        o.put(C.VALUE, stringConst.getValue().replaceAll("[<>\\$]", ""));
        return app(o);
    }

    @Override
    public final V visitNullConst(NullConst<V> nullConst) {
        JSONObject o = makeLeaf(C.EXPR, nullConst).put(C.EXPR, "C." + nullConst.getKind().getName());
        return app(o);
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        int colorId = 0;
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#FF1493":
                colorId = 1;
                break;
            case "#800080":
                colorId = 2;
                break;
            case "#4876FF":
                colorId = 3;
                break;
            case "#00FFFF":
                colorId = 4;
                break;
            case "#90EE90":
                colorId = 5;
                break;
            case "#008000":
                colorId = 6;
                break;
            case "#FFFF00":
                colorId = 7;
                break;
            case "#FFA500":
                colorId = 8;
                break;
            case "#FF0000":
                colorId = 9;
                break;
            case "#FFFFFE":
            case "#FFFFFF":
                colorId = 10;
                break;
            default:
                colorConst.addInfo(NepoInfo.error("SIM_BLOCK_NOT_SUPPORTED"));
                throw new DbcException("Invalid color constant: " + colorConst.getHexIntAsString());
        }
        JSONObject o = makeLeaf(C.EXPR, colorConst).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, colorId);
        return app(o);
    }

    @Override
    public final V visitRgbColor(RgbColor<V> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        JSONObject o = makeLeaf(C.EXPR, rgbColor).put(C.EXPR, C.RGB_COLOR_CONST);
        return app(o);
    }

    @Override
    public final V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().accept(this);
        } else {
            shadowExpr.getShadow().accept(this);
        }
        return null;
    }

    @Override
    public final V visitVar(Var<V> var) {
        JSONObject o = makeLeaf(C.EXPR, var).put(C.EXPR, C.VAR).put(C.NAME, var.getValue());
        return app(o);
    }

    @Override
    public final V visitVarDeclaration(VarDeclaration<V> var) {
        if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
            ExprList<V> list = (ExprList<V>) var.getValue();
            if ( list.get().size() == 2 ) {
                list.get().get(1).accept(this);
            } else {
                list.get().get(0).accept(this);
            }
        } else {
            var.getValue().accept(this);
        }
        JSONObject o = makeLeaf(C.VAR_DECLARATION, var).put(C.TYPE, var.getTypeVar()).put(C.NAME, var.getName());
        return app(o);
    }

    @Override
    public final V visitUnary(Unary<V> unary) {
        unary.getExpr().accept(this);
        JSONObject o = makeLeaf(C.EXPR, unary).put(C.EXPR, C.UNARY).put(C.OP, unary.getOp());
        return app(o);
    }

    @Override
    public final V visitBinary(Binary<V> binary) {
        switch ( binary.getOp() ) {
            case AND:
            case OR:
                /*
                Jumps are needed because of lazy evaluation

                AND                       OR
                left                      left
                 ▼                         ▼
                JUMP false──┐             JUMP true ──┐
                 ▼          │              ▼          │
                right       │             right       │
                 ▼          │              ▼          │
                JUMP Always─┼─┐           JUMP Always─┼─┐
                 ▼          │ │            ▼          │ │
                false◄──────┘ │           true◄───────┘ │
                 ▼            │            ▼            │
                  ◄───────────┘             ◄───────────┘
                 */

                boolean isOr = binary.getOp() == Op.OR;
                binary.getLeft().accept(this);
                JSONObject skipNextCondition = makeLeaf(C.JUMP, binary).put(C.CONDITIONAL, isOr);
                app(skipNextCondition);

                binary.getRight().accept(this);
                JSONObject jumpToEnd = makeLeaf(C.JUMP, binary).put(C.CONDITIONAL, C.ALWAYS);
                app(jumpToEnd);

                skipNextCondition.put(C.TARGET, opArray.size());
                app(makeLeaf(C.EXPR, binary).put(C.EXPR, C.BOOL_CONST).put(C.VALUE, isOr));
                jumpToEnd.put(C.TARGET, opArray.size());
                return null;
            default:
                binary.getLeft().accept(this);
                binary.getRight().accept(this);
                JSONObject o;
                // FIXME: The math change should be removed from the binary expression since it is a statement
                switch ( binary.getOp() ) {
                    case MATH_CHANGE:
                        o = makeLeaf(C.MATH_CHANGE, binary).put(C.NAME, ((Var<V>) binary.getLeft()).getValue());
                        break;
                    case TEXT_APPEND:
                        o = makeLeaf(C.TEXT_APPEND, binary).put(C.NAME, ((Var<V>) binary.getLeft()).getValue());
                        break;

                    default:
                        o = makeLeaf(C.EXPR, binary).put(C.EXPR, C.BINARY).put(C.OP, binary.getOp());
                        break;
                }
                return app(o);

        }

    }

    @Override
    public final V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct) {
        mathPowerFunct.getParam().get(0).accept(this);
        mathPowerFunct.getParam().get(1).accept(this);
        JSONObject o = makeLeaf(C.EXPR, mathPowerFunct).put(C.EXPR, C.BINARY).put(C.OP, mathPowerFunct.getFunctName());
        return app(o);
    }

    @Override
    public final V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().accept(this);
        return null;
    }

    @Override
    public final V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().accept(this);
        return null;
    }

    @Override
    public final V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().accept(this);
        return null;
    }

    @Override
    public final V visitEmptyList(EmptyList<V> emptyList) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public final V visitEmptyExpr(EmptyExpr<V> emptyExpr) {
        JSONObject o;
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.STRING_CONST).put(C.VALUE, "");
                break;
            case BOOLEAN:
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.BOOL_CONST).put(C.VALUE, "true");
                break;
            case NUMBER_INT:
            case NUMBER:
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0);
                break;
            case COLOR:
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.LED_COLOR_CONST).put(C.VALUE, 3);
                break;
            case NULL:
            case CONNECTION:
            case ARRAY_BOOLEAN:
            case ARRAY_COLOUR:
            case ARRAY_CONNECTION:
            case ARRAY_IMAGE:
            case ARRAY_NUMBER:
            case ARRAY_STRING:
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.NULL_CONST);
                break;
            case IMAGE:
                JSONArray jsonImage = new JSONArray();
                for ( int i = 0; i < 5; i++ ) {
                    ArrayList<Integer> a = new ArrayList<>();
                    for ( int j = 0; j < 5; j++ ) {
                        a.add(0);
                    }
                    jsonImage.put(new JSONArray(a));
                }
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.IMAGE_CONST).put(C.VALUE, jsonImage);
                break;
            case CAPTURED_TYPE: // TODO: get the captured type
                o = makeLeaf(C.EXPR, emptyExpr).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0);
                break;
            default:
                throw new DbcException("Operation not supported");
        }
        return app(o);
    }

    @Override
    public final V visitExprList(ExprList<V> exprList) {
        for ( Expr<V> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                expr.accept(this);
            }
        }
        return null;
    }

    @Override
    public final V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().accept(this);
        return null;
    }

    @Override
    public final V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().accept(this);
        return null;
    }

    @Override
    public final V visitAssignStmt(AssignStmt<V> assignStmt) {
        assignStmt.getExpr().accept(this);
        JSONObject o = makeLeaf(C.ASSIGN_STMT, assignStmt).put(C.NAME, assignStmt.getName().getValue());
        return app(o);
    }

    @Override
    public final V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().accept(this);
        return null;
    }

    @Override
    public final V visitIfStmt(IfStmt<V> ifStmt) {
        int numberOfThens = ifStmt.getExpr().size();
        if ( ifStmt.isTernary() ) {
            Assert.isTrue(numberOfThens == 1);
            Assert.isFalse(ifStmt.getElseList().get().isEmpty());
        }
        
        beginPhrase(ifStmt);
        app(makeNode(C.COMMENT).put(C.TARGET, C.IF_STMT));

        List<JSONObject> jumpsToEnd = new ArrayList<>();
        // TODO: better a list of pairs. pair of lists needs this kind of for
        for ( int i = 0; i < numberOfThens; i++ ) {
            ifStmt.getExpr().get(i).accept(this);
            // JUMP when condition not fullfilled
            JSONObject jumpOverThen = makeNode(C.JUMP).put(C.CONDITIONAL, false).put(C.TARGET, JUMP_THEN_MARKER);
            app(jumpOverThen);
            ifStmt.getThenList().get(i).accept(this);

            // JUMP when if was fullfilled
            JSONObject jumpToEnd = makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, JUMP_END_MARKER);
            app(jumpToEnd);
            jumpsToEnd.add(jumpToEnd);

            jumpOverThen.put(C.TARGET, opArray.size());
        }
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            ifStmt.getElseList().accept(this);
        }

        jumpsToEnd
            .forEach(jump -> jump.put(C.TARGET, opArray.size()));

        endPhrase(ifStmt);
        return null;
    }

    @Override
    public final V visitNNStepStmt(NNStepStmt<V> nnStepStmt) {
        for ( Expr<V> e : nnStepStmt.getIl() ) {
            e.accept(this);
        }
        JSONObject o = makeLeaf(C.NNSTEP_STMT, nnStepStmt);
        app(o);
        for ( Var<V> v : nnStepStmt.getOl() ) {
            JSONObject ov = makeLeaf(C.ASSIGN_STMT, nnStepStmt).put(C.NAME, v.getValue());
            app(ov);
        }
        return null;
    }

    @Override
    public final V visitRepeatStmt(RepeatStmt<V> repeatStmt) {
        Mode mode = repeatStmt.getMode();

        switch ( mode ) {
            case WAIT:
                // the very special case of a wait stmt. The AST is not perfectly designed for this case
                repeatStmt.getExpr().accept(this);
                JSONObject skipThenPart = makeNode(C.JUMP).put(C.CONDITIONAL, false).put(C.TARGET, JUMP_THEN_MARKER);
                app(skipThenPart);
                repeatStmt.getList().accept(this);
                JSONObject breakStatement = makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, BREAK_MARKER);
                app(breakStatement);
                flowControlStatements.add(breakStatement);
                skipThenPart.put(C.TARGET, opArray.size());
                return null;
            case FOR:
            case TIMES:
                encloseFlowStatementScope(() -> {
                    if ( !(repeatStmt.getExpr() instanceof ExprList<?>) ) {
                        throw new DbcException(String.format("Expected %s to be an ExprList", repeatStmt.getExpr()));
                    }

                    List<Expr<V>> exprList = ((ExprList<V>) repeatStmt.getExpr()).get();
                    if ( !(exprList.get(0) instanceof Var<?>) ) {
                        throw new DbcException(String.format("Expected %s to be an variable", exprList.get(0)));
                    }
                    Var<V> variable = (Var<V>) exprList.get(0);
                    String variableName = variable.getValue();
                    Expr<V> initialValue = exprList.get(1);
                    Expr<V> terminationValue = exprList.get(2);
                    Expr<V> incrementValue = exprList.get(3);

                    // Initialize Var
                    initialValue.accept(this);
                    app(makeNode(C.VAR_DECLARATION).put(C.TYPE, initialValue.getVarType()).put(C.NAME, variableName));

                    int programCounterAfterInitialization = opArray.size();

                    beginPhrase(repeatStmt);
                    app(makeNode(C.COMMENT).put(C.TARGET, C.REPEAT_STMT));

                    // Termination Expr
                    variable.accept(this);
                    terminationValue.accept(this);
                    app(makeNode(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, Op.LT));
                    JSONObject jump = makeNode(C.JUMP).put(C.CONDITIONAL, false);
                    app(jump);

                    repeatStmt.getList().accept(this);

                    int programCounterAfterStatementList = opArray.size();

                    // Increment
                    incrementValue.accept(this);
                    variable.accept(this);
                    app(makeNode(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, Op.ADD));
                    app(makeNode(C.ASSIGN_STMT).put(C.NAME, variableName));

                    app(makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, programCounterAfterInitialization));
                    int programCounterAfterForLoop = opArray.size();
                    jump.put(C.TARGET, programCounterAfterForLoop);

                    app(makeNode(C.UNBIND_VAR).put(C.NAME, variableName));

                    flowControlStatements.forEach(statement -> {
                        if ( statement.get(C.TARGET).equals(CONTINUE_MARKER) ) {
                            statement.put(C.TARGET, programCounterAfterStatementList);
                        } else if ( statement.get(C.TARGET).equals(BREAK_MARKER) ) {
                            statement.put(C.TARGET, programCounterAfterForLoop);
                        } else {
                            throw new DbcException("Invalid flow control expression");
                        }
                    });
                    endPhrase(repeatStmt);
                });
                return null;
            case FOR_EACH:
                encloseFlowStatementScope(() -> {
                    if ( !(repeatStmt.getExpr() instanceof Binary<?>) ) {
                        throw new DbcException(String.format("Expected %s to be an Binary", repeatStmt.getExpr()));
                    }

                    Binary<V> binary = (Binary<V>) repeatStmt.getExpr();
                    if ( !(binary.getLeft() instanceof VarDeclaration<?>) ) {
                        throw new DbcException(String.format("Expected %s to be a VarDeclaration", repeatStmt.getExpr()));
                    }
                    if ( !(binary.getRight() instanceof Var<?>) ) {
                        throw new DbcException(String.format("Expected %s to be a VarDeclaration", repeatStmt.getExpr()));
                    }

                    VarDeclaration<V> varDeclaration = (VarDeclaration<V>) binary.getLeft();
                    String variableName = varDeclaration.getName();
                    String runVariableName = variableName + "_runningVariable";
                    Var<V> listVariable = (Var<V>) binary.getRight();

                    // Init run variable (int i = 0)
                    app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
                    app(makeNode(C.VAR_DECLARATION).put(C.TYPE, BlocklyType.NUMBER).put(C.NAME, runVariableName));

                    // Init variable (Element element)
                    varDeclaration.accept(this);
                    int programCounterAfterInitialization = opArray.size();

                    beginPhrase(repeatStmt);
                    app(makeNode(C.COMMENT).put(C.TARGET, C.REPEAT_STMT));

                    // Termination expr ( i < list.length )
                    app(makeNode(C.EXPR).put(C.EXPR, C.VAR).put(C.NAME, runVariableName));
                    listVariable.accept(this);
                    app(makeNode(C.EXPR).put(C.EXPR, C.LIST_OPERATION).put(C.OP, FunctionNames.LIST_LENGTH.toString().toLowerCase()));
                    app(makeNode(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, Op.LT));

                    JSONObject jump = makeNode(C.JUMP).put(C.CONDITIONAL, false);
                    app(jump);

                    // Assign variable ( element = list.get(i) )
                    listVariable.accept(this);
                    app(makeNode(C.EXPR).put(C.EXPR, C.VAR).put(C.NAME, runVariableName));
                    app(makeNode(C.EXPR)
                        .put(C.EXPR, C.LIST_OPERATION)
                        .put(C.OP, ListElementOperations.GET.toString().toLowerCase())
                        .put(C.POSITION, IndexLocation.FROM_START.toString().toLowerCase()));
                    app(makeNode(C.ASSIGN_STMT).put(C.NAME, variableName));

                    repeatStmt.getList().accept(this);

                    int programCounterAfterStatementList = opArray.size();

                    // Increment (i = i + 1)
                    app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 1));
                    app(makeNode(C.EXPR).put(C.EXPR, C.VAR).put(C.NAME, runVariableName));
                    app(makeNode(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, Op.ADD));
                    app(makeNode(C.ASSIGN_STMT).put(C.NAME, runVariableName));

                    app(makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, programCounterAfterInitialization));
                    int programCounterAfterForLoop = opArray.size();
                    jump.put(C.TARGET, programCounterAfterForLoop);

                    app(makeNode(C.UNBIND_VAR).put(C.NAME, variableName));
                    app(makeNode(C.UNBIND_VAR).put(C.NAME, runVariableName));

                    flowControlStatements.forEach(statement -> {
                        if ( statement.get(C.TARGET).equals(CONTINUE_MARKER) ) {
                            statement.put(C.TARGET, programCounterAfterStatementList);
                        } else if ( statement.get(C.TARGET).equals(BREAK_MARKER) ) {
                            statement.put(C.TARGET, programCounterAfterForLoop);
                        } else {
                            throw new DbcException("Invalid flow control expression");
                        }
                    });
                    endPhrase(repeatStmt);
                });
                return null;
            case FOREVER:
            case FOREVER_ARDU:
                encloseFlowStatementScope(() -> {
                    int beforeExprTarget = opArray.size();

                    beginPhrase(repeatStmt);
                    app(makeNode(C.COMMENT).put(C.TARGET, C.REPEAT_STMT));

                    repeatStmt.getList().accept(this);

                    app(makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, beforeExprTarget));
                    flowControlStatements.forEach(flowControlStatement -> {
                        if ( flowControlStatement.get(C.TARGET).equals(BREAK_MARKER) ) {
                            flowControlStatement.put(C.TARGET, opArray.size());
                        } else if ( flowControlStatement.get(C.TARGET).equals(CONTINUE_MARKER) ) {
                            flowControlStatement.put(C.TARGET, beforeExprTarget);
                        } else {
                            throw new DbcException("Invalid flow control expression");
                        }
                    });
                    endPhrase(repeatStmt);
                });
                return null;
            case WHILE:
            case UNTIL:
                encloseFlowStatementScope(() -> {
                    int beforeExprTarget = opArray.size();

                    beginPhrase(repeatStmt);
                    app(makeNode(C.COMMENT).put(C.TARGET, C.REPEAT_STMT));

                    repeatStmt.getExpr().accept(this);
                    // no difference between WHILE and UNTIL because a NOT gets injected into UNTIL by jaxbToAST
                    JSONObject jumpOverWhile = makeNode(C.JUMP).put(C.CONDITIONAL, false).put(C.TARGET, BREAK_MARKER);
                    flowControlStatements.add(jumpOverWhile);
                    app(jumpOverWhile);
                    repeatStmt.getList().accept(this);
                    app(makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, beforeExprTarget));
                    flowControlStatements.forEach(flowControlStatement -> {
                        if ( flowControlStatement.get(C.TARGET).equals(BREAK_MARKER) ) {
                            flowControlStatement.put(C.TARGET, opArray.size());
                        } else if ( flowControlStatement.get(C.TARGET).equals(CONTINUE_MARKER) ) {
                            flowControlStatement.put(C.TARGET, beforeExprTarget);
                        } else {
                            throw new DbcException("Invalid flow control expression");
                        }
                    });
                    endPhrase(repeatStmt);
                });
                return null;
            default:
                throw new DbcException("Invalid repeat mode: " + mode);
        }

    }

    @Override
    public final V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().accept(this);
        return null;
    }

    @Override
    public final V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon) {
        JSONObject o = makeLeaf(C.JUMP, stmtFlowCon).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, stmtFlowCon.getFlow() == Flow.BREAK ? BREAK_MARKER : CONTINUE_MARKER);
        flowControlStatements.add(o);
        return app(o);
    }

    @Override
    public final V visitStmtList(StmtList<V> stmtList) {
        for ( Stmt<V> stmt : stmtList.get() ) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public final V visitMainTask(MainTask<V> mainTask) {
        mainTask.getVariables().accept(this);
        if ( mainTask.getDebug().equals("TRUE") ) {
            JSONObject o = makeLeaf(C.CREATE_DEBUG_ACTION, mainTask);
            return app(o);
        }
        return null;
    }

    @Override
    public final V visitActivityTask(ActivityTask<V> activityTask) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public final V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public final V visitWaitStmt(WaitStmt<V> waitStmt) {
        encloseFlowStatementScope(() -> {
            int programCounterStart = opArray.size();

            beginPhrase(waitStmt);
            app(makeNode(C.COMMENT).put(C.TARGET, C.WAIT_STMT));

            waitStmt.getStatements().get()
                .forEach(statement -> statement.accept(this));
            this.getOpArray().add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 1));
            this.getOpArray().add(makeNode(C.WAIT_TIME_STMT));
            app(makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, programCounterStart));
            flowControlStatements.forEach(statement -> {
                if ( statement.get(C.TARGET).equals(BREAK_MARKER) ) {
                    statement.put(C.TARGET, opArray.size());
                } else {
                    throw new DbcException("Invalid flow control expression");
                }
            });
            endPhrase(waitStmt);
        });
        return null;
    }

    @Override
    public final V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt) {
        waitTimeStmt.getTime().accept(this);
        JSONObject o = makeLeaf(C.WAIT_TIME_STMT, waitTimeStmt);
        return app(o);
    }

    @Override
    public final V visitLocation(Location<V> location) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public final V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct) {
        return null;
    }

    @Override
    public final V visitStmtTextComment(StmtTextComment<V> textComment) {
        JSONObject o;
        o = makeLeaf(C.COMMENT, textComment).put(C.VALUE, textComment.getTextComment());
        return app(o);
    }

    @Override
    public final V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().accept(this);
        return null;
    }

    @Override
    public final V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().accept(this);
        return null;
    }

    @Override
    public final V visitGetSubFunct(GetSubFunct<V> getSubFunct) {
        getSubFunct.getParam().forEach(x -> x.accept(this));

        JSONObject o =
            makeLeaf(C.EXPR, getSubFunct)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, C.LIST_GET_SUBLIST)
                .put(C.POSITION, getSubFunct.getStrParam().stream().map(x -> x.toString().toLowerCase()).toArray());

        return app(o);
    }

    @Override
    public final V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct) {
        indexOfFunct.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            makeLeaf(C.EXPR, indexOfFunct)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, C.LIST_FIND_ITEM)
                .put(C.POSITION, indexOfFunct.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().get(0).accept(this);
        JSONObject o = makeLeaf(C.EXPR, lengthOfIsEmptyFunct).put(C.EXPR, C.LIST_OPERATION).put(C.OP, lengthOfIsEmptyFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListCreate(ListCreate<V> listCreate) {
        listCreate.getValue().accept(this);
        int n = listCreate.getValue().get().size();

        JSONObject o = makeLeaf(C.EXPR, listCreate).put(C.EXPR, C.CREATE_LIST).put(C.NUMBER, n);
        return app(o);
    }

    @Override
    public final V visitListSetIndex(ListSetIndex<V> listSetIndex) {
        listSetIndex.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            makeLeaf(C.LIST_OPERATION, listSetIndex)
                .put(C.OP, listSetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listSetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListGetIndex(ListGetIndex<V> listGetIndex) {
        listGetIndex.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            makeLeaf(C.EXPR, listGetIndex)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, listGetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listGetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListRepeat(ListRepeat<V> listRepeat) {
        listRepeat.getParam().forEach(x -> x.accept(this));
        JSONObject o = makeLeaf(C.EXPR, listRepeat).put(C.EXPR, C.CREATE_LIST_REPEAT);
        return app(o);
    }

    @Override
    public final V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct) {
        mathConstrainFunct.getParam().get(0).accept(this);
        mathConstrainFunct.getParam().get(1).accept(this);
        mathConstrainFunct.getParam().get(2).accept(this);
        JSONObject o = makeLeaf(C.EXPR, mathConstrainFunct).put(C.EXPR, C.MATH_CONSTRAIN_FUNCTION);
        return app(o);
    }

    @Override
    public final V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct) {
        mathNumPropFunct.getParam().get(0).accept(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            mathNumPropFunct.getParam().get(1).accept(this);
        }
        JSONObject o = makeLeaf(C.EXPR, mathNumPropFunct).put(C.EXPR, C.MATH_PROP_FUNCT).put(C.OP, mathNumPropFunct.getFunctName());
        return app(o);
    }

    @Override
    public final V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct) {
        mathOnListFunct.getParam().forEach(x -> x.accept(this));
        JSONObject o = makeLeaf(C.EXPR, mathOnListFunct).put(C.EXPR, C.MATH_ON_LIST).put(C.OP, mathOnListFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct) {
        JSONObject o = makeLeaf(C.EXPR, mathRandomFloatFunct).put(C.EXPR, C.RANDOM_DOUBLE);
        return app(o);
    }

    @Override
    public final V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().get(0).accept(this);
        mathRandomIntFunct.getParam().get(1).accept(this);
        JSONObject o = makeLeaf(C.EXPR, mathRandomIntFunct).put(C.EXPR, C.RANDOM_INT);
        return app(o);
    }

    @Override
    public final V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct) {
        mathSingleFunct.getParam().get(0).accept(this);
        JSONObject o = makeLeaf(C.EXPR, mathSingleFunct).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, mathSingleFunct.getFunctName());
        return app(o);
    }

    @Override
    public V visitMathCastStringFunct(MathCastStringFunct<V> mathCastStringFunct) {
        mathCastStringFunct.getParam().get(0).accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.CAST_STRING);
        return app(o);
    }

    @Override
    public V visitMathCastCharFunct(MathCastCharFunct<V> mathCastCharFunct) {
        mathCastCharFunct.getParam().get(0).accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.CAST_CHAR);
        return app(o);
    }

    @Override
    public V visitTextStringCastNumberFunct(TextStringCastNumberFunct<V> textStringCastNumberFunct) {
        textStringCastNumberFunct.getParam().get(0).accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.CAST_STRING_NUMBER);
        return app(o);
    }

    @Override
    public V visitTextCharCastNumberFunct(TextCharCastNumberFunct<V> textCharCastNumberFunct) {
        textCharCastNumberFunct.getParam().get(0).accept(this);
        textCharCastNumberFunct.getParam().get(1).accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.CAST_CHAR_NUMBER);
        return app(o);
    }

    @Override
    public final V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct) {
        textJoinFunct.getParam().accept(this);
        int n = textJoinFunct.getParam().get().size();
        JSONObject o = makeLeaf(C.TEXT_JOIN, textJoinFunct).put(C.NUMBER, n);
        return app(o);
    }

    @Override
    public final V visitMethodVoid(MethodVoid<V> methodVoid) {
        encloseFlowStatementScope(() -> {
            beginPhrase(methodVoid);
            registerMethodDeclaration(methodVoid.getMethodName());

            app(makeNode(C.COMMENT).put(C.TARGET, C.METHOD_VOID));

            // Start of method
            ExprList<V> parameters = methodVoid.getParameters();
            if ( !parameters.get().stream().allMatch(parameter -> parameter instanceof VarDeclaration<?>) ) {
                throw new DbcException(String.format("Expected %s to be a list of VarDeclarations", parameters.get()));
            }

            Lists.reverse(parameters.get())
                .stream()
                .map(parameter -> (VarDeclaration<V>) parameter)
                .forEach(parameter -> {
                    JSONObject o = makeLeaf(C.VAR_DECLARATION, methodVoid).put(C.TYPE, parameter.getTypeVar()).put(C.NAME, parameter.getName());
                    app(o);
                });

            methodVoid.getBody().accept(this);

            flowControlStatements.forEach(statement -> {
                if ( statement.get(C.TARGET).equals(METHOD_END) ) {
                    statement.put(C.TARGET, opArray.size());
                } else {
                    throw new DbcException("Invalid flow control expression");
                }
            });

            parameters.get().stream()
                .map(parameter -> (VarDeclaration<V>) parameter)
                .forEach(parameter -> app(makeLeaf(C.UNBIND_VAR, methodVoid).put(C.NAME, parameter.getName())));

            endPhrase(methodVoid);

            app(makeNode(C.RETURN).put(C.VALUES, false));
        });
        return null;
    }


    @Override
    public final V visitMethodReturn(MethodReturn<V> methodReturn) {
        encloseFlowStatementScope(() -> {
            beginPhrase(methodReturn);

            registerMethodDeclaration(methodReturn.getMethodName());

            app(makeNode(C.COMMENT).put(C.TARGET, C.METHOD_RETURN));

            // Start of method
            ExprList<V> parameters = methodReturn.getParameters();
            if ( !parameters.get().stream().allMatch(parameter -> parameter instanceof VarDeclaration<?>) ) {
                throw new DbcException(String.format("Expected %s to be a list of VarDeclarations", parameters.get()));
            }

            Lists.reverse(parameters.get())
                .stream()
                .map(parameter -> (VarDeclaration<V>) parameter)
                .forEach(parameter -> {
                    JSONObject o = makeLeaf(C.VAR_DECLARATION, methodReturn).put(C.TYPE, parameter.getTypeVar()).put(C.NAME, parameter.getName());
                    app(o);
                });

            methodReturn.getBody().accept(this);

            methodReturn.getReturnValue().accept(this);

            flowControlStatements.forEach(statement -> {
                if ( statement.get(C.TARGET).equals(METHOD_END) ) {
                    statement.put(C.TARGET, opArray.size());
                } else {
                    throw new DbcException("Invalid flow control expression");
                }
            });

            parameters.get().stream()
                .map(parameter -> (VarDeclaration<V>) parameter)
                .forEach(parameter -> app(makeLeaf(C.UNBIND_VAR, methodReturn).put(C.NAME, parameter.getName())));

            endPhrase(methodReturn);

            app(makeNode(C.RETURN).put(C.VALUES, true));
        });
        return null;
    }

    @Override
    public final V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn) {
        beginPhrase(methodIfReturn);

        app(makeNode(C.COMMENT).put(C.TARGET, C.IF_RETURN));

        methodIfReturn.getCondition().accept(this);
        JSONObject jumpOverReturn = makeNode(C.JUMP).put(C.CONDITIONAL, false);
        app(jumpOverReturn);

        methodIfReturn.getReturnValue().accept(this);
        JSONObject jumpToMethodEnd = makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS).put(C.TARGET, METHOD_END);
        flowControlStatements.add(jumpToMethodEnd);

        endPhrase(methodIfReturn);
        app(jumpToMethodEnd);
        jumpOverReturn.put(C.TARGET, opArray.size());
        return null;
    }

    @Override
    public final V visitMethodStmt(MethodStmt<V> methodStmt) {
        methodStmt.getMethod().accept(this);
        return null;
    }

    @Override
    public final V visitMethodCall(MethodCall<V> methodCall) {
        beginPhrase(methodCall);
        app(makeNode(C.COMMENT).put(C.TARGET, C.METHOD_CALL));

        JSONObject returnAddress = makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST);
        app(returnAddress);
        methodCall.getParametersValues().get()
            .forEach(v -> v.accept(this));
        app(createJumpToMethod(methodCall));
        returnAddress.put(C.VALUE, opArray.size());
        endPhrase(methodCall);
        return null;
    }

    @Override
    public V visitConnectConst(ConnectConst<V> connectConst) {
        throw new DbcException("Operation not supported");
    }

    @SuppressWarnings("unchecked")
    @Override
    public V visitAssertStmt(AssertStmt<V> assertStmt) {
        assertStmt.getAssert().accept(this);
        ((Binary<Void>) assertStmt.getAssert()).getLeft().accept((IVisitor<Void>) this);
        ((Binary<Void>) assertStmt.getAssert()).getRight().accept((IVisitor<Void>) this);
        String op = ((Binary<Void>) assertStmt.getAssert()).getOp().toString();
        JSONObject o = makeLeaf(C.ASSERT_ACTION, assertStmt).put(C.MSG, assertStmt.getMsg()).put(C.OP, op);
        return app(o);
    }

    @Override
    public V visitDebugAction(DebugAction<V> debugAction) {
        debugAction.getValue().accept(this);
        JSONObject o = makeLeaf(C.DEBUG_ACTION, debugAction);
        return app(o);
    }

    private JSONObject createJumpToMethod(MethodCall<V> methodCall) {
        JSONObject jump = makeNode(C.JUMP).put(C.CONDITIONAL, C.ALWAYS);
        methodCalls.putIfAbsent(methodCall.getMethodName(), new ArrayList<>());
        methodCalls.get(methodCall.getMethodName()).add(jump);
        return jump;
    }

    private void registerMethodDeclaration(String methodName) {
        methodDeclarations.put(methodName, opArray.size());
    }

    private List<JSONObject> getRegisteredMethodCalls(String methodName) {
        return methodCalls.getOrDefault(methodName, new ArrayList<>());
    }

    protected final ConfigurationComponent getConfigurationComponent(String userDefinedName) {
        ConfigurationComponent configurationComponent = this.configuration.getConfigurationComponent(userDefinedName);
        return configurationComponent;
    }

    /**
     * Processes the optional duration and adds it to the stack if it is available.
     *
     * @param duration the duration, may be null, represents distance/duration/degrees depending on the method
     * @return whether the duration was pushed to the the stack
     */
    protected final boolean processOptionalDuration(MotorDuration<V> duration) {
        if ( duration != null ) {
            duration.getValue().accept(this);
            return true;
        } else {
            return false;
        }
    }

    private void bindMethods() {
        methodDeclarations
            .forEach((methodName, address) -> getRegisteredMethodCalls(methodName)
                .forEach(jsonObject -> jsonObject.put(C.TARGET, address)));
    }

    protected final DriveDirection getDriveDirection(boolean isReverse) {
        return isReverse ? DriveDirection.BACKWARD : DriveDirection.FOREWARD;
    }

    protected final TurnDirection getTurnDirection(boolean isReverse) {
        return isReverse ? TurnDirection.RIGHT : TurnDirection.LEFT;
    }

    public final void generateCodeFromPhrases(List<List<Phrase<V>>> phrasesSet) {
        List<Phrase<V>> methods = new ArrayList<>();
        for ( List<Phrase<V>> phrases : phrasesSet ) {
            for ( Phrase<V> phrase : phrases ) {
                boolean isMethod = phrase instanceof MethodVoid<?> || phrase instanceof MethodReturn<?>;
                if ( !isMethod ) {
                    phrase.accept(this);
                    continue;
                }
                methods.add(phrase);
            }
        }
        app(makeNode(C.STOP));
        for ( Phrase<V> method : methods ) {
            method.accept(this);
        }
        bindMethods();
    }

    protected final JSONObject makeNode(String opCode) {
        JSONObject operation = new JSONObject().put(C.OPCODE, opCode);
        if ( !initiateList.isEmpty() ) {
            initiateList.removeIf(IS_INVALID_BLOCK_ID);
            operation.put(C.HIGHTLIGHT_PLUS, new ArrayList<>(initiateList));
            notYetTerminatedList.addAll(initiateList);
            initiateList.clear();
        }
        if ( !terminateList.isEmpty() ) {
            terminateList.removeIf(IS_INVALID_BLOCK_ID);
            operation.put(C.HIGHTLIGHT_MINUS, new ArrayList<>(terminateList));
            notYetTerminatedList.removeAll(terminateList);
            terminateList.clear();
        }
        return operation;
    }

    protected JSONObject makeLeaf(String opCode, Phrase<V> phrase) {
        String blockId = phrase.getProperty().getBlocklyId();
        boolean isSameBlockAgain = notYetTerminatedList.contains(blockId) && terminateList.contains(blockId);
        if (isSameBlockAgain) terminateList.remove(blockId);

        beginPhrase(phrase);

        JSONObject node = makeNode(opCode);

        endPhrase(phrase);
        return node;
    }

    protected void endPhrase(Phrase<V> phrase) {
        String blocklyId = phrase.getProperty().getBlocklyId();
        if ( debugger && !terminateList.contains(blocklyId) ) terminateList.add(blocklyId);
    }

    protected void beginPhrase(Phrase<V> phrase) {
        String blocklyId = phrase.getProperty().getBlocklyId();
        if ( debugger && !initiateList.contains(blocklyId) && !notYetTerminatedList.contains(blocklyId) ) initiateList.add(blocklyId);
    }

    protected V app(JSONObject o) {
        this.getOpArray().add(o);
        return null;
    }

    protected final void generateProgramPrefix(boolean withWrapping) {
        // nothing to do
    }

    public List<JSONObject> getOpArray() {
        return this.opArray;
    }

    /**
     * Enclose the scope of flowControlStatements while runnable is run
     *
     * @param runnable
     */
    protected void encloseFlowStatementScope(Runnable runnable) {
        List<JSONObject> flowControlTemp = new ArrayList<>(flowControlStatements);
        flowControlStatements.clear();

        runnable.run();

        flowControlStatements.clear();
        flowControlStatements.addAll(flowControlTemp);
    }
}
