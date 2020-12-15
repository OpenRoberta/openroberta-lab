package de.fhg.iais.roberta.visitor.lang.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
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
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class AbstractStackMachineVisitor<V> implements ILanguageVisitor<V> {
    private JSONObject fctDecls = new JSONObject();
    private List<JSONObject> opArray = new ArrayList<>();
    private final List<List<JSONObject>> opArrayStack = new ArrayList<>();
    protected final ConfigurationAst configuration;

    protected AbstractStackMachineVisitor(ConfigurationAst configuration) {
        this.configuration = configuration;
    }

    @Override
    public final V visitNumConst(NumConst<V> numConst) {
        JSONObject o = mk(C.EXPR, numConst).put(C.EXPR, numConst.getKind().getName()).put(C.VALUE, numConst.getValue());
        return app(o);
    }

    @Override
    public final V visitMathConst(MathConst<V> mathConst) {
        JSONObject o = mk(C.EXPR, mathConst).put(C.EXPR, C.MATH_CONST).put(C.VALUE, mathConst.getMathConst());
        return app(o);
    }

    @Override
    public final V visitBoolConst(BoolConst<V> boolConst) {
        JSONObject o = mk(C.EXPR, boolConst).put(C.EXPR, boolConst.getKind().getName()).put(C.VALUE, boolConst.getValue());
        return app(o);
    }

    @Override
    public final V visitStringConst(StringConst<V> stringConst) {
        JSONObject o = mk(C.EXPR, stringConst).put(C.EXPR, stringConst.getKind().getName());
        o.put(C.VALUE, stringConst.getValue().replaceAll("[<>\\$]", ""));
        return app(o);
    }

    @Override
    public final V visitNullConst(NullConst<V> nullConst) {
        JSONObject o = mk(C.EXPR, nullConst).put(C.EXPR, "C." + nullConst.getKind().getName());
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
        JSONObject o = mk(C.EXPR, colorConst).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, colorId);
        return app(o);
    }

    @Override
    public final V visitRgbColor(RgbColor<V> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        JSONObject o = mk(C.EXPR, rgbColor).put(C.EXPR, C.RGB_COLOR_CONST);
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
        JSONObject o = mk(C.EXPR, var).put(C.EXPR, C.VAR).put(C.NAME, var.getValue());
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
        JSONObject o = mk(C.VAR_DECLARATION, var).put(C.TYPE, var.getTypeVar()).put(C.NAME, var.getName());
        return app(o);
    }

    @Override
    public final V visitUnary(Unary<V> unary) {
        unary.getExpr().accept(this);
        JSONObject o = mk(C.EXPR, unary).put(C.EXPR, C.UNARY).put(C.OP, unary.getOp());
        return app(o);
    }

    @Override
    public final V visitBinary(Binary<V> binary) {
        switch ( binary.getOp() ) {
            case AND:
            case OR:
                JSONObject stmtListEnd = mk(C.FLOW_CONTROL, binary).put(C.KIND, C.IF_STMT).put(C.CONDITIONAL, false).put(C.BREAK, true);
                JSONObject t = mk(C.EXPR, binary).put(C.EXPR, "BOOL_CONST").put(C.VALUE, true);
                JSONObject f = mk(C.EXPR, binary).put(C.EXPR, "BOOL_CONST").put(C.VALUE, false);
                pushOpArray();
                binary.getLeft().accept(this);
                pushOpArray();
                if ( binary.getOp() == Op.AND ) {
                    binary.getRight().accept(this);
                } else {
                    this.getOpArray().add(t);
                }
                this.getOpArray().add(stmtListEnd);
                List<JSONObject> thenStmts = popOpArray();
                JSONObject ifTrue = mk(C.IF_TRUE_STMT, binary).put(C.STMT_LIST, thenStmts);
                this.getOpArray().add(ifTrue);
                if ( binary.getOp() == Op.AND ) {
                    this.getOpArray().add(f);
                } else {
                    binary.getRight().accept(this);
                }
                this.getOpArray().add(stmtListEnd);
                List<JSONObject> ifThenElseOps = popOpArray();
                JSONObject lazyAndOr = mk(C.IF_STMT, binary).put(C.STMT_LIST, ifThenElseOps);
                return app(lazyAndOr);

            default:
                binary.getLeft().accept(this);
                binary.getRight().accept(this);
                JSONObject o;
                // FIXME: The math change should be removed from the binary expression since it is a statement
                switch ( binary.getOp() ) {
                    case MATH_CHANGE:
                        o = mk(C.MATH_CHANGE, binary).put(C.NAME, ((Var<V>) binary.getLeft()).getValue());
                        break;
                    case TEXT_APPEND:
                        o = mk(C.TEXT_APPEND, binary).put(C.NAME, ((Var<V>) binary.getLeft()).getValue());
                        break;

                    default:
                        o = mk(C.EXPR, binary).put(C.EXPR, C.BINARY).put(C.OP, binary.getOp());
                        break;
                }
                return app(o);

        }

    }

    @Override
    public final V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct) {
        mathPowerFunct.getParam().get(0).accept(this);
        mathPowerFunct.getParam().get(1).accept(this);
        JSONObject o = mk(C.EXPR, mathPowerFunct).put(C.EXPR, C.BINARY).put(C.OP, mathPowerFunct.getFunctName());
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
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.STRING_CONST).put(C.VALUE, "");
                break;
            case BOOLEAN:
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.BOOL_CONST).put(C.VALUE, "true");
                break;
            case NUMBER_INT:
            case NUMBER:
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0);
                break;
            case COLOR:
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.LED_COLOR_CONST).put(C.VALUE, 3);
                break;
            case NULL:
            case CONNECTION:
            case ARRAY_BOOLEAN:
            case ARRAY_COLOUR:
            case ARRAY_CONNECTION:
            case ARRAY_IMAGE:
            case ARRAY_NUMBER:
            case ARRAY_STRING:
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.NULL_CONST);
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
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.IMAGE_CONST).put(C.VALUE, jsonImage);
                break;
            case CAPTURED_TYPE: // TODO: get the captured type
                o = mk(C.EXPR, emptyExpr).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0);
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
        JSONObject o = mk(C.ASSIGN_STMT, assignStmt).put(C.NAME, assignStmt.getName().getValue());
        return app(o);
    }

    @Override
    public final V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().accept(this);
        return null;
    }

    @Override
    public final V visitIfStmt(IfStmt<V> ifStmt) {
        JSONObject stmtListEnd = mk(C.FLOW_CONTROL, ifStmt).put(C.KIND, C.IF_STMT).put(C.CONDITIONAL, false).put(C.BREAK, true);
        int numberOfThens = ifStmt.getExpr().size();
        if ( ifStmt.isTernary() ) {
            Assert.isTrue(numberOfThens == 1);
            Assert.isFalse(ifStmt.getElseList().get().isEmpty());
        }
        pushOpArray();
        // TODO: better a list of pairs. pair of lists needs this kind of for
        for ( int i = 0; i < numberOfThens; i++ ) {
            ifStmt.getExpr().get(i).accept(this);
            pushOpArray();
            ifStmt.getThenList().get(i).accept(this);
            this.getOpArray().add(stmtListEnd);
            List<JSONObject> thenStmts = popOpArray();
            JSONObject ifTrue = mk(C.IF_TRUE_STMT, ifStmt).put(C.STMT_LIST, thenStmts);
            this.getOpArray().add(ifTrue);
        }
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            ifStmt.getElseList().accept(this);
        }
        this.getOpArray().add(stmtListEnd);
        List<JSONObject> ifThenElseOps = popOpArray();
        JSONObject o = mk(C.IF_STMT, ifStmt).put(C.STMT_LIST, ifThenElseOps);
        return app(o);
    }

    @Override
    public final V visitNNStepStmt(NNStepStmt<V> nnStepStmt) {
        for ( Expr<V> e : nnStepStmt.getIl() ) {
            e.accept(this);
        }
        JSONObject o = mk(C.NNSTEP_STMT, nnStepStmt);
        app(o);
        for ( Var<V> v : nnStepStmt.getOl() ) {
            JSONObject ov = mk(C.ASSIGN_STMT, nnStepStmt).put(C.NAME, v.getValue());
            app(ov);
        }
        return null;
    }

    @Override
    public final V visitRepeatStmt(RepeatStmt<V> repeatStmt) {
        Mode mode = repeatStmt.getMode();

        // the very special case of a wait stmt. The AST is not perfectly designed for this case
        if ( mode == Mode.WAIT ) {
            repeatStmt.getExpr().accept(this);
            pushOpArray();
            repeatStmt.getList().accept(this);
            JSONObject stmtListEnd = mk(C.FLOW_CONTROL, repeatStmt).put(C.KIND, C.WAIT_STMT).put(C.CONDITIONAL, false).put(C.BREAK, true);
            this.getOpArray().add(stmtListEnd);
            List<JSONObject> waitBody = popOpArray();
            JSONObject o = mk(C.IF_TRUE_STMT, repeatStmt).put(C.STMT_LIST, waitBody);
            return app(o);
        }

        // The "real" repeat cases
        if ( mode == Mode.FOREVER || mode == Mode.TIMES || mode == Mode.FOR || mode == Mode.FOR_EACH || mode == Mode.FOREVER_ARDU ) {
            pushOpArray();
            repeatStmt.getList().accept(this);
            List<JSONObject> repeatBody = popOpArray();
            JSONObject cont = mk(C.REPEAT_STMT_CONTINUATION, repeatStmt).put(C.MODE, mode).put(C.STMT_LIST, repeatBody);
            JSONObject repeat = mk(C.REPEAT_STMT, repeatStmt).put(C.MODE, mode).put(C.STMT_LIST, Arrays.asList(cont));
            if ( mode == Mode.FOREVER || mode == Mode.FOREVER_ARDU ) {
                return app(repeat);
            } else if ( mode == Mode.FOR_EACH ) {
                pushOpArray();
                repeatStmt.getExpr().accept(this);
                List<JSONObject> timesExprs =
                    popOpArray()
                        .stream()
                        .filter(d -> (d.get(C.OPCODE) != C.TERMINATE_BLOCK && d.get(C.OPCODE) != C.INITIATE_BLOCK))
                        .collect(Collectors.toList());
                timesExprs.remove(0);
                JSONObject varDecl = timesExprs.remove(0);
                JSONObject listDecl = timesExprs.remove(0);
                timesExprs.remove(0);
                Assert.isTrue(timesExprs.size() == 0);
                timesExprs.add(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
                timesExprs.add(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 1));
                //timesExprs.add(inExpr);
                this.getOpArray().addAll(timesExprs);
                String varName = varDecl.getString(C.NAME);
                String runVarName = varName + "_runningVariable";
                String listName = listDecl.getString(C.NAME);
                cont.put(C.NAME, varName).put(C.EACH_COUNTER, runVarName).put(C.LIST, listName);
                repeat.put(C.NAME, varName).put(C.EACH_COUNTER, runVarName).put(C.LIST, listName);
                return app(repeat);
            } else {
                pushOpArray();
                repeatStmt.getExpr().accept(this); // expected: expr list length 4: var, start, end, incr
                List<JSONObject> timesExprs =
                    popOpArray()
                        .stream()
                        .filter(d -> (d.get(C.OPCODE) != C.TERMINATE_BLOCK && d.get(C.OPCODE) != C.INITIATE_BLOCK))
                        .collect(Collectors.toList());
                ;
                JSONObject decl = timesExprs.remove(0);
                this.getOpArray().addAll(timesExprs);
                String runVarName = decl.getString(C.NAME);
                cont.put(C.NAME, runVarName);
                repeat.put(C.NAME, runVarName);
                return app(repeat);
            }
        }

        if ( mode == Mode.WHILE || mode == Mode.UNTIL ) {
            pushOpArray();
            repeatStmt.getExpr().accept(this);
            List<JSONObject> expr = popOpArray();
            pushOpArray();
            repeatStmt.getList().accept(this);
            List<JSONObject> body = popOpArray();
            JSONObject cont = mk(C.REPEAT_STMT_CONTINUATION, repeatStmt).put(C.MODE, mode);
            JSONObject repeat = mk(C.REPEAT_STMT, repeatStmt).put(C.MODE, mode).put(C.STMT_LIST, Arrays.asList(cont));
            List<JSONObject> exprAndBody = new ArrayList<>();
            exprAndBody.addAll(expr);
            exprAndBody.add(mk(C.FLOW_CONTROL, repeatStmt).put(C.KIND, C.REPEAT_STMT).put(C.CONDITIONAL, true).put(C.BREAK, true).put(C.BOOLEAN, false));
            exprAndBody.addAll(body);
            cont.put(C.STMT_LIST, exprAndBody);
            return app(repeat);
        }

        throw new DbcException("invalid repeat mode: " + mode);
    }

    @Override
    public final V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().accept(this);
        return null;
    }

    @Override
    public final V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon) {
        boolean breakAndNotContinue = stmtFlowCon.getFlow() == Flow.BREAK;
        String targetStmt = breakAndNotContinue ? C.REPEAT_STMT : C.REPEAT_STMT_CONTINUATION;
        JSONObject o = mk(C.FLOW_CONTROL, stmtFlowCon).put(C.KIND, targetStmt).put(C.CONDITIONAL, false).put(C.BREAK, breakAndNotContinue);
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
            JSONObject o = mk(C.CREATE_DEBUG_ACTION, mainTask);
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
        pushOpArray();
        List<Stmt<V>> repeatStmts = waitStmt.getStatements().get();
        for ( Stmt<V> repeatStmt : repeatStmts ) {
            repeatStmt.accept(this);
        }
        this.getOpArray().add(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 1));
        this.getOpArray().add(mk(C.WAIT_TIME_STMT));
        List<JSONObject> waitBlocks = popOpArray();
        JSONObject o = mk(C.WAIT_STMT, waitStmt).put(C.STMT_LIST, waitBlocks);
        return app(o);
    }

    @Override
    public final V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt) {
        waitTimeStmt.getTime().accept(this);
        JSONObject o = mk(C.WAIT_TIME_STMT, waitTimeStmt);
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
        o = mk(C.COMMENT, textComment).put(C.VALUE, textComment.getTextComment());
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
            mk(C.EXPR, getSubFunct)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, C.LIST_GET_SUBLIST)
                .put(C.POSITION, getSubFunct.getStrParam().stream().map(x -> x.toString().toLowerCase()).toArray());

        return app(o);
    }

    @Override
    public final V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct) {
        indexOfFunct.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            mk(C.EXPR, indexOfFunct)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, C.LIST_FIND_ITEM)
                .put(C.POSITION, indexOfFunct.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().get(0).accept(this);
        JSONObject o = mk(C.EXPR, lengthOfIsEmptyFunct).put(C.EXPR, C.LIST_OPERATION).put(C.OP, lengthOfIsEmptyFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListCreate(ListCreate<V> listCreate) {
        listCreate.getValue().accept(this);
        int n = listCreate.getValue().get().size();

        JSONObject o = mk(C.EXPR, listCreate).put(C.EXPR, C.CREATE_LIST).put(C.NUMBER, n);
        return app(o);
    }

    @Override
    public final V visitListSetIndex(ListSetIndex<V> listSetIndex) {
        listSetIndex.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            mk(C.LIST_OPERATION, listSetIndex)
                .put(C.OP, listSetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listSetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListGetIndex(ListGetIndex<V> listGetIndex) {
        listGetIndex.getParam().forEach(x -> x.accept(this));
        JSONObject o =
            mk(C.EXPR, listGetIndex)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, listGetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listGetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitListRepeat(ListRepeat<V> listRepeat) {
        listRepeat.getParam().forEach(x -> x.accept(this));
        JSONObject o = mk(C.EXPR, listRepeat).put(C.EXPR, C.CREATE_LIST_REPEAT);
        return app(o);
    }

    @Override
    public final V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct) {
        mathConstrainFunct.getParam().get(0).accept(this);
        mathConstrainFunct.getParam().get(1).accept(this);
        mathConstrainFunct.getParam().get(2).accept(this);
        JSONObject o = mk(C.EXPR, mathConstrainFunct).put(C.EXPR, C.MATH_CONSTRAIN_FUNCTION);
        return app(o);
    }

    @Override
    public final V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct) {
        mathNumPropFunct.getParam().get(0).accept(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            mathNumPropFunct.getParam().get(1).accept(this);
        }
        JSONObject o = mk(C.EXPR, mathNumPropFunct).put(C.EXPR, C.MATH_PROP_FUNCT).put(C.OP, mathNumPropFunct.getFunctName());
        return app(o);
    }

    @Override
    public final V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct) {
        mathOnListFunct.getParam().forEach(x -> x.accept(this));
        JSONObject o = mk(C.EXPR, mathOnListFunct).put(C.EXPR, C.MATH_ON_LIST).put(C.OP, mathOnListFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public final V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct) {
        JSONObject o = mk(C.EXPR, mathRandomFloatFunct).put(C.EXPR, C.RANDOM_DOUBLE);
        return app(o);
    }

    @Override
    public final V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().get(0).accept(this);
        mathRandomIntFunct.getParam().get(1).accept(this);
        JSONObject o = mk(C.EXPR, mathRandomIntFunct).put(C.EXPR, C.RANDOM_INT);
        return app(o);
    }

    @Override
    public final V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct) {
        mathSingleFunct.getParam().get(0).accept(this);
        JSONObject o = mk(C.EXPR, mathSingleFunct).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, mathSingleFunct.getFunctName());
        return app(o);
    }

    @Override
    public V visitMathCastStringFunct(MathCastStringFunct<V> mathCastStringFunct) {
        mathCastStringFunct.getParam().get(0).accept(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.CAST_STRING);
        return app(o);
    }

    @Override
    public V visitMathCastCharFunct(MathCastCharFunct<V> mathCastCharFunct) {
        mathCastCharFunct.getParam().get(0).accept(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.CAST_CHAR);
        return app(o);
    }

    @Override
    public V visitTextStringCastNumberFunct(TextStringCastNumberFunct<V> textStringCastNumberFunct) {
        textStringCastNumberFunct.getParam().get(0).accept(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.CAST_STRING_NUMBER);
        return app(o);
    }

    @Override
    public V visitTextCharCastNumberFunct(TextCharCastNumberFunct<V> textCharCastNumberFunct) {
        textCharCastNumberFunct.getParam().get(0).accept(this);
        textCharCastNumberFunct.getParam().get(1).accept(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.CAST_CHAR_NUMBER);
        return app(o);
    }

    @Override
    public final V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct) {
        textJoinFunct.getParam().accept(this);
        int n = textJoinFunct.getParam().get().size();
        JSONObject o = mk(C.TEXT_JOIN, textJoinFunct).put(C.NUMBER, n);
        return app(o);
    }

    @Override
    public final V visitMethodVoid(MethodVoid<V> methodVoid) {
        pushOpArray();
        methodVoid.getParameters().accept(this);
        popOpArray();
        pushOpArray();
        methodVoid.getBody().accept(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL, methodVoid).put(C.KIND, C.METHOD_CALL_VOID).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.getOpArray().add(terminateMethodCall);
        List<JSONObject> methodBody = popOpArray();
        JSONObject o = mk(C.METHOD_VOID, methodVoid).put(C.NAME, methodVoid.getMethodName()).put(C.STATEMENTS, methodBody);
        this.getFctDecls().put(methodVoid.getMethodName(), o);
        return null;
    }

    @Override
    public final V visitMethodReturn(MethodReturn<V> methodReturn) {
        pushOpArray();
        methodReturn.getParameters().accept(this);
        popOpArray();
        pushOpArray();
        methodReturn.getBody().accept(this);
        methodReturn.getReturnValue().accept(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL, methodReturn).put(C.KIND, C.METHOD_CALL_RETURN).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.getOpArray().add(terminateMethodCall);
        List<JSONObject> methodBody = popOpArray();
        JSONObject o =
            mk(C.METHOD_RETURN, methodReturn).put(C.TYPE, methodReturn.getReturnType()).put(C.NAME, methodReturn.getMethodName()).put(C.STATEMENTS, methodBody);
        this.getFctDecls().put(methodReturn.getMethodName(), o);
        return null;
    }

    @Override
    public final V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn) {
        methodIfReturn.getCondition().accept(this);
        pushOpArray();
        methodIfReturn.getReturnValue().accept(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL, methodIfReturn).put(C.KIND, C.METHOD_CALL_RETURN).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.getOpArray().add(terminateMethodCall);
        List<JSONObject> returnValueExpr = popOpArray();
        JSONObject o = mk(C.IF_RETURN, methodIfReturn).put(C.STMT_LIST, returnValueExpr);
        return app(o);
    }

    @Override
    public final V visitMethodStmt(MethodStmt<V> methodStmt) {
        methodStmt.getMethod().accept(this);
        return null;
    }

    @Override
    public final V visitMethodCall(MethodCall<V> methodCall) {
        List<Expr<V>> parametersNames = methodCall.getParameters().get();
        pushOpArray();
        parametersNames.stream().forEach(n -> n.accept(this));
        List<String> names =
            this
                .getOpArray()
                .stream()
                .filter(d -> (d.get(C.OPCODE) != C.TERMINATE_BLOCK && d.get(C.OPCODE) != C.INITIATE_BLOCK))
                .map(d -> d.getString(C.NAME))
                .collect(Collectors.toList());
        names = Lists.reverse(names);
        popOpArray();
        List<Expr<V>> parametersValues = methodCall.getParametersValues().get();
        parametersValues.stream().forEach(v -> v.accept(this));
        // TODO: better AST needed. Push and pop used only to get the parameter names
        String methodKind = methodCall.getReturnType() == BlocklyType.VOID ? C.METHOD_CALL_VOID : C.METHOD_CALL_RETURN;
        JSONObject call = mk(methodKind, methodCall).put(C.NAME, methodCall.getMethodName()).put(C.NAMES, names);
        return app(call);
    }

    @Override
    public V visitConnectConst(ConnectConst<V> connectConst) {
        throw new DbcException("Operation not supported");
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

    protected final DriveDirection getDriveDirection(boolean isReverse) {
        return isReverse ? DriveDirection.BACKWARD : DriveDirection.FOREWARD;
    }

    protected final TurnDirection getTurnDirection(boolean isReverse) {
        return isReverse ? TurnDirection.RIGHT : TurnDirection.LEFT;
    }

    public final void generateCodeFromPhrases(List<List<Phrase<V>>> phrasesSet) {
        for ( List<Phrase<V>> phrases : phrasesSet ) {
            for ( Phrase<V> phrase : phrases ) {
                phrase.accept(this);
            }
        }
    }

    protected final JSONObject mk(String opCode) {
        return new JSONObject().put(C.OPCODE, opCode);
    }

    protected JSONObject mk(String opCode, Phrase<V> phrase) {
        return new JSONObject().put(C.OPCODE, opCode).put(C.BLOCK_ID, phrase.getProperty().getBlocklyId());
    }

    protected V app(JSONObject o) {
        if ( o.has(C.BLOCK_ID) ) {
            this.getOpArray().add(mk(C.INITIATE_BLOCK).put(C.BLOCK_ID, o.get(C.BLOCK_ID)).put(C.OP, o.get(C.OPCODE)));
            this.getOpArray().add(o);
            this.getOpArray().add(mk(C.TERMINATE_BLOCK).put(C.BLOCK_ID, o.get(C.BLOCK_ID)));
        } else {
            this.getOpArray().add(o);
        }
        return null;
    }

    protected final void pushOpArray() {
        this.opArrayStack.add(this.getOpArray());
        this.setOpArray(new ArrayList<>());
    }

    protected final List<JSONObject> popOpArray() {
        List<JSONObject> opArray = this.getOpArray();
        this.setOpArray(this.opArrayStack.remove(this.opArrayStack.size() - 1));
        return opArray;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V visitAssertStmt(AssertStmt<V> assertStmt) {
        assertStmt.getAssert().accept(this);
        ((Binary<Void>) assertStmt.getAssert()).getLeft().accept((IVisitor<Void>) this);
        ((Binary<Void>) assertStmt.getAssert()).getRight().accept((IVisitor<Void>) this);
        String op = ((Binary<Void>) assertStmt.getAssert()).getOp().toString();
        JSONObject o = mk(C.ASSERT_ACTION, assertStmt).put(C.MSG, assertStmt.getMsg()).put(C.OP, op);
        return app(o);
    }

    @Override
    public V visitDebugAction(DebugAction<V> debugAction) {
        debugAction.getValue().accept(this);
        JSONObject o = mk(C.DEBUG_ACTION, debugAction);
        return app(o);
    }

    protected final void generateProgramPrefix(boolean withWrapping) {
        // TODO Auto-generated method stub

    }

    public List<JSONObject> getOpArray() {
        return this.opArray;
    }

    public void setOpArray(List<JSONObject> opArray) {
        this.opArray = opArray;
    }

    public JSONObject getFctDecls() {
        return this.fctDecls;
    }

    public void setFctDecls(JSONObject fctDecls) {
        this.fctDecls = fctDecls;
    }
}
