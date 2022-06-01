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
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNChangeBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNChangeWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNInputNeuronStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNOutputNeuronStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNOutputNeuronWoVarStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.NNStepDecl;
import de.fhg.iais.roberta.util.visitor.SourceBuilder;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.BaseVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

public abstract class AbstractLanguageVisitor extends BaseVisitor<Void> implements ILanguageVisitor<Void> {
    //TODO find more simple way of handling the loops
    private int loopCounter = 0;
    protected LinkedList<Integer> currentLoop = new LinkedList<>();

    protected final List<Phrase<Void>> programPhrases;

    protected StringBuilder sb;
    protected SourceBuilder src;

    private final ClassToInstanceMap<IProjectBean> beans;

    /**
     * initialize the common language code generator visitor.
     */
    protected AbstractLanguageVisitor(List<List<Phrase<Void>>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
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

    protected void generateNNStuff() {
        NNStepDecl nnStepDecl = this.getBean(CodeGeneratorSetupBean.class).getNNStepDecl();
        if ( nnStepDecl != null ) {
            src.add("private float ____");
            src.collect(nnStepDecl.getOutputNeurons(), ", ____").add(";").nlI();
            src.add("private void ____nnStep(").collect(nnStepDecl.getInputNeurons(), " float _", ", float _", "").add(") {").INCR();
            JSONArray weights = nnStepDecl.getWeights();
            JSONArray biases = nnStepDecl.getBiases();
            for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
                JSONArray weightsForLayer = weights.getJSONArray(layer);
                JSONArray biasesForLayer = biases.getJSONArray(layer + 1);
                int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
                for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                    String targetNeuron = (layer == weights.length() - 2) ? ("____" + nnStepDecl.getOutputNeurons().get(targetNum)) : "float h" + (layer + 1) + "n" + (targetNum + 1);
                    src.nlI().add(targetNeuron, " = ", biasesForLayer.getString(targetNum));
                    for ( int sourceNum = 0; sourceNum < weightsForLayer.length(); sourceNum++ ) {
                        String sourceNeuron = (layer == 0) ? ("_" + nnStepDecl.getInputNeurons().get(sourceNum)) : ("h" + layer + "n" + (sourceNum + 1));
                        src.add(" + ", sourceNeuron);
                        writeWeightTerm(weightsForLayer.getJSONArray(sourceNum).getString(targetNum));
                    }
                    src.add(";");
                }
            }
            src.DECR().nlI().add("}").nlI();
        }
    }

    private void writeWeightTerm(String weight) {
        char firstChar = weight.charAt(0);
        if ( firstChar == '*' ) {
            src.add(weight);
        } else if ( firstChar == '/' || firstChar == ':' ) {
            src.add("/").add(weight.substring(1));
        } else {
            src.add("*").add(weight);
        }
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        this.sb.append(numConst.getValue());
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.getValue());
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        src.add("\"", StringEscapeUtils.escapeEcmaScript(stringConst.getValue().replaceAll("[<>\\$]", "")), "\"");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        throw new UnsupportedOperationException("should be overriden in a robot-specific class");
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().accept(this);
        this.sb.append(", ");
        rgbColor.getG().accept(this);
        this.sb.append(", ");
        rgbColor.getB().accept(this);
        this.sb.append(", ");
        rgbColor.getA().accept(this);
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        this.sb.append(var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        src.add(getLanguageVarTypeFromBlocklyType(var.getTypeVar()), " ", var.getCodeSafeName());
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            src.add(" = ");
            if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).accept(this);
                } else {
                    list.get().get(0).accept(this);
                }
            } else {
                var.getValue().accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        src.add("// ", stmtTextComment.getTextComment().replace("\n", " "));
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        Unary.Op op = unary.getOp();
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
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                first = src.addIf(first, ", ");
                expr.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().accept(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getName().accept(this);
        src.add(" = ");
        assignStmt.getExpr().accept(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        generateCodeFromIfElse(ifStmt);
        generateCodeFromElse(ifStmt);
        return null;
    }

    @Override
    public Void visitTernaryExpr(TernaryExpr<Void> ternaryExpr) {
        generateCodeFromTernary(ternaryExpr);
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt<Void> nnStepStmt) {
        this.src.add("____nnStep(");
        boolean first = true;
        for ( Stmt stmt : nnStepStmt.getInputNeurons() ) {
            first = src.addIf(first, ",");
            stmt.accept(this);
        }
        this.src.add(");").nlI();
        for ( Stmt stmt : nnStepStmt.getOutputNeurons() ) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public Void visitNNChangeWeightStmt(NNChangeWeightStmt<Void> chgStmt) {
        return null;
    }

    @Override
    public Void visitNNChangeBiasStmt(NNChangeBiasStmt<Void> chgStmt) {
        return null;
    }

    @Override
    public Void visitNNGetOutputNeuronVal(NNGetOutputNeuronVal<Void> getVal) {
        src.add("____").add(getVal.getName());
        return null;
    }

    @Override
    public Void visitNNInputNeuronStmt(NNInputNeuronStmt<Void> nnInputNeuronStmt) {
        nnInputNeuronStmt.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitNNOutputNeuronStmt(NNOutputNeuronStmt<Void> nnOutputNeuronStmt) {
        nnOutputNeuronStmt.getValue().accept(this);
        src.add(" = ____", nnOutputNeuronStmt.getName(), ";").nlI();
        return null;
    }

    @Override
    public Void visitNNOutputNeuronWoVarStmt(NNOutputNeuronWoVarStmt<Void> nnOutputNeuronWoVarStmt) {
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        stmtList.get().stream().forEach(stmt -> {
            nlIndent();
            stmt.accept(this);
        });
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        src.add(methodCall.getMethodName(), "(");
        methodCall.getParametersValues().accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().accept(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        mathPowerFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    protected void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() || unary.getOp() == Unary.Op.NEG ) {
            sb.append("(");
            unary.getExpr().accept(this);
            sb.append(")");
        } else {
            unary.getExpr().accept(this);
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

    protected boolean isMainBlock(Phrase<Void> phrase) {
        return phrase.getKind().getName().equals("MAIN_TASK");
    }

    protected boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind().hasName("BINARY") && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    protected void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
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

    abstract protected void generateCodeFromTernary(TernaryExpr<Void> ternaryExpr);

    abstract protected void generateCodeFromIfElse(IfStmt<Void> ifStmt);

    abstract protected void generateCodeFromElse(IfStmt<Void> ifStmt);

    abstract protected void generateProgramPrefix(boolean withWrapping);

    abstract protected void generateProgramSuffix(boolean withWrapping);

    abstract protected String getBinaryOperatorSymbol(Binary.Op op);

    abstract protected String getUnaryOperatorSymbol(Unary.Op op);

    protected static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    protected static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
        return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
    }

    protected static <K, U> Collector<Map.Entry<K, U>, ?, ConcurrentMap<K, U>> entriesToConcurrentMap() {
        return Collectors.toConcurrentMap((e) -> e.getKey(), (e) -> e.getValue());
    }
}
