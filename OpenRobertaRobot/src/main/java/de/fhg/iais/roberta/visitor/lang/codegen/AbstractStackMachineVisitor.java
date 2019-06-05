package de.fhg.iais.roberta.visitor.lang.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.components.Configuration;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class AbstractStackMachineVisitor<V> implements ILanguageVisitor<V> {
    protected int loopsCounter = 0;
    protected int currentLoop = 0;
    protected int stmtsNumber = 0;
    protected int methodsNumber = 0;

    protected JSONObject fctDecls = new JSONObject();
    protected List<JSONObject> opArray = new ArrayList<>();
    protected final List<List<JSONObject>> opArrayStack = new ArrayList<>();
    protected final Configuration configuration;

    protected AbstractStackMachineVisitor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public V visitNumConst(NumConst<V> numConst) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, numConst.getKind().getName()).put(C.VALUE, numConst.getValue());
        return app(o);
    }

    @Override
    public V visitMathConst(MathConst<V> mathConst) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.MATH_CONST).put(C.VALUE, mathConst.getMathConst());
        return app(o);
    }

    @Override
    public V visitBoolConst(BoolConst<V> boolConst) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, boolConst.getKind().getName()).put(C.VALUE, boolConst.getValue());
        return app(o);
    }

    @Override
    public V visitStringConst(StringConst<V> stringConst) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, stringConst.getKind().getName());
        o.put(C.VALUE, stringConst.getValue().replaceAll("[<>\\$]", ""));
        return app(o);
    }

    @Override
    public V visitNullConst(NullConst<V> nullConst) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, "C." + nullConst.getKind().getName());
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
        JSONObject o = mk(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, colorId);
        return app(o);
    }

    @Override
    public V visitRgbColor(RgbColor<V> rgbColor) {
        rgbColor.getR().visit(this);
        rgbColor.getG().visit(this);
        rgbColor.getB().visit(this);
        JSONObject o = mk(C.RGB_COLOR_CONST);
        return app(o);
    }

    @Override
    public V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public V visitVar(Var<V> var) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.VAR).put(C.NAME, var.getValue());
        return app(o);
    }

    @Override
    public V visitVarDeclaration(VarDeclaration<V> var) {
        if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
            ExprList<V> list = (ExprList<V>) var.getValue();
            if ( list.get().size() == 2 ) {
                list.get().get(1).visit(this);
            } else {
                list.get().get(0).visit(this);
            }
        } else {
            var.getValue().visit(this);
        }
        JSONObject o = mk(C.VAR_DECLARATION).put(C.TYPE, var.getTypeVar()).put(C.NAME, var.getName());
        return app(o);
    }

    @Override
    public V visitUnary(Unary<V> unary) {
        unary.getExpr().visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.UNARY).put(C.OP, unary.getOp());
        return app(o);
    }

    @Override
    public V visitBinary(Binary<V> binary) {
        binary.getLeft().visit(this);
        binary.getRight().visit(this);
        JSONObject o;
        // FIXME: The math change should be removed from the binary expression since it is a statement
        switch ( binary.getOp() ) {
            case MATH_CHANGE:
                o = mk(C.MATH_CHANGE);
                break;
            case TEXT_APPEND:
                o = mk(C.TEXT_APPEND);
                break;
            default:
                o = mk(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, binary.getOp());
                break;
        }
        return app(o);
    }

    @Override
    public V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct) {
        mathPowerFunct.getParam().get(0).visit(this);
        mathPowerFunct.getParam().get(1).visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.BINARY).put(C.OP, mathPowerFunct.getFunctName());
        return app(o);
    }

    @Override
    public V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public V visitEmptyList(EmptyList<V> emptyList) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public V visitEmptyExpr(EmptyExpr<V> emptyExpr) {
        JSONObject o;
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                o = mk(C.EXPR).put(C.EXPR, C.STRING_CONST).put(C.VALUE, "");
                break;
            case BOOLEAN:
                o = mk(C.EXPR).put(C.EXPR, C.BOOL_CONST).put(C.VALUE, "true");
                break;
            case NUMBER_INT:
            case NUMBER:
                o = mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0);
                break;
            case COLOR:
                o = mk(C.EXPR).put(C.EXPR, C.LED_COLOR_CONST).put(C.VALUE, 3);
                break;
            case NULL:
                o = mk(C.EXPR).put(C.EXPR, C.NULL_CONST);
                break;
            default:
                throw new DbcException("Operation not supported");
        }
        return app(o);
    }

    @Override
    public V visitExprList(ExprList<V> exprList) {
        for ( Expr<V> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public V visitAssignStmt(AssignStmt<V> assignStmt) {
        assignStmt.getExpr().visit(this);
        JSONObject o = mk(C.ASSIGN_STMT).put(C.NAME, assignStmt.getName().getValue());
        return app(o);
    }

    @Override
    public V visitExprStmt(ExprStmt<V> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public V visitIfStmt(IfStmt<V> ifStmt) {
        if ( ifStmt.isTernary() ) {
            throw new DbcException("Operation not supported");
        } else {
            pushOpArray();
            int numberOfThens = ifStmt.getExpr().size();
            JSONObject stmtListEnd = mk(C.FLOW_CONTROL).put(C.KIND, C.IF_STMT).put(C.CONDITIONAL, false).put(C.BREAK, true);
            // TODO: better a list of pairs. pair of lists needs this kind of for
            for ( int i = 0; i < numberOfThens; i++ ) {
                ifStmt.getExpr().get(i).visit(this);
                pushOpArray();
                ifStmt.getThenList().get(i).visit(this);
                this.opArray.add(stmtListEnd);
                List<JSONObject> thenStmts = popOpArray();
                JSONObject ifTrue = mk(C.IF_TRUE_STMT).put(C.STMT_LIST, thenStmts);
                this.opArray.add(ifTrue);
            }
            if ( !ifStmt.getElseList().get().isEmpty() ) {
                ifStmt.getElseList().visit(this);
                this.opArray.add(stmtListEnd);
            }
            List<JSONObject> ifThenElseOps = popOpArray();
            JSONObject o = mk(C.IF_STMT).put(C.STMT_LIST, ifThenElseOps);
            return app(o);
        }
    }

    @Override
    public V visitRepeatStmt(RepeatStmt<V> repeatStmt) {
        Mode mode = repeatStmt.getMode();

        // the very special case of a wait stmt. The AST is not perfectly designed for this case
        if ( mode == Mode.WAIT ) {
            repeatStmt.getExpr().visit(this);
            pushOpArray();
            repeatStmt.getList().visit(this);
            JSONObject stmtListEnd = mk(C.FLOW_CONTROL).put(C.KIND, C.WAIT_STMT).put(C.CONDITIONAL, false).put(C.BREAK, true);
            this.opArray.add(stmtListEnd);
            List<JSONObject> waitBody = popOpArray();
            JSONObject o = mk(C.IF_TRUE_STMT).put(C.STMT_LIST, waitBody);
            return app(o);
        }

        // The "real" repeat cases
        if ( (mode == Mode.FOREVER) || (mode == Mode.TIMES) || (mode == Mode.FOR) ) {
            pushOpArray();
            repeatStmt.getList().visit(this);
            List<JSONObject> repeatBody = popOpArray();
            JSONObject cont = mk(C.REPEAT_STMT_CONTINUATION).put(C.MODE, mode).put(C.STMT_LIST, repeatBody);
            JSONObject repeat = mk(C.REPEAT_STMT).put(C.MODE, mode).put(C.STMT_LIST, Arrays.asList(cont));
            if ( mode == Mode.FOREVER ) {
                return app(repeat);
            } else {
                pushOpArray();
                repeatStmt.getExpr().visit(this); // expected: expr list length 4: var, start, end, incr
                List<JSONObject> timesExprs = popOpArray();
                JSONObject decl = timesExprs.remove(0);
                this.opArray.addAll(timesExprs);
                String runVarName = decl.getString(C.NAME);
                cont.put(C.NAME, runVarName);
                repeat.put(C.NAME, runVarName);
                return app(repeat);
            }
        }

        if ( (mode == Mode.WHILE) || (mode == Mode.UNTIL) ) {
            pushOpArray();
            repeatStmt.getExpr().visit(this);
            List<JSONObject> expr = popOpArray();
            pushOpArray();
            repeatStmt.getList().visit(this);
            List<JSONObject> body = popOpArray();
            JSONObject cont = mk(C.REPEAT_STMT_CONTINUATION).put(C.MODE, mode);
            JSONObject repeat = mk(C.REPEAT_STMT).put(C.MODE, mode).put(C.STMT_LIST, Arrays.asList(cont));
            List<JSONObject> exprAndBody = new ArrayList<>();
            exprAndBody.addAll(expr);
            exprAndBody.add(mk(C.FLOW_CONTROL).put(C.KIND, C.REPEAT_STMT).put(C.CONDITIONAL, true).put(C.BREAK, true).put(C.BOOLEAN, false));
            exprAndBody.addAll(body);
            cont.put(C.STMT_LIST, exprAndBody);
            return app(repeat);
        }
        throw new DbcException("invalid repeat mode: " + mode);
    }

    @Override
    public V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon) {
        boolean breakAndNotContinue = stmtFlowCon.getFlow() == Flow.BREAK;
        String targetStmt = breakAndNotContinue ? C.REPEAT_STMT : C.REPEAT_STMT_CONTINUATION;
        JSONObject o = mk(C.FLOW_CONTROL).put(C.KIND, targetStmt).put(C.CONDITIONAL, false).put(C.BREAK, breakAndNotContinue);
        return app(o);
    }

    @Override
    public V visitStmtList(StmtList<V> stmtList) {
        for ( Stmt<V> stmt : stmtList.get() ) {
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public V visitMainTask(MainTask<V> mainTask) {
        mainTask.getVariables().visit(this);
        if ( mainTask.getDebug().equals("TRUE") ) {
            JSONObject o = mk(C.CREATE_DEBUG_ACTION);
            return app(o);
        }
        return null;
    }

    @Override
    public V visitActivityTask(ActivityTask<V> activityTask) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public V visitWaitStmt(WaitStmt<V> waitStmt) {
        pushOpArray();
        List<Stmt<V>> repeatStmts = waitStmt.getStatements().get();
        for ( Stmt<V> repeatStmt : repeatStmts ) {
            repeatStmt.visit(this);
        }
        this.opArray.add(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 1));
        this.opArray.add(mk(C.WAIT_TIME_STMT));
        List<JSONObject> waitBlocks = popOpArray();
        JSONObject o = mk(C.WAIT_STMT).put(C.STMT_LIST, waitBlocks);
        return app(o);
    }

    @Override
    public V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt) {
        waitTimeStmt.getTime().visit(this);
        JSONObject o = mk(C.WAIT_TIME_STMT);
        return app(o);
    }

    @Override
    public V visitLocation(Location<V> location) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct) {
        return null;
    }

    @Override
    public V visitStmtTextComment(StmtTextComment<V> textComment) {
        return null;
    }

    @Override
    public V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    @Override
    public V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public V visitGetSubFunct(GetSubFunct<V> getSubFunct) {
        getSubFunct.getParam().forEach(x -> x.visit(this));

        JSONObject o =
            mk(C.EXPR)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, C.LIST_GET_SUBLIST)
                .put(C.POSITION, getSubFunct.getStrParam().stream().map(x -> x.toString().toLowerCase()).toArray());

        return app(o);
    }

    @Override
    public V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct) {
        indexOfFunct.getParam().forEach(x -> x.visit(this));
        JSONObject o =
            mk(C.EXPR).put(C.EXPR, C.LIST_OPERATION).put(C.OP, C.LIST_FIND_ITEM).put(C.POSITION, indexOfFunct.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.LIST_OPERATION).put(C.OP, lengthOfIsEmptyFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitListCreate(ListCreate<V> listCreate) {
        listCreate.getValue().visit(this);
        int n = listCreate.getValue().get().size();

        JSONObject o = mk(C.EXPR).put(C.EXPR, C.CREATE_LIST).put(C.NUMBER, n);
        return app(o);
    }

    @Override
    public V visitListSetIndex(ListSetIndex<V> listSetIndex) {
        listSetIndex.getParam().forEach(x -> x.visit(this));
        JSONObject o =
            mk(C.LIST_OPERATION)
                .put(C.OP, listSetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listSetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitListGetIndex(ListGetIndex<V> listGetIndex) {
        listGetIndex.getParam().forEach(x -> x.visit(this));
        JSONObject o =
            mk(C.EXPR)
                .put(C.EXPR, C.LIST_OPERATION)
                .put(C.OP, listGetIndex.getElementOperation().toString().toLowerCase())
                .put(C.POSITION, listGetIndex.getLocation().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitListRepeat(ListRepeat<V> listRepeat) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct) {
        mathConstrainFunct.getParam().get(0).visit(this);
        mathConstrainFunct.getParam().get(1).visit(this);
        mathConstrainFunct.getParam().get(2).visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.MATH_CONSTRAIN_FUNCTION);
        return app(o);
    }

    @Override
    public V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct) {
        mathNumPropFunct.getParam().get(0).visit(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            mathNumPropFunct.getParam().get(1).visit(this);
        }
        JSONObject o = mk(C.MATH_PROP_FUNCT).put(C.NAME, mathNumPropFunct.getFunctName());
        return app(o);
    }

    @Override
    public V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct) {
        mathOnListFunct.getParam().forEach(x -> x.visit(this));
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.MATH_ON_LIST).put(C.OP, mathOnListFunct.getFunctName().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct) {
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.RANDOM_DOUBLE);
        return app(o);
    }

    @Override
    public V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().get(0).visit(this);
        mathRandomIntFunct.getParam().get(1).visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.RANDOM_INT);
        return app(o);
    }

    @Override
    public V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct) {
        mathSingleFunct.getParam().get(0).visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, mathSingleFunct.getFunctName());
        return app(o);
    }

    @Override
    public V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct) {
        textJoinFunct.getParam().visit(this);
        JSONObject o = mk(C.TEXT_JOIN);
        return app(o);
    }

    @Override
    public V visitMethodVoid(MethodVoid<V> methodVoid) {
        pushOpArray();
        methodVoid.getParameters().visit(this);
        popOpArray();
        pushOpArray();
        methodVoid.getBody().visit(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL).put(C.KIND, C.METHOD_CALL_VOID).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.opArray.add(terminateMethodCall);
        List<JSONObject> methodBody = popOpArray();
        JSONObject o = mk(C.METHOD_VOID).put(C.NAME, methodVoid.getMethodName()).put(C.STATEMENTS, methodBody);
        this.fctDecls.put(methodVoid.getMethodName(), o);
        return null;
    }

    @Override
    public V visitMethodReturn(MethodReturn<V> methodReturn) {
        pushOpArray();
        methodReturn.getParameters().visit(this);
        popOpArray();
        pushOpArray();
        methodReturn.getBody().visit(this);
        methodReturn.getReturnValue().visit(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL).put(C.KIND, C.METHOD_CALL_RETURN).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.opArray.add(terminateMethodCall);
        List<JSONObject> methodBody = popOpArray();
        JSONObject o = mk(C.METHOD_RETURN).put(C.TYPE, methodReturn.getReturnType()).put(C.NAME, methodReturn.getMethodName()).put(C.STATEMENTS, methodBody);
        this.fctDecls.put(methodReturn.getMethodName(), o);
        return null;
    }

    @Override
    public V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn) {
        methodIfReturn.getCondition().visit(this);
        pushOpArray();
        methodIfReturn.getReturnValue().visit(this);
        JSONObject terminateMethodCall = mk(C.FLOW_CONTROL).put(C.KIND, C.METHOD_CALL_RETURN).put(C.CONDITIONAL, false).put(C.BREAK, true);
        this.opArray.add(terminateMethodCall);
        List<JSONObject> returnValueExpr = popOpArray();
        JSONObject o = mk(C.IF_RETURN).put(C.STMT_LIST, returnValueExpr);
        return app(o);
    }

    @Override
    public V visitMethodStmt(MethodStmt<V> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public V visitMethodCall(MethodCall<V> methodCall) {
        List<Expr<V>> parametersNames = methodCall.getParameters().get();
        pushOpArray();
        parametersNames.stream().forEach(n -> n.visit(this));
        List<String> names = this.opArray.stream().map(d -> d.getString(C.NAME)).collect(Collectors.toList());
        names = Lists.reverse(names);
        popOpArray();
        List<Expr<V>> parametersValues = methodCall.getParametersValues().get();
        parametersValues.stream().forEach(v -> v.visit(this));
        // TODO: better AST needed. Push and pop used only to get the parameter names
        String methodKind = methodCall.getReturnType() == BlocklyType.VOID ? C.METHOD_CALL_VOID : C.METHOD_CALL_RETURN;
        JSONObject call = mk(methodKind).put(C.NAME, methodCall.getMethodName()).put(C.NAMES, names);
        return app(call);
    }

    @Override
    public V visitConnectConst(ConnectConst<V> connectConst) {
        throw new DbcException("Operation not supported");
    }

    protected ConfigurationComponent getConfigurationComponent(String userDefinedName) {
        ConfigurationComponent configurationComponent = this.configuration.getConfigurationComponent(userDefinedName);
        return configurationComponent;
    }

    protected void appendDuration(MotorDuration<V> duration) {
        if ( duration != null ) {
            duration.getValue().visit(this);
        }
    }

    protected DriveDirection getDriveDirection(boolean isReverse) {
        return isReverse ? DriveDirection.BACKWARD : DriveDirection.FOREWARD;
    }

    protected TurnDirection getTurnDirection(boolean isReverse) {
        return isReverse ? TurnDirection.RIGHT : TurnDirection.LEFT;
    }

    protected void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<V>>> phrasesSet) {
        for ( ArrayList<Phrase<V>> phrases : phrasesSet ) {
            for ( Phrase<V> phrase : phrases ) {
                phrase.visit(this);
            }
        }
    }

    protected JSONObject mk(String opCode) {
        return new JSONObject().put(C.OPCODE, opCode);
    }

    protected V app(JSONObject o) {
        this.opArray.add(o);
        return null;
    }

    protected void pushOpArray() {
        this.opArrayStack.add(this.opArray);
        this.opArray = new ArrayList<>();
    }

    protected List<JSONObject> popOpArray() {
        List<JSONObject> opArray = this.opArray;
        this.opArray = this.opArrayStack.remove(this.opArrayStack.size() - 1);
        return opArray;
    }

    protected void generateProgramPrefix(boolean withWrapping) {
        // TODO Auto-generated method stub

    }
}
