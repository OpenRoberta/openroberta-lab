package de.fhg.iais.roberta.visitor.validate;

import java.util.HashMap;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
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
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class AbstractCollectorVisitor implements ILanguageVisitor<Void> {

    private final ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders;
    private final HashMap<Integer, Integer> waitsInLoops = new HashMap<>();
    private int loopCounter = 0;
    private int currentLoop = 0;

    protected AbstractCollectorVisitor(ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        this.beanBuilders = beanBuilders;
    }

    protected <T extends IProjectBean.IBuilder<?>> T getBuilder(Class<T> clazz) {
        return this.beanBuilders.getInstance(clazz);
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().accept(this);
        rgbColor.getG().accept(this);
        rgbColor.getB().accept(this);
        rgbColor.getA().accept(this);
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( var.isGlobal() ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addVisitedVariable(var);
        }
        if ( var.getVarType().equals(BlocklyType.ARRAY)
            || var.getVarType().equals(BlocklyType.ARRAY_BOOLEAN)
            || var.getVarType().equals(BlocklyType.ARRAY_NUMBER)
            || var.getVarType().equals(BlocklyType.ARRAY_COLOUR)
            || var.getVarType().equals(BlocklyType.ARRAY_CONNECTION)
            || var.getVarType().equals(BlocklyType.ARRAY_IMAGE)
            || var.getVarType().equals(BlocklyType.ARRAY_STRING) ) {
            this.getBuilder(UsedHardwareBean.Builder.class).setListsUsed(true);
        }
        var.getValue().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addGlobalVariable(var.getName());
        this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(var.getName());
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        unary.getExpr().accept(this);
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        binary.getLeft().accept(this);
        binary.getRight().accept(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        mathPowerFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.getBuilder(UsedHardwareBean.Builder.class).setListsUsed(true);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        return null;
    }

    @Override
    public final Void visitExprList(ExprList<Void> exprList) {
        exprList.get().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public final Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getExpr().accept(this);
        String variableName = assignStmt.getName().getValue();
        if ( this.getBuilder(UsedHardwareBean.Builder.class).containsGlobalVariable(variableName) ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addMarkedVariableAsGlobal(variableName);
        }
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            ifStmt.getExpr().get(i).accept(this);
            ifStmt.getThenList().get(i).accept(this);
        }
        ifStmt.getElseList().accept(this);
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt<Void> nnStepStmt) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getExpr().getKind().hasName("EXPR_LIST") ) {
            ExprList<Void> exprList = (ExprList<Void>) repeatStmt.getExpr();
            String varName = ((Var<Void>) exprList.get().get(0)).getValue();
            this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(varName);
            exprList.accept(this);
        } else {
            repeatStmt.getExpr().accept(this);
        }

        if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
            increaseLoopCounter();
            repeatStmt.getList().accept(this);
            this.currentLoop--;
        } else {
            repeatStmt.getList().accept(this);
        }
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        boolean isInWaitStmt = this.waitsInLoops.get(this.currentLoop) != 0;
        this.getBuilder(UsedHardwareBean.Builder.class).putLoopLabel(this.currentLoop, isInWaitStmt);
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        stmtList.get().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        if ( this.waitsInLoops.get(this.loopCounter) != null ) {
            increaseWaitStmsInLoop();
            waitStmt.getStatements().accept(this);
            decreaseWaitStmtInLoop();
        } else {
            waitStmt.getStatements().accept(this);
        }
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        waitTimeStmt.getTime().accept(this);
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        textPrintFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> textComment) {
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        getSubFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        for ( Expr<Void> expr : indexOfFunct.getParam() ) {
            expr.accept(this);
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        listCreate.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        listRepeat.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        mathConstrainFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        mathNumPropFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        mathOnListFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        mathSingleFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        mathCastStringFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        mathCastCharFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        textStringCastNumberFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        textCharCastNumberFunct.getParam().stream().forEach(expr -> expr.accept(this));
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        textJoinFunct.getParam().accept(this);
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodVoid);
        methodVoid.getParameters().accept(this);
        methodVoid.getBody().accept(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodReturn);
        methodReturn.getParameters().accept(this);
        methodReturn.getBody().accept(this);
        methodReturn.getReturnValue().accept(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        methodIfReturn.getCondition().accept(this);
        methodIfReturn.getReturnValue().accept(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().accept(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        methodCall.getParametersValues().accept(this);
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        assertStmt.getAssert().accept(this);
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        debugAction.getValue().accept(this);
        return null;
    }

    private void increaseLoopCounter() {
        this.loopCounter++;
        this.currentLoop = this.loopCounter;
        this.getBuilder(UsedHardwareBean.Builder.class).putLoopLabel(this.loopCounter, false);
        this.waitsInLoops.put(this.loopCounter, 0);
    }

    private void decreaseWaitStmtInLoop() {
        int count;
        count = this.waitsInLoops.get(this.loopCounter);
        this.waitsInLoops.put(this.loopCounter, --count);
    }

    private void increaseWaitStmsInLoop() {
        int count;
        count = this.waitsInLoops.get(this.loopCounter);
        this.waitsInLoops.put(this.loopCounter, ++count);
    }
}
