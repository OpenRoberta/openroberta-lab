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
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathModuloFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.visitor.SourceBuilder;
import de.fhg.iais.roberta.visitor.BaseVisitor;

public abstract class AbstractLanguageVisitor extends BaseVisitor<Void> {
    //TODO find more simple way of handling the loops
    private int loopCounter = 0;
    protected LinkedList<Integer> currentLoop = new LinkedList<>();

    protected final List<Phrase> programPhrases;
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
                .filter(p -> {
                    BlocklyProperties blocklyProperties2 = p.getProperty();
                    if ( blocklyProperties2.blocklyRegion.inTask == null ) return true;
                    BlocklyProperties blocklyProperties1 = p.getProperty();
                    if ( !blocklyProperties1.blocklyRegion.inTask ) return false;
                    BlocklyProperties blocklyProperties = p.getProperty();
                    return !blocklyProperties.blocklyRegion.disabled;
                }) //TODO check if we can avoid null value for inTask
                .collect(Collectors.toList());
    }

    protected <T extends IProjectBean> T getBean(Class<T> clazz) {
        T bean = this.beans.getInstance(clazz);
        Assert.notNull(bean, "This bean does not exist!");
        return bean;
    }

    public void setStringBuilders(StringBuilder sourceCode, StringBuilder indentation) {
        this.src = new SourceBuilder(sourceCode);
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
        if ( this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .count() > 0 ) {
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .forEach(e -> {
                    e.accept(this);
                    nlIndent();
                });
        }
    }

    /**
     * generate code for the xNN neural networks in one of the 4 target languages "java" "python" "c++" "nxc"
     * only generate code IF NN blocks are used
     *
     * @param targetLanguage
     */
    protected final void generateNNStuff(String targetLanguage) {
        if ( this.getBean(UsedHardwareBean.class).isNNBlockUsed() ) {
            NNBean nnBean = this.getBean(CodeGeneratorSetupBean.class).getNNBean();
            if ( nnBean != null && nnBean.hasAtLeastOneInputAndOutputNeuron() ) {
                generateNNVariables(targetLanguage);
                generateNNStepFunction(targetLanguage);
            }
        }
    }

    protected final void generateNNVariables(String targetLanguage) {
        NNBean nnBean = this.getBean(CodeGeneratorSetupBean.class).getNNBean();
        final JSONArray weights = nnBean.getWeights();
        final JSONArray biases = nnBean.getBiases();

        for ( String neuron : nnBean.getInputNeurons() ) {
            mkDecl(targetLanguage, neuron);
            addForGlobalDeclIfPython(targetLanguage, neuron);
        }
        for ( String neuron : nnBean.getAllHiddenNeurons() ) {
            mkDecl(targetLanguage, neuron);
            addForGlobalDeclIfPython(targetLanguage, neuron);
        }
        for ( String neuron : nnBean.getOutputNeurons() ) {
            mkDecl(targetLanguage, neuron);
        }
        for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
            JSONArray weightsForLayer = weights.getJSONArray(layer);
            JSONArray biasesForLayer = biases.getJSONArray(layer + 1);
            int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
            for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                String targetName = (layer == weights.length() - 2) ? nnBean.getOutputNeurons().get(targetNum) : nnBean.getHiddenNeuronsByLayer(layer).get(targetNum);
                mkDeclWithAssign(targetLanguage, "b_" + targetName);
                mkWeightTerm(targetLanguage, biasesForLayer.getString(targetNum));
                addForGlobalDeclIfPython(targetLanguage, "b_" + targetName);
                for ( int sourceNum = 0; sourceNum < weightsForLayer.length(); sourceNum++ ) {
                    String sourceName = (layer == 0) ? nnBean.getInputNeurons().get(sourceNum) : nnBean.getHiddenNeuronsByLayer(layer - 1).get(sourceNum);
                    mkDeclWithAssign(targetLanguage, "w_" + sourceName + "_" + targetName);
                    mkWeightTerm(targetLanguage, weightsForLayer.getJSONArray(sourceNum).getString(targetNum));
                    addForGlobalDeclIfPython(targetLanguage, "w_" + sourceName + "_" + targetName);
                }
            }
        }
    }

    protected void addForGlobalDeclIfPython(String targetLanguage, String neuron) {
        // do nothing. Only the AbstractPythonVisitor should verwrite this method!
    }

    protected final void generateNNStepFunction(String targetLanguage) {
        NNBean nnBean = this.getBean(CodeGeneratorSetupBean.class).getNNBean();
        final JSONArray weights = nnBean.getWeights();
        final String activationKey = nnBean.getActivationKey();

        mkNnStepStart(targetLanguage);
        if ( targetLanguage.equals("python") ) {
            boolean globalWritten = false;
            boolean secondNeuronFound = false;
            for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
                final JSONArray weightsForLayer = weights.getJSONArray(layer);
                int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
                for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                    String targetName = (layer == weights.length() - 2) ? nnBean.getOutputNeurons().get(targetNum) : nnBean.getHiddenNeuronsByLayer(layer).get(targetNum);
                    if ( !globalWritten ) {
                        globalWritten = true;
                        src.nlI().add("global ");
                    }
                    if ( secondNeuronFound ) {
                        src.add(", ");
                    }
                    secondNeuronFound = true;
                    src.add("____").add(targetName);
                }
            }
        }
        for ( int layer = 0; layer < weights.length() - 1; layer++ ) {
            final JSONArray weightsForLayer = weights.getJSONArray(layer);
            int numberOfNeurons = weightsForLayer.getJSONArray(0).length();
            for ( int targetNum = 0; targetNum < numberOfNeurons; targetNum++ ) {
                String targetName = (layer == weights.length() - 2) ? nnBean.getOutputNeurons().get(targetNum) : nnBean.getHiddenNeuronsByLayer(layer).get(targetNum);
                src.nlI().add("____", targetName, " = ____b_", targetName);
                for ( int sourceNum = 0; sourceNum < weightsForLayer.length(); sourceNum++ ) {
                    String sourceName = (layer == 0) ? nnBean.getInputNeurons().get(sourceNum) : nnBean.getHiddenNeuronsByLayer(layer - 1).get(sourceNum);
                    src.add(" + ____", sourceName, " * ____w_", sourceName, "_", targetName);
                }
                mkStmtTerminator(targetLanguage);
                mkActivationFunctionTerm(targetLanguage, targetName, activationKey);
            }
        }
        mkNnStepEnd(targetLanguage);
    }

    private void mkStmtTerminator(String targetLanguage) {
        switch ( targetLanguage ) {
            case "java":
            case "c++":
            case "nxc":
                src.add(";");
                break;
            case "python":
            case "aseba":
                // no terminator at all
                break;
            default:
                Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkActivationFunctionTerm(String targetLanguage, String targetName, String activationKey) {
        switch ( activationKey ) {
            case "linear":
                break;
            case "relu":
                switch ( targetLanguage ) {
                    case "java":
                        src.nlI().add("____", targetName, " = (float) Math.max(0, ____", targetName, ");");
                        break;
                    case "c++":
                        src.nlI().add("____", targetName, " = fmax(0.0, ____", targetName, ");");
                        break;
                    case "nxc":
                        src.nlI().add("____", targetName, " = MAX(0.0, ____", targetName, ");");
                        break;
                    case "python":
                        src.nlI().add("____", targetName, " = max(0, ____", targetName, ")");
                        break;
                    case "aseba":
                        src.nlI().add("call math.max(____", targetName, ", 0, ____", targetName, ")");
                        break;
                    default:
                        Assert.fail("invalid target language for xNN: " + targetLanguage);
                }
                break;
            case "tanh":
                switch ( targetLanguage ) {
                    case "java":
                        src.nlI().add("____", targetName, " = (float) Math.tanh(____", targetName, ");");
                        break;
                    case "c++":
                        src.nlI().add("____", targetName, " = tanh(____", targetName, ");");
                        break;
                    case "python":
                        src.nlI().add("____", targetName, " = math.tanh(____", targetName, ")");
                        break;
                    default:
                        Assert.fail("invalid target language for xNN: " + targetLanguage);
                }
                break;
            case "sigmoid":
                switch ( targetLanguage ) {
                    case "java":
                        src.nlI().add("____", targetName, " = (float) 1 / (1 + (float) Math.exp(-____", targetName, "));");
                        break;
                    case "c++":
                        src.nlI().add("____", targetName, " = 1 / (1 + exp(-____", targetName, "));");
                        break;
                    case "python":
                        src.nlI().add("____", targetName, " = 1 / (1 + math.exp(-____", targetName, "))");
                        break;
                    default:
                        Assert.fail("invalid target language for xNN: " + targetLanguage);
                }
                break;
            case "bool":
                switch ( targetLanguage ) {
                    case "java":
                    case "c++":
                    case "nxc":
                    case "python":
                        src.nlI().add("____", targetName, " = ____", targetName, " < 1 ? 0 : 1");
                        mkStmtTerminator(targetLanguage);
                        break;
                    default:
                        Assert.fail("invalid target language for xNN: " + targetLanguage);
                }
                break;
            default:
                throw new DbcException("Invalid activation key encountered for NN: " + activationKey);
        }
    }

    private void mkNnStepStart(String targetLanguage) {
        src.nlI().nlI();
        switch ( targetLanguage ) {
            case "java":
                src.add("private void ____nnStep() {");
                break;
            case "c++":
            case "nxc":
                src.nlI().add("void ____nnStep() {");
                break;
            case "python":
                src.nlI().add("def ____nnStep():");
                break;
            case "aseba":
                src.nlI().add("sub ____nnStep");
                break;
            default:
                Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
        src.INCR();
    }

    private void mkNnStepEnd(String targetLanguage) {
        src.DECR().nlI();
        switch ( targetLanguage ) {
            case "java":
            case "c++":
            case "nxc":
                src.add("}").nlI();
                break;
            case "python":
            case "aseba":
                // no terminator at all
                break;
            default:
                Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }


    private void mkDecl(String targetLanguage, String neuron) {
        switch ( targetLanguage ) {
            case "java":
                src.nlI().add("private float ____", neuron, ";");
                break;
            case "c++":
                src.nlI().add("double ____", neuron, ";");
                break;
            case "nxc":
                src.nlI().add("float ____", neuron, ";");
                break;
            case "python":
                src.nlI().add("____", neuron, " = 0");
                break;
            case "aseba":
                src.nlI().add("var ____", neuron, " = 0");
                break;
            default:
                Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    private void mkDeclWithAssign(String targetLanguage, String neuron) {
        switch ( targetLanguage ) {
            case "java":
                src.nlI().add("private float ____", neuron, " = ");
                break;
            case "c++":
                src.nlI().add("double ____", neuron, " = ");
                break;
            case "nxc":
                src.nlI().add("float ____", neuron, " = ");
                break;
            case "python":
                src.nlI().add("____", neuron, " = ");
                break;
            case "aseba":
                src.nlI().add("var ____", neuron, " = ");
                break;
            default:
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
        switch ( targetLanguage ) {
            case "java":
                src.add("f;"); // otherwise the init value is double. Possible conversion loss #1396
                break;
            case "c++":
            case "nxc":
                src.add(";");
                break;
            case "python":
            case "aseba":
                // no terminator at all
                break;
            default:
                Assert.fail("invalid target language for xNN: " + targetLanguage);
        }
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        this.src.add(numConst.value);
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        this.src.add(boolConst.value);
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
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(", ");
        rgbColor.A.accept(this);
        return null;
    }

    @Override
    public Void visitVar(Var var) {
        this.src.add(var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        src.add(getLanguageVarTypeFromBlocklyType(var.getBlocklyType()), " ", var.getCodeSafeName());
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
            generateExprCode(unary, this.src);
            src.add(sym);
        } else {
            src.add(sym, " ");
            generateExprCode(unary, this.src);
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
        this.src.add(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        mathChangeStmt.var.accept(this);
        this.src.add(" += ");
        mathChangeStmt.delta.accept(this);
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        this.src.add("( ( ");
        mathModuloFunct.dividend.accept(this);
        this.src.add(" ) % ( ");
        mathModuloFunct.divisor.accept(this);
        this.src.add(" ) )");
        return null;
    }

    protected void generateExprCode(Unary unary, SourceBuilder src) {
        if ( unary.expr.getPrecedence() < unary.getPrecedence() || unary.op == Unary.Op.NEG ) {
            src.add("(");
            unary.expr.accept(this);
            src.add(")");
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

    protected void generateSubExpr(SourceBuilder src, boolean minusAdaption, Expr expr, Binary binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && !expr.getKind().hasName("BINARY") ) {
            // parentheses are omitted
            expr.accept(this);
        } else {
            src.add("( ");
            expr.accept(this);
            src.add(" )");
        }
    }

    abstract protected String getLanguageVarTypeFromBlocklyType(BlocklyType type);

    abstract protected void generateCodeFromTernary(TernaryExpr ternaryExpr);

    abstract protected void generateCodeFromIfElse(IfStmt ifStmt);

    abstract protected void generateCodeFromElse(IfStmt ifStmt);



    /**
     * How to rework Visitors:
     * generateProgramPrefix will do the ordering for when to generate imports, nn, helper methods, etc.
     * this function may stay abstract only if all vistors generation order differs to much, JAVA and CPP Code can probably be generated pretty similarly
     * so there is potetntial for turning this into a non abstract function
     * <p>
     * all new functions introduced to solve this usse are prefiex with visitor<Name> this will solve naming conflicts
     * e.g. EV3C4ev3... ussed generateImport although this visitor should not yet be changed
     *
     * <p>
     * The Abstract CPPVisitor has some more information on this
     */
    protected abstract void generateProgramPrefix(boolean withWrapping);

    /**
     * just the nn-function call all this once in the abstract visitors for a given langugage
     */
    abstract protected void visitorGenerateNN();

    abstract protected void visitorGenerateImports();

    /**
     * these are global and constant variables not user generated globals (user generated globals will be added in mainTask @_generateUserVariables)
     */
    abstract protected void visitorGenerateGlobalVariables();

    /**
     * this function should not be changed for a consitant behavour, this  may lead to minor changes in visitors
     * right now most visitors to this in the suffix, but some in the prefix -- make sure to remove helper function logic from visitor or methods will be
     * generated more than once
     */
    protected void visitorGenerateHelperMethods() {
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this
                    .getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.src.add(helperMethodImpls).nlI();
        }
    }

    /**
     * this will be called from inside MaiNTask since we need the variable list, right now a lot of visitors to bascially this with some varaition
     * please unify this to one function
     */
    protected void visitorGenerateUserVariablesAndMethods(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        if ( !variables.get().isEmpty() ) {
            variables.accept(this);
            nlIndent();
        }
        generateUserDefinedMethods();
    }

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
