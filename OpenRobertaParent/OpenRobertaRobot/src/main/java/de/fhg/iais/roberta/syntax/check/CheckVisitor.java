package de.fhg.iais.roberta.syntax.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.lang.AstLanguageVisitor;

public abstract class CheckVisitor implements AstLanguageVisitor<Void> {

    protected final List<String> globalVariables = new ArrayList<String>();
    protected final List<String> declaredVariables = new ArrayList<String>();
    protected ArrayList<VarDeclaration<Void>> visitedVars = new ArrayList<VarDeclaration<Void>>();
    private final List<Method<Void>> userDefinedMethods = new ArrayList<Method<Void>>();
    private final Set<String> markedVariablesAsGlobal = new HashSet<String>();

    private boolean isProgramEmpty = false;

    private int loopCounter = 0;
    private int currenLoop = 0;
    private final HashMap<Integer, Boolean> loopsLabelContainer = new HashMap<Integer, Boolean>();
    private final HashMap<Integer, Integer> waitsInLoops = new HashMap<>();

    /**
     * Returns map of loop number and boolean value that indicates if the loop is labeled in Blockly program.
     *
     * @return map of loops and boolean value
     */
    public Map<Integer, Boolean> getloopsLabelContainer() {
        return this.loopsLabelContainer;
    }

    public Set<String> getMarkedVariablesAsGlobal() {
        return this.markedVariablesAsGlobal;
    }

    public List<Method<Void>> getUserDefinedMethods() {
        return this.userDefinedMethods;
    }

    public boolean isProgramEmpty() {
        return this.isProgramEmpty;
    }

    protected void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        collectGlobalVariables(phrasesSet);
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                if ( isMainBlock(phrase) ) {
                    this.isProgramEmpty = phrases.size() == 2;
                } else {
                    phrase.visit(this);
                }
            }
        }
    }

    protected void collectGlobalVariables(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            visitIfMain(phrase);
        }
    }

    private void visitIfMain(Phrase<Void> phrase) {
        if ( isMainBlock(phrase) ) {
            phrase.visit(this);
        }
    }

    protected boolean isMainBlock(Phrase<Void> phrase) {
        return phrase.getKind().getName().equals("MAIN_TASK");
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
        rgbColor.getR().visit(this);
        rgbColor.getG().visit(this);
        rgbColor.getB().visit(this);
        rgbColor.getA().visit(this);
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( !var.toString().contains("false, false") ) {
            this.visitedVars.add(var);
        }
        var.getValue().visit(this);
        this.globalVariables.add(var.getName());
        this.declaredVariables.add(var.getName());
        return null;
    }

    public ArrayList<VarDeclaration<Void>> getVisitedVars() {
        return this.visitedVars;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        unary.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        binary.getLeft().visit(this);
        binary.getRight().visit(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        mathPowerFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        exprList.get().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getExpr().visit(this);
        String variableName = assignStmt.getName().getValue();
        if ( this.globalVariables.contains(variableName) ) {
            this.markedVariablesAsGlobal.add(variableName);
        }
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            ifStmt.getExpr().get(i).visit(this);
            ifStmt.getThenList().get(i).visit(this);
        }
        ifStmt.getElseList().visit(this);
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getExpr().getKind().hasName("EXPR_LIST") ) {
            ExprList<Void> exprList = (ExprList<Void>) repeatStmt.getExpr();
            String varName = ((Var<Void>) exprList.get().get(0)).getValue();
            this.declaredVariables.add(varName);
            exprList.visit(this);
        } else {
            repeatStmt.getExpr().visit(this);
        }

        if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
            increaseLoopCounter();
            repeatStmt.getList().visit(this);
            this.currenLoop--;
        } else {
            repeatStmt.getList().visit(this);
        }
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        boolean isInWaitStmt = this.waitsInLoops.get(this.currenLoop) != 0;
        this.loopsLabelContainer.put(this.currenLoop, isInWaitStmt);
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        stmtList.get().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        if ( this.waitsInLoops.get(this.loopCounter) != null ) {
            increseWaitStmsInLoop();
            waitStmt.getStatements().visit(this);
            decreaseWaitStmtInLoop();
        } else {
            waitStmt.getStatements().visit(this);
        }
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        waitTimeStmt.getTime().visit(this);
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        textPrintFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> textComment) {
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        getSubFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        for ( Expr<Void> expr : indexOfFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        listCreate.getValue().visit(this);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        listRepeat.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        mathConstrainFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        mathNumPropFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        mathOnListFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        mathRandomIntFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        mathSingleFunct.getParam().stream().forEach(expr -> expr.visit(this));
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        textJoinFunct.getParam().visit(this);
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.userDefinedMethods.add(methodVoid);
        methodVoid.getParameters().visit(this);
        methodVoid.getBody().visit(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.userDefinedMethods.add(methodReturn);
        methodReturn.getParameters().visit(this);
        methodReturn.getBody().visit(this);
        methodReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        methodIfReturn.getCondition().visit(this);
        methodIfReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        methodCall.getParametersValues().visit(this);
        return null;
    }

    private void increaseLoopCounter() {
        this.loopCounter++;
        this.currenLoop = this.loopCounter;
        this.loopsLabelContainer.put(this.loopCounter, false);
        this.waitsInLoops.put(this.loopCounter, 0);
    }

    private void decreaseWaitStmtInLoop() {
        int count;
        count = this.waitsInLoops.get(this.loopCounter);
        this.waitsInLoops.put(this.loopCounter, --count);
    }

    private void increseWaitStmsInLoop() {
        int count;
        count = this.waitsInLoops.get(this.loopCounter);
        this.waitsInLoops.put(this.loopCounter, ++count);
    }

}
