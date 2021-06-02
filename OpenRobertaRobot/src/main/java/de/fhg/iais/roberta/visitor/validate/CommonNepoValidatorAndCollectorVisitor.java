package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
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
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class CommonNepoValidatorAndCollectorVisitor implements ILanguageVisitor<Void> {

    protected final UsedMethodBean.Builder usedMethodBuilder;
    protected final UsedHardwareBean.Builder usedHardwareBuilder;
    protected final ConfigurationAst robotConfiguration;
    private final ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders;
    private final HashMap<Integer, Integer> waitsInLoops = new HashMap<>();
    protected int errorCount = 0;
    protected int warningCount = 0;
    protected List<String> errorAndWarningMessages = new ArrayList<>();;
    private int loopCounter = 0;
    private int currentLoop = 0;

    protected CommonNepoValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) //
    {
        this.robotConfiguration = robotConfiguration;
        this.beanBuilders = beanBuilders;
        this.usedMethodBuilder = getBuilder(UsedMethodBean.Builder.class);
        this.usedHardwareBuilder = getBuilder(UsedHardwareBean.Builder.class);
    }

    protected <T extends IProjectBean.IBuilder<?>> T getBuilder(Class<T> clazz) {
        return this.beanBuilders.getInstance(clazz);
    }

    @Override
    public final Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        requiredComponentVisited(assertStmt, assertStmt.getAssert());
        return null;
    }

    @Override
    public final Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        requiredComponentVisited(assignStmt, assignStmt.getExpr());
        String variableName = assignStmt.getName().getValue();
        if ( this.getBuilder(UsedHardwareBean.Builder.class).containsGlobalVariable(variableName) ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addMarkedVariableAsGlobal(variableName);
        }
        return null;
    }

    @Override
    public final Void visitBinary(Binary<Void> phrase) {
        requiredComponentVisited(phrase, phrase.getLeft(), phrase.getRight());
        return null;
    }

    @Override
    public final Void visitBoolConst(BoolConst<Void> boolConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        requiredComponentVisited(debugAction, debugAction.getValue());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.getBuilder(UsedHardwareBean.Builder.class).setListsUsed(true);
        return null;
    }

    @Override
    public final Void visitExprList(ExprList<Void> exprList) {
        requiredComponentVisited(exprList, exprList.get());
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        requiredComponentVisited(getSubFunct, getSubFunct.getParam());
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        requiredComponentVisited(ifStmt, ifStmt.getExpr());
        requiredComponentVisited(ifStmt, ifStmt.getThenList());
        requiredComponentVisited(ifStmt, ifStmt.getElseList());
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        requiredComponentVisited(indexOfFunct, indexOfFunct.getParam());
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        requiredComponentVisited(lengthOfIsEmptyFunct, lengthOfIsEmptyFunct.getParam());
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        requiredComponentVisited(listCreate, listCreate.getValue());
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        IListElementOperations iOp = listGetIndex.getElementOperation();
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.usedMethodBuilder.addUsedMethod(op);
        }
        requiredComponentVisited(listGetIndex, listGetIndex.getParam());
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        usedMethodBuilder.addUsedMethod(FunctionNames.LISTS_REPEAT);
        requiredComponentVisited(listRepeat, listRepeat.getParam());
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        IListElementOperations iOp = listSetIndex.getElementOperation();
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.usedMethodBuilder.addUsedMethod(op);
        }
        requiredComponentVisited(listSetIndex, listSetIndex.getParam());
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        requiredComponentVisited(mainTask, mainTask.getVariables());
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(mathCastCharFunct, mathCastCharFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(mathCastStringFunct, mathCastStringFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        requiredComponentVisited(mathConstrainFunct, mathConstrainFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        usedMethodBuilder.addUsedMethod(mathNumPropFunct.getFunctName());
        requiredComponentVisited(mathNumPropFunct, mathNumPropFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        usedMethodBuilder.addUsedMethod(mathOnListFunct.getFunctName());
        requiredComponentVisited(mathOnListFunct, mathOnListFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
        requiredComponentVisited(mathPowerFunct, mathPowerFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.RANDOM_DOUBLE);
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.RANDOM);
        requiredComponentVisited(mathRandomIntFunct, mathRandomIntFunct.getParam());
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        if ( mathSingleFunct.getFunctName() == FunctionNames.POW10 ) {
            usedMethodBuilder.addUsedMethod(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            usedMethodBuilder.addUsedMethod(mathSingleFunct.getFunctName());
        }
        requiredComponentVisited(mathSingleFunct, mathSingleFunct.getParam());
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        requiredComponentVisited(methodCall, methodCall.getParametersValues());
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        requiredComponentVisited(methodIfReturn, methodIfReturn.getCondition());
        if (!methodIfReturn.getReturnType().equals(BlocklyType.VOID)) {
            requiredComponentVisited(methodIfReturn, methodIfReturn.getReturnValue());
        }
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodReturn);
        requiredComponentVisited(methodReturn, methodReturn.getParameters(), methodReturn.getBody());
        requiredComponentVisited(methodReturn, methodReturn.getReturnValue());
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        requiredComponentVisited(methodStmt, methodStmt.getMethod());
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodVoid);
        requiredComponentVisited(methodVoid, methodVoid.getParameters());
        requiredComponentVisited(methodVoid, methodVoid.getBody());
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt<Void> nnStepStmt) {
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        return null;
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getExpr().getKind().hasName("EXPR_LIST") ) {
            ExprList<Void> exprList = (ExprList<Void>) repeatStmt.getExpr();
            String varName = ((Var<Void>) exprList.get().get(0)).getValue();
            this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(varName);
            requiredComponentVisited(repeatStmt, exprList);
        } else {
            requiredComponentVisited(repeatStmt, repeatStmt.getExpr());
        }

        if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
            increaseLoopCounter();
            requiredComponentVisited(repeatStmt, repeatStmt.getList());
            this.currentLoop--;
        } else {
            requiredComponentVisited(repeatStmt, repeatStmt.getList());
        }
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        requiredComponentVisited(rgbColor, rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
        optionalComponentVisited(rgbColor.getA());
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
        requiredComponentVisited(stmtList, stmtList.get());
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> textComment) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(textCharCastNumberFunct, textCharCastNumberFunct.getParam());
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        requiredComponentVisited(textJoinFunct, textJoinFunct.getParam());
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        requiredComponentVisited(textPrintFunct, textPrintFunct.getParam());
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(textStringCastNumberFunct, textStringCastNumberFunct.getParam());
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> phrase) {
        requiredComponentVisited(phrase, phrase.getExpr());
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
        optionalComponentVisited(var.getValue());
        this.getBuilder(UsedHardwareBean.Builder.class).addGlobalVariable(var.getName());
        this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(var.getName());
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        if ( this.waitsInLoops.get(this.loopCounter) != null ) {
            increaseWaitStmsInLoop();
            requiredComponentVisited(waitStmt, waitStmt.getStatements());
            decreaseWaitStmtInLoop();
        } else {
            requiredComponentVisited(waitStmt, waitStmt.getStatements());
        }
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        requiredComponentVisited(waitTimeStmt, waitTimeStmt.getTime());
        return null;
    }

    /**
     * if the subPhrase is not the {@link EmptyExpr}, visit the subPhrase
     *
     * @param subPhrase to be visited, if not empty
     */
    protected void optionalComponentVisited(Phrase<Void> subPhrase) {
        if ( !(subPhrase instanceof EmptyExpr<?>) ) {
            subPhrase.accept(this);
        }
    }

    /**
     * for the superPhrase check, that subPhrases are not empty. If true, visit the subPhrases, otherwise add error information to the superPhrase.
     *
     * @param superPhrase phrase, whose components should be checked and visited
     * @param subPhrases the component of superPhrase to be checked and visited
     */
    @SafeVarargs
    protected final void requiredComponentVisited(Phrase<Void> superPhrase, Phrase<Void>... subPhrases) {
        for ( Phrase<Void> subPhrase : subPhrases ) {
            if ( subPhrase instanceof EmptyExpr<?> ) {
                addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
            } else {
                subPhrase.accept(this);
            }
        }
    }

    protected final <T extends Phrase<Void>> void requiredComponentVisited(Phrase<Void> superPhrase, List<T> subPhrases) {
        for ( Phrase<Void> subPhrase : subPhrases ) {
            if ( subPhrase instanceof EmptyExpr<?> ) {
                addErrorToPhrase(superPhrase, "ERROR_MISSING_PARAMETER");
            } else {
                subPhrase.accept(this);
            }
        }
    }

    protected void addErrorToPhrase(final Phrase<Void> phrase, final String message) {
        phrase.addInfo(NepoInfo.error(message));
        errorAndWarningMessages.add(message);
        errorCount++;
    }

    protected void addWarningToPhrase(final Phrase<Void> phrase, final String message) {
        phrase.addInfo(NepoInfo.warning(message));
        errorAndWarningMessages.add(message);
        warningCount++;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public List<String> getErrorAndWarningMessages() {
        return Collections.unmodifiableList(errorAndWarningMessages);
    }

    public int getWarningCount() {
        return this.warningCount;
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
