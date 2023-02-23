package de.fhg.iais.roberta.visitor.validate;

import java.util.HashMap;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class CommonNepoValidatorAndCollectorVisitor extends AbstractValidatorAndCollectorVisitor implements ILanguageVisitor<Void> {

    private final HashMap<Integer, Integer> waitsInLoops = new HashMap<>();
    private int loopCounter = 0;
    private int currentLoop = 0;

    private NNBean nnBean; // set by the start block, used to check neuron name consistency etc.

    protected CommonNepoValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) //
    {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        requiredComponentVisited(assertStmt, assertStmt.asserts);
        return null;
    }

    @Override
    public final Void visitAssignStmt(AssignStmt assignStmt) {
        requiredComponentVisited(assignStmt, assignStmt.expr);
        UsedHardwareBean.Builder builder = this.getBuilder(UsedHardwareBean.Builder.class);
        String variableName = assignStmt.name.name;
        if ( builder.containsGlobalVariable(variableName) ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addMarkedVariableAsGlobal(variableName);
        }
        if ( !builder.containsInScopeVariable(variableName) ) {
            addErrorToPhrase(assignStmt, "SCOPE_ERROR"); // key will be overwritten by blockly
        }
        return null;
    }

    @Override
    public final Void visitBinary(Binary phrase) {
        requiredComponentVisited(phrase, phrase.left, phrase.getRight());
        return null;
    }

    @Override
    public final Void visitBoolConst(BoolConst boolConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        requiredComponentVisited(debugAction, debugAction.value);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.getBuilder(UsedHardwareBean.Builder.class).setListsUsed(true);
        return null;
    }

    @Override
    public final Void visitExprList(ExprList exprList) {
        requiredComponentVisited(exprList, exprList.get());
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        requiredComponentVisited(getSubFunct, getSubFunct.param);
        if ( getSubFunct.param.get(0).toString().contains("ListCreate ") ||
            getSubFunct.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(getSubFunct, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt ifStmt) {
        requiredComponentVisited(ifStmt, ifStmt.expr);
        requiredComponentVisited(ifStmt, ifStmt.thenList);
        requiredComponentVisited(ifStmt, ifStmt.elseList);
        return null;
    }

    @Override
    public Void visitTernaryExpr(TernaryExpr ternaryExpr) {
        requiredComponentVisited(ternaryExpr, ternaryExpr.condition);
        requiredComponentVisited(ternaryExpr, ternaryExpr.thenPart);
        requiredComponentVisited(ternaryExpr, ternaryExpr.elsePart);
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        requiredComponentVisited(indexOfFunct, indexOfFunct.param);
        if ( indexOfFunct.param.get(0).toString().contains("ListCreate ") ||
            indexOfFunct.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(indexOfFunct, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        requiredComponentVisited(lengthOfIsEmptyFunct, lengthOfIsEmptyFunct.param);
        if ( lengthOfIsEmptyFunct.param.get(0).toString().contains("ListCreate ") ||
            lengthOfIsEmptyFunct.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(lengthOfIsEmptyFunct, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        requiredComponentVisited(listCreate, listCreate.exprList);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        IListElementOperations iOp = listGetIndex.getElementOperation();
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.usedMethodBuilder.addUsedMethod(op);
        }
        requiredComponentVisited(listGetIndex, listGetIndex.param);
        if ( listGetIndex.param.get(0).toString().contains("ListCreate ") ||
            listGetIndex.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(listGetIndex, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        usedMethodBuilder.addUsedMethod(FunctionNames.LISTS_REPEAT);
        requiredComponentVisited(listRepeat, listRepeat.param);
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        IListElementOperations iOp = listSetIndex.mode;
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.usedMethodBuilder.addUsedMethod(op);
        }
        requiredComponentVisited(listSetIndex, listSetIndex.param);
        if ( listSetIndex.param.get(0).toString().contains("ListCreate ") ||
            listSetIndex.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(listSetIndex, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        String optError = this.nnBeanBuilder.setNN(mainTask.data);
        if ( optError != null ) {
            addErrorToPhrase(mainTask, optError);
        }
        this.nnBean = this.nnBeanBuilder.build();
        requiredComponentVisited(mainTask, mainTask.variables);
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(mathCastCharFunct, mathCastCharFunct.param);
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(mathCastStringFunct, mathCastStringFunct.param);
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        requiredComponentVisited(mathConstrainFunct, mathConstrainFunct.param);
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        usedMethodBuilder.addUsedMethod(mathNumPropFunct.functName);
        requiredComponentVisited(mathNumPropFunct, mathNumPropFunct.param);
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        usedMethodBuilder.addUsedMethod(mathOnListFunct.functName);
        requiredComponentVisited(mathOnListFunct, mathOnListFunct.param);
        if ( mathOnListFunct.param.get(0).toString().contains("ListCreate ") ||
            mathOnListFunct.param.get(0).toString().contains("ListRepeat ") ) {
            addErrorToPhrase(mathOnListFunct, "BLOCK_USED_INCORRECTLY");
        }
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
        requiredComponentVisited(mathPowerFunct, mathPowerFunct.param);
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.RANDOM_DOUBLE);
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.RANDOM);
        requiredComponentVisited(mathRandomIntFunct, mathRandomIntFunct.param);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        if ( mathSingleFunct.functName == FunctionNames.POW10 ) {
            usedMethodBuilder.addUsedMethod(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            usedMethodBuilder.addUsedMethod(mathSingleFunct.functName);
        }
        requiredComponentVisited(mathSingleFunct, mathSingleFunct.param);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        requiredComponentVisited(methodCall, methodCall.getParametersValues());
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        requiredComponentVisited(methodIfReturn, methodIfReturn.oraCondition);
        if ( !methodIfReturn.getReturnType().equals(BlocklyType.VOID) ) {
            requiredComponentVisited(methodIfReturn, methodIfReturn.oraReturnValue);
        }
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodReturn);
        requiredComponentVisited(methodReturn, methodReturn.getParameters(), methodReturn.body);
        requiredComponentVisited(methodReturn, methodReturn.returnValue);
        for ( Expr param : methodReturn.getParameters().get() ) {
            this.getBuilder(UsedHardwareBean.Builder.class).removeInScopeVariable(((VarDeclaration) param).name);
        }
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt methodStmt) {
        requiredComponentVisited(methodStmt, methodStmt.method);
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUserDefinedMethod(methodVoid);
        requiredComponentVisited(methodVoid, methodVoid.getParameters());
        requiredComponentVisited(methodVoid, methodVoid.body);
        for ( Expr param : methodVoid.getParameters().get() ) {
            this.getBuilder(UsedHardwareBean.Builder.class).removeInScopeVariable(((VarDeclaration) param).name);
        }
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt stmt) {
        return null;
    }

    @Override
    public Void visitNNGetOutputNeuronVal(NNGetOutputNeuronVal get) {
        checkNeuronName(get, false, false, true, get.name);
        return null;
    }

    @Override
    public Void visitNNSetInputNeuronVal(NNSetInputNeuronVal get) {
        checkNeuronName(get, true, false, false, get.name);
        requiredComponentVisited(get, get.value);
        return null;
    }

    @Override
    public Void visitNNSetWeightStmt(NNSetWeightStmt stmt) {
        checkConnectedNeuronNames(stmt, stmt.from, stmt.to);
        requiredComponentVisited(stmt, stmt.value);
        return null;
    }

    @Override
    public Void visitNNSetBiasStmt(NNSetBiasStmt stmt) {
        checkNeuronName(stmt, false, true, true, stmt.name);
        requiredComponentVisited(stmt, stmt.value);
        return null;
    }

    @Override
    public Void visitNNGetWeight(NNGetWeight get) {
        checkConnectedNeuronNames(get, get.from, get.to);
        return null;
    }

    @Override
    public Void visitNNGetBias(NNGetBias get) {
        checkNeuronName(get, false, true, true, get.name);
        return null;
    }

    private int getLevelOfNeuron(String name) {
        if ( nnBean.getInputNeurons().contains(name) ) {
            return 0;
        }
        if ( nnBean.getOutputNeurons().contains(name) ) {
            return nnBean.getNetworkShape().size() + 1;
        }
        if ( name.length() != 4 || name.charAt(0) != 'h' || name.charAt(2) != 'n' ) {
            return -1;
        }
        int level = Integer.parseInt(name.substring(1, 2));
        if ( level >= 1 && level <= nnBean.getNetworkShape().size() ) {
            return level;
        } else {
            return -1;
        }
    }

    private void checkNeuronName(Phrase toBeChecked, boolean inputLegal, boolean hiddenLegal, boolean outputLegal, String name) {
        int level = getLevelOfNeuron(name);
        int outputLevel = nnBean.getNetworkShape().size() + 1;
        if ( level <= -1 || level == 0 && !inputLegal || level == outputLevel && !outputLegal ) {
            addErrorToPhrase(toBeChecked, "NN_INVALID_NEURONNAME");
        }
        if ( level > 1 && level < outputLevel && !hiddenLegal ) {
            addErrorToPhrase(toBeChecked, "NN_INVALID_NEURONNAME");
        }
    }

    private void checkConnectedNeuronNames(Phrase toBeChecked, String n1, String n2) {
        int l1 = getLevelOfNeuron(n1);
        int l2 = getLevelOfNeuron(n2);
        if ( l1 <= -1 || l2 <= -1 || l1 + 1 != l2 ) {
            addErrorToPhrase(toBeChecked, "NN_INVALID_NEURONNAMES");
        }
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        return null;
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        String varName = "";
        if ( repeatStmt.expr.getKind().hasName("EXPR_LIST") ) {
            ExprList exprList = (ExprList) repeatStmt.expr;
            varName = ((Var) exprList.get().get(0)).name;
            this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(varName);
            this.getBuilder(UsedHardwareBean.Builder.class).addInScopeVariable(varName);
        } else if ( repeatStmt.mode == RepeatStmt.Mode.FOR_EACH ) {
            varName = ((VarDeclaration) ((Binary) repeatStmt.expr).left).name;
        }
        requiredComponentVisited(repeatStmt, repeatStmt.expr);

        if ( repeatStmt.mode != RepeatStmt.Mode.WAIT ) {
            increaseLoopCounter();
            requiredComponentVisited(repeatStmt, repeatStmt.list);
            this.currentLoop--;
        } else {
            requiredComponentVisited(repeatStmt, repeatStmt.list);
        }
        if ( !varName.isEmpty() ) {
            this.getBuilder(UsedHardwareBean.Builder.class).removeInScopeVariable(varName);
        }
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        requiredComponentVisited(rgbColor, rgbColor.R, rgbColor.G, rgbColor.B);
        optionalComponentVisited(rgbColor.A);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        boolean isInWaitStmt = this.waitsInLoops.get(this.currentLoop) != 0;
        this.getBuilder(UsedHardwareBean.Builder.class).putLoopLabel(this.currentLoop, isInWaitStmt);
        return null;
    }

    @Override
    public Void visitStmtList(StmtList stmtList) {
        requiredComponentVisited(stmtList, stmtList.get());
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment textComment) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(textCharCastNumberFunct, textCharCastNumberFunct.param);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        requiredComponentVisited(textJoinFunct, textJoinFunct.param);
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        requiredComponentVisited(textPrintFunct, textPrintFunct.param);
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.usedMethodBuilder.addUsedMethod(FunctionNames.CAST);
        requiredComponentVisited(textStringCastNumberFunct, textStringCastNumberFunct.param);
        return null;
    }

    @Override
    public Void visitUnary(Unary phrase) {
        requiredComponentVisited(phrase, phrase.expr);
        return null;
    }

    @Override
    public Void visitVar(Var var) {
        String variableName = var.name;
        if ( !(this.getBuilder(UsedHardwareBean.Builder.class).containsInScopeVariable(variableName)) ) {
            addErrorToPhrase(var, "SCOPE_ERROR"); // key will be overwritten by blockly
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        if ( var.global ) {
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
        // TODO: dangerous check to detect local decls (fct params e.g.). Better don't reuse the blockly block for global vars
        String blocktype = var.getProperty().getBlockType();
        boolean allowedEmptyExprInHiddenDecls = blocktype.equals("robLocalVariables_declare") || blocktype.equals("robControls_forEach");
        if ( !allowedEmptyExprInHiddenDecls ) {
            requiredComponentVisited(var, var.value);
        }
        this.getBuilder(UsedHardwareBean.Builder.class).addGlobalVariable(var.name);
        this.getBuilder(UsedHardwareBean.Builder.class).addDeclaredVariable(var.name);
        this.getBuilder(UsedHardwareBean.Builder.class).addInScopeVariable(var.name);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        if ( this.waitsInLoops.get(this.loopCounter) != null ) {
            increaseWaitStmsInLoop();
            requiredComponentVisited(waitStmt, waitStmt.statements);
            decreaseWaitStmtInLoop();
        } else {
            requiredComponentVisited(waitStmt, waitStmt.statements);
        }
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        requiredComponentVisited(waitTimeStmt, waitTimeStmt.time);
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        requiredComponentVisited(serialWriteAction, serialWriteAction.value);
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
