package de.fhg.iais.roberta.visitor.lang.codegen;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.visitor.SourceBuilder;
import de.fhg.iais.roberta.visitor.BaseVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class AbstractLanguageVisitor extends BaseVisitor<Void> implements ILanguageVisitor<Void> {
    //TODO find more simple way of handling the loops
    private int loopCounter = 0;
    protected LinkedList<Integer> currentLoop = new LinkedList<>();

    protected final List<Phrase> programPhrases;

    protected StringBuilder sb;
    protected SourceBuilder src;

    private final ClassToInstanceMap<IProjectBean> beans;

    /**
     * initialize the common language code generator visitor.
     */
    protected AbstractLanguageVisitor(List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        Assert.isTrue(!programPhrases.isEmpty());
        this.beans = beans;

        this.programPhrases =
            programPhrases
                .stream()
                .flatMap(e -> e.subList(1, e.size()).stream())
                .filter(p -> p.getProperty().isInTask() == null || p.getProperty().isInTask() && !p.getProperty().isDisabled()) //TODO check if we can avoid null value for inTask
                .collect(Collectors.toList());
    }

    protected <T extends IProjectBean> T getBean(Class<T> clazz) {
        T bean = this.beans.getInstance(clazz);
        Assert.notNull(bean, "This bean does not exist!");
        return bean;
    }

    public void setStringBuilders(StringBuilder sourceCode, StringBuilder indentation) {
        this.sb = sourceCode;
        this.src = new SourceBuilder(this.sb);
    }

    public void generateCode(boolean withWrapping) {
        generateProgramPrefix(withWrapping);
        generateProgramMainBody();
        generateProgramSuffix(withWrapping);
    }

    private void generateProgramMainBody() {
        this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() != Category.METHOD || phrase.getKind().hasName("METHOD_CALL"))
            .forEach(p -> {
                nlIndent();
                p.accept(this);
            });
    }

    protected void generateUserDefinedMethods() {
        this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .forEach(e -> {
                e.accept(this);
                nlIndent();
            });
    }

    /**
     * generate code for the xNN neural networks in one of the 3 target languages "java" "python" "c++"
     *
     * @param targetLanguage
     */
    protected final void generateNNStuff(String targetLanguage) {
        NNBean nnBean = this.getBean(CodeGeneratorSetupBean.class).getNNBean();
        if ( nnBean != null && nnBean.hasAtLeastOneInputAndOutputNeuron() ) {
            for ( String neuron : nnBean.getInputNeurons() ) {
                mkDecl(targetLanguage, neuron);
            }
            for ( String neuron : nnBean.getOutputNeurons() ) {
                mkDecl(targetLanguage, neuron);
            }
            final JSONArray weights = nnBean.getWeights();
            final JSONArray biases = nnBean.getBiases();
            for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
                JSONArray weightsForLayer = weights.getJSONArray(layer);
                JSONArray biasesForLayer = biases.getJSONArray(layer + 1);
                int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
                for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                    String targetName = (layer == weights.length() - 2) ? nnBean.getOutputNeurons().get(targetNum) : ("h" + (layer + 1) + "n" + (targetNum + 1));
                    mkDeclWithAssign(targetLanguage, "b_" + targetName);
                    mkWeightTerm(targetLanguage, biasesForLayer.getString(targetNum));
                    for ( int sourceNum = 0; sourceNum < weightsForLayer.length(); sourceNum++ ) {
                        String sourceName = (layer == 0) ? nnBean.getInputNeurons().get(sourceNum) : ("h" + layer + "n" + (sourceNum + 1));
                        mkDeclWithAssign(targetLanguage, "w_" + sourceName + "_" + targetName);
                        mkWeightTerm(targetLanguage, weightsForLayer.getJSONArray(sourceNum).getString(targetNum));
                    }
                }
            }
            mkNnStepStart(targetLanguage);
            for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
                final JSONArray weightsForLayer = weights.getJSONArray(layer);
                final JSONArray biasesForLayer = biases.getJSONArray(layer + 1);
                int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
                for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                    String targetName;
                    boolean optDeclNeeded;
                    if ( layer == weights.length() - 2 ) {
                        // output
                        targetName = nnBean.getOutputNeurons().get(targetNum);
                        optDeclNeeded = false;
                    } else {
                        // hidden
                        targetName = "h" + (layer + 1) + "n" + (targetNum + 1);
                        optDeclNeeded = true;
                    }
                    mkStepAssign(targetLanguage, optDeclNeeded, targetName);
                    for ( int sourceNum = 0; sourceNum < weightsForLayer.length(); sourceNum++ ) {
                        String sourceName;
                        String sourcePrefix;
                        if ( layer == 0 ) {
                            // input
                            sourceName = nnBean.getInputNeurons().get(sourceNum);
                        } else {
                            // hidden
                            sourceName = "h" + layer + "n" + (sourceNum + 1);
                        }
                        src.add(" + ____", sourceName, " * ____w_", sourceName, "_", targetName);
                    }
                    mkStmtTerminator(targetLanguage);
                }
            }
            mkNnStepEnd(targetLanguage);
        }
    }

    private void mkStmtTerminator(String targetLanguage) {
        if ( targetLanguage.equals("java") ) {
            src.add(";");
        } else if ( targetLanguage.equals("python") ) {
            // no terminator at all
        } else if ( targetLanguage.equals("c++") ) {
            src.add(";");
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkStepAssign(String targetLanguage, boolean optDeclNeeded, String targetName) {
        if ( targetLanguage.equals("java") ) {
            src.nlI().add(optDeclNeeded ? "float " : "", "____", targetName, " = ____b_", targetName);
        } else if ( targetLanguage.equals("python") ) {
            if ( !optDeclNeeded ) {
                // global variable. Declare that
                src.nlI().add("global ____").add(targetName);
            }
            src.nlI().add("____", targetName, " = ____b_", targetName);
        } else if ( targetLanguage.equals("c++") ) {
            src.nlI().add(optDeclNeeded ? "double " : "", "____", targetName, " = ____b_", targetName);
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkNnStepStart(String targetLanguage) {
        src.nlI().nlI();
        if ( targetLanguage.equals("java") ) {
            src.add("private void ____nnStep() {");
        } else if ( targetLanguage.equals("python") ) {
            src.nlI().add("def ____nnStep():");
        } else if ( targetLanguage.equals("c++") ) {
            src.nlI().add("void ____nnStep() {");
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
        src.INCR();
    }

    private void mkNnStepEnd(String targetLanguage) {
        src.DECR().nlI();
        if ( targetLanguage.equals("java") ) {
            src.add("}").nlI();
        } else if ( targetLanguage.equals("python") ) {
            // no terminator at all
        } else if ( targetLanguage.equals("c++") ) {
            src.add("}").nlI();
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }


    private void mkDecl(String targetLanguage, String neuron) {
        if ( targetLanguage.equals("java") ) {
            src.nlI().add("private float ____", neuron, ";");
        } else if ( targetLanguage.equals("python") ) {
            src.nlI().add("____", neuron, " = 0");
        } else if ( targetLanguage.equals("c++") ) {
            src.nlI().add("double ____", neuron, ";");
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkDeclWithAssign(String targetLanguage, String neuron) {
        if ( targetLanguage.equals("java") ) {
            src.nlI().add("private float ____", neuron, " = ");
        } else if ( targetLanguage.equals("python") ) {
            src.nlI().add("____", neuron, " = ");
        } else if ( targetLanguage.equals("c++") ) {
            src.nlI().add("double ____", neuron, " = ");
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkWeightTerm(String targetLanguage, String weight) {
        weight = weight.replaceAll(",", ".");
        char firstChar = weight.charAt(0);
        if ( firstChar == '*' ) {
            src.add(weight.substring(1));
        } else if ( firstChar == '/' || firstChar == ':' ) {
            src.add("1/").add(weight.substring(1));
        } else {
            src.add(weight);
        }
        if ( targetLanguage.equals("java") ) {
            src.add("f;"); // otherwise the init value is double. Possible conversion loss #1396
        } else if ( targetLanguage.equals("python") ) {
            // no terminator at all
        } else if ( targetLanguage.equals("c++") ) {
            src.add(";");
        } else {
            Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        this.sb.append(numConst.value);
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        this.sb.append(boolConst.value);
        return null;
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        src.add("\"", StringEscapeUtils.escapeEcmaScript(stringConst.value.replaceAll("[<>\\$]", "")), "\"");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        rgbColor.R.accept(this);
        this.sb.append(", ");
        rgbColor.G.accept(this);
        this.sb.append(", ");
        rgbColor.B.accept(this);
        this.sb.append(", ");
        rgbColor.A.accept(this);
        return null;
    }

    @Override
    public Void visitVar(Var var) {
        this.sb.append(var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        src.add(getLanguageVarTypeFromBlocklyType(var.typeVar), " ", var.getCodeSafeName());
        if ( !var.value.getKind().hasName("EMPTY_EXPR") ) {
            src.add(" = ");
            if ( var.value.getKind().hasName("EXPR_LIST") ) {
                ExprList list = (ExprList) var.value;
                if ( list.get().size() == 2 ) {
                    list.get().get(1).accept(this);
                } else {
                    list.get().get(0).accept(this);
                }
            } else {
                var.value.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment stmtTextComment) {
        src.add("// ", stmtTextComment.textComment.replace("\n", " "));
        return null;
    }

    @Override
    public Void visitUnary(Unary unary) {
        Unary.Op op = unary.op;
        String sym = getUnaryOperatorSymbol(op);
        if ( op == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            src.add(sym);
        } else {
            src.add(sym, whitespace());
            generateExprCode(unary, this.sb);
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList exprList) {
        boolean first = true;
        for ( Expr expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                first = src.addIf(first, ", ");
                expr.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt actionStmt) {
        actionStmt.action.accept(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt assignStmt) {
        assignStmt.name.accept(this);
        src.add(" = ");
        assignStmt.expr.accept(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt ifStmt) {
        generateCodeFromIfElse(ifStmt);
        generateCodeFromElse(ifStmt);
        return null;
    }

    @Override
    public Void visitTernaryExpr(TernaryExpr ternaryExpr) {
        generateCodeFromTernary(ternaryExpr);
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt nnStepStmt) {
        this.src.add("____nnStep();");
        return null;
    }

    @Override
    public Void visitNNSetInputNeuronVal(NNSetInputNeuronVal setVal) {
        src.add("____").add(setVal.name).add(" = ");
        setVal.value.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitNNGetOutputNeuronVal(NNGetOutputNeuronVal getVal) {
        src.add("____").add(getVal.name);
        return null;
    }

    @Override
    public Void visitNNSetWeightStmt(NNSetWeightStmt chgStmt) {
        this.src.add("____w_", chgStmt.from, "_", chgStmt.to, " = ");
        chgStmt.value.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitNNSetBiasStmt(NNSetBiasStmt chgStmt) {
        this.src.add("____b_", chgStmt.name, " = ");
        chgStmt.value.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitNNGetWeight(NNGetWeight getVal) {
        src.add("____w_", getVal.from, "_", getVal.to);
        return null;
    }

    @Override
    public Void visitNNGetBias(NNGetBias getVal) {
        src.add("____b_", getVal.name);
        return null;
    }

    @Override
    public Void visitStmtList(StmtList stmtList) {
        stmtList.get().stream().forEach(stmt -> {
            nlIndent();
            stmt.accept(this);
        });
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        src.add(methodCall.getCodeSafeMethodName(), "(");
        methodCall.getParametersValues().accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt methodStmt) {
        methodStmt.method.accept(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        mathPowerFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    protected void generateExprCode(Unary unary, StringBuilder sb) {
        if ( unary.expr.getPrecedence() < unary.getPrecedence() || unary.op == Unary.Op.NEG ) {
            sb.append("(");
            unary.expr.accept(this);
            sb.append(")");
        } else {
            unary.expr.accept(this);
        }
    }

    protected void incrIndentation() {
        src.INCR();
    }

    protected void decrIndentation() {
        src.DECR();
    }

    protected void indent() {
        src.indent();
    }

    public void nlIndent() {
        src.nlI();
    }

    protected boolean isInteger(String str) {
        try {
            Integer.parseInt(str); //NOSONAR : it is checked if the string is a parseable Integer. Result is NOT used.
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    protected void increaseLoopCounter() {
        this.loopCounter++;
        this.currentLoop.add(this.loopCounter);
    }

    protected String whitespace() {
        return " ";
    }

    protected String getEnumCode(String value) {
        return value.toLowerCase();
    }

    protected String getEnumCode(IMode value) {
        return value.getClass().getSimpleName() + "." + value;
    }

    protected boolean isMainBlock(Phrase phrase) {
        return phrase.hasName("MAIN_TASK");
    }

    protected boolean parenthesesCheck(Binary binary) {
        return binary.op == Op.MINUS && binary.getRight().getKind().hasName("BINARY") && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    protected void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr expr, Binary binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && !expr.getKind().hasName("BINARY") ) {
            // parentheses are omitted
            expr.accept(this);
        } else {
            sb.append("(" + whitespace());
            expr.accept(this);
            sb.append(whitespace() + ")");
        }
    }

    abstract protected String getLanguageVarTypeFromBlocklyType(BlocklyType type);

    abstract protected void generateCodeFromTernary(TernaryExpr ternaryExpr);

    abstract protected void generateCodeFromIfElse(IfStmt ifStmt);

    abstract protected void generateCodeFromElse(IfStmt ifStmt);

    abstract protected void generateProgramPrefix(boolean withWrapping);

    abstract protected void generateProgramSuffix(boolean withWrapping);

    abstract protected String getBinaryOperatorSymbol(Binary.Op op);

    abstract protected String getUnaryOperatorSymbol(Unary.Op op);

    protected static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry(key, value);
    }

    protected static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
        return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
    }

    protected static <K, U> Collector<Map.Entry<K, U>, ?, ConcurrentMap<K, U>> entriesToConcurrentMap() {
        return Collectors.toConcurrentMap((e) -> e.getKey(), (e) -> e.getValue());
    }
}
