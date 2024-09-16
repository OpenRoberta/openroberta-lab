package de.fhg.iais.roberta.visitor.lang.codegen.prog;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
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
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathModuloFunct;
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
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.visitor.SourceBuilder;
import de.fhg.iais.roberta.visitor.BaseVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they create a textual representation of NEPO programs
 * as an alternative to the blockly representation. This class is to be subclasses in all robot plugins that support the
 * textual representation as textlyJava
 */
public abstract class AbstractRegenerateTextlyJavaVisitor extends BaseVisitor<Void> {
    protected final List<Phrase> programPhrases;
    protected SourceBuilder src;

    /**
     * initialize the Java textly code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    protected AbstractRegenerateTextlyJavaVisitor(List<List<Phrase>> programPhrases) {
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

    public void setStringBuilders(StringBuilder sourceCode, StringBuilder indentation) {
        this.src = new SourceBuilder(sourceCode);
    }

    public void generateCode(boolean withWrapping) {
        this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() != Category.METHOD || phrase.getKind().hasName("METHOD_CALL"))
            .forEach(p -> {
                p.accept(this);
            });
        this.src.DECR().nlI().add("}");
        this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .forEach(p -> {
                p.accept(this);
            });
        this.src.DECR().nlI().add("}");
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        this.src.add(numConst.value);
        return null;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        this.src.add("null");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        switch ( mathConst.mathConst ) {
            case PI:
                this.src.add("pi");
                break;
            case E:
                this.src.add("e");
                break;
            case GOLDEN_RATIO:
                this.src.add("phi");
                break;
            case SQRT2:
                this.src.add("sqrt2");
                break;
            case SQRT1_2:
                this.src.add("sqrt_1_2");
                break;
            case INFINITY:
                this.src.add("inf");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        generateSubExpr(this.src, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(" ", sym, " ");
        generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        this.src.add(boolConst.value);
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("--- COLOR ---");
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.src.add("\"\"");
                break;
            case BOOLEAN:
                this.src.add("true");
                break;
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            default:
                this.src.add("null");
                break;
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt assignStmt) {
        this.src.nlI();
        assignStmt.name.accept(this);
        src.add(" = ");
        assignStmt.expr.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt exprStmt) {
        super.visitExprStmt(exprStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt rept) {
        switch ( rept.mode ) {
            case WHILE:
                generateCodeFromStmtCondition("while", rept.expr, rept.list);
                break;
            case FOREVER:
                this.src.nlI().add("while ( true ) {").INCR().accept(rept.list, this).DECR().nlI().add("};");
                break;
            case UNTIL:
                this.src.nlI().add("{").INCR().accept(rept.list, this).DECR().nlI().add("} until ( ").accept(rept.expr, this).add(" );");
                break;
            case TIMES:
                Expr timesClause = ((ExprList) rept.expr).get().get(2);
                this.src.nlI().add("for ( ").accept(timesClause, this).add(" times ) {").INCR().accept(rept.list, this).DECR().nlI().add("};");
                break;
            case FOR:
                generateCodeFromStmtConditionFor(rept.expr, rept.list);
                break;
            case FOR_EACH:
                Binary eachDecl = (Binary) rept.expr;
                this.src.nlI().add("for ( ");
                genParameter((VarDeclaration) eachDecl.left);
                this.src.add(" : ").accept(eachDecl.right,this).add(" )").add(" {").INCR().accept(rept.list, this).DECR().nlI().add("};");
                break;
            default:
                throw new DbcException("invalid mode of repeat statement");
        }
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("--- RGB ---");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt functionStmt) {
        this.src.nlI();
        super.visitFunctionStmt(functionStmt);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        this.src.nlI();
        this.src.add(stmtFlowCon.flow.toString().toLowerCase(), ";");
        return null;
    }

    @Override
    public Void visitStmtList(StmtList stmtList) {
        stmtList.get().stream().forEach(s -> s.accept(this));
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment stmtTextComment) {
        this.src.nlI().add("// ", stmtTextComment.textComment);
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        this.src.add("System.out.println(");
        textPrintFunct.param.get(0).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.src.add("[]");
        return null;
    }

    @Override
    public Void visitExprList(ExprList exprList) {
        exprList.get().stream().forEach(s -> s.accept(this));
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt ifStmt) {
        generateCodeFromIfElse(ifStmt);
        generateCodeFromElse(ifStmt);
        return null;
    }

    @Override
    public Void visitTernaryExpr(TernaryExpr te) {
        this.src.add("(").accept(te.condition, this).add("?").accept(te.thenPart, this).add(":").accept(te.elsePart, this).add(")");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        this.src.add("new ArrayList<>(Collections.nCopies( (int) ");
        listRepeat.param.get(1).accept(this);
        this.src.add(", ");
        if ( listRepeat.param.get(0).getBlocklyType() == BlocklyType.NUMBER ) {
            this.src.add(" (float) ");
        }
        listRepeat.param.get(0).accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        mainTask.variables.accept(this);
        this.src.nlI().nlI().add("void main() {").INCR();
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                this.src.add("(float) Math.pow(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(", 2)");
                return null;
            case ROOT:
                this.src.add("(float) Math.sqrt(");
                break;
            case LN:
                this.src.add("(float) Math.log(");
                break;
            case POW10:
                this.src.add("(float) Math.pow(10, ");
                break;
            case ROUNDUP:
                this.src.add("(float) Math.ceil(");
                break;
            case ROUNDDOWN:
                this.src.add("(float) Math.floor(");
                break;
            default:
                this.src.add("(float) Math.");
                this.src.add(mathSingleFunct.functName.name().toLowerCase(Locale.ENGLISH), "(");
                break;
        }
        mathSingleFunct.param.get(0).accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("Math.min(Math.max(");
        mathConstrainFunct.value.accept(this);
        this.src.add(", ");
        mathConstrainFunct.lowerBound.accept(this);
        this.src.add("), ");
        mathConstrainFunct.upperBound.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2 == 0)");
                break;
            case ODD:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 2 == 1)");
                break;
            case PRIME:
                this.src.add("( prime ");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case WHOLE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % 1 == 0)");
                break;
            case POSITIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" > 0)");
                break;
            case NEGATIVE:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" < 0)");
                break;
            case DIVISIBLE_BY:
                this.src.add("(");
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add(" % ");
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add(" == 0)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        switch ( mathOnListFunct.functName ) {
            case MIN:
                this.src.add("Collections.min(");
                mathOnListFunct.list.accept(this);
                break;
            case MAX:
                this.src.add("Collections.max(");
                mathOnListFunct.list.accept(this);
                break;
            case RANDOM:
                mathOnListFunct.list.accept(this);
                this.src.add(".get(0"); // TODO remove? implement?
                break;
            default:
                mathOnListFunct.list.accept(this);
                break;
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        this.src.add("(float) Math.random()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("( Math.round(Math.random() * ((");
        mathRandomIntFunct.to.accept(this);
        this.src.add(") - (");
        mathRandomIntFunct.from.accept(this);
        this.src.add("))) + (");
        mathRandomIntFunct.from.accept(this);
        this.src.add(") )");
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
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        this.src.add("(String.valueOf(");
        mathCastStringFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        this.src.add("String.valueOf((char)(int)(");
        mathCastCharFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        mathChangeStmt.var.accept(this);
        this.src.add(" += ");
        mathChangeStmt.delta.accept(this);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += String.valueOf(");
        textAppendStmt.text.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        this.src.add("Float.parseFloat(");
        textStringCastNumberFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }

    @Override
    public Void visitUnary(Unary unary) {
        this.src.add(unary.op.values[0]);
        unary.expr.accept(this);
        return null;
    }

    @Override
    public Void visitVar(Var var) {
        this.src.add(var.name);
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.src.nlI().add(var.getBlocklyType().getBlocklyName(), " ", var.name, " = ");
        var.value.accept(this);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        generateCodeFromWait(waitStmt);
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("(int)(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add(".charAt(");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        List<Expr> exprs = textJoinFunct.param.get();
        Iterator<Expr> iterator = exprs.iterator();
        while ( iterator.hasNext() ) {
            this.src.add("String.valueOf(");
            Expr expr = iterator.next();
            expr.accept(this);
            this.src.add(")");
            if ( iterator.hasNext() ) {
                this.src.add(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        this.src.nlI().nlI().add("void ", methodVoid.getMethodName(), "(");
        genParameterList(methodVoid.getParameters().el);
        ;
        this.src.add(") {").INCR().accept(methodVoid.body, this).DECR().nlI().add("}");
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt nnStepStmt) {
        return null;
    }

    @Override
    public Void visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal) {
        return null;
    }

    @Override
    public Void visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal) {
        return null;
    }

    @Override
    public Void visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt) {
        return null;
    }

    @Override
    public Void visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt) {
        return null;
    }

    @Override
    public Void visitNNGetWeight(NNGetWeight nnGetWeight) {
        return null;
    }

    @Override
    public Void visitNNGetBias(NNGetBias nnGetBias) {
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        this.src.nlI().nlI().add(methodReturn.getReturnType().getBlocklyName(), " ", methodReturn.getMethodName(), "(");
        genParameterList(methodReturn.getParameters().el);
        this.src.add(") {").INCR().accept(methodReturn.body, this).nlI().add("return ");
        methodReturn.returnValue.accept(this);
        this.src.add(";").DECR().nlI().add("}");
        return null;
    }


    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.src.add("if (");
        methodIfReturn.oraCondition.accept(this);
        this.src.add(") ");
        this.src.add("return ");
        methodIfReturn.oraReturnValue.accept(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt methodStmt) {
        this.src.nlI().nlI().accept(methodStmt.method, this).add(";");
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        src.add(methodCall.getMethodName(), "(");
        genExpressionList(methodCall.getParametersValues().el);
        src.add(")");
        return null;
    }

    @Override
    public Void visitStringConst(StringConst stringConst) {
        this.src.add("\"", StringEscapeUtils.escapeJava(stringConst.value.replaceAll("[<>\\$]", "")), "\"");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        this.src.add("if (!(");
        assertStmt.asserts.accept(this);
        this.src.add(")) {");
        incrIndentation();
        nlIndent();
        this.src.add("System.out.println(\"Assertion failed: \" + \"", assertStmt.msg, "\" + ");
        ((Binary) assertStmt.asserts).left.accept(this);
        this.src.add(" + \"", ((Binary) assertStmt.asserts).op.toString(), "\" + ");
        ((Binary) assertStmt.asserts).getRight().accept(this);
        this.src.add(");");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.src.add("System.out.println(");
        debugAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        this.src.add("System.out.println(");
        serialWriteAction.value.accept(this);
        this.src.add(");");
        return null;
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

    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case CAPTURED_TYPE_ARRAY_ITEM:
                return "";
            case ARRAY:
                return "List";
            case ARRAY_NUMBER:
                return "ArrayList<Float>";
            case ARRAY_STRING:
                return "ArrayList<String>";
            case ARRAY_COLOUR:
                return "ArrayList<PickColor>";
            case ARRAY_BOOLEAN:
                return "ArrayList<Boolean>";
            case ARRAY_CONNECTION:
                return "ArrayList<NXTConnection>";
            case BOOLEAN:
                return "boolean";
            case NUMBER:
                return "float";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "String";
            case COLOR:
                return "PickColor";
            case VOID:
                return "void";
            case CONNECTION:
                return "NXTConnection";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private boolean isStringExpr(Expr e) {
        switch ( e.getKind().getName() ) {
            case "STRING_CONST":
                return true;
            case "VAR":
                return e.getBlocklyType() == BlocklyType.STRING;
            case "FUNCTION_EXPR":
                BlockDescriptor functionKind = ((FunctionExpr) e).getFunction().getKind();
                return functionKind.hasName("TEXT_JOIN_FUNCT", "LIST_INDEX_OF");
            case "METHOD_EXPR":
                MethodCall methodCall = (MethodCall) ((MethodExpr) e).getMethod();
                return methodCall.getKind().hasName("METHOD_CALL") && methodCall.getReturnType() == BlocklyType.STRING;
            case "ACTION_EXPR":
                Action action = ((ActionExpr) e).action;
                return action.getKind().hasName("BLUETOOTH_RECEIVED_ACTION");

            default:
                return false;
        }
    }

    protected void generateCodeFromIfElse(IfStmt ifStmt) {
        int exprSize = ifStmt.expr.size();
        boolean first = true;
        for ( int i = 0; i < exprSize; i++ ) {
            if ( first ) {
                this.src.nlI().add("if");
                first = false;
            } else {
                this.src.add(" else if");
            }
            this.src.add(" ( ").accept(ifStmt.expr.get(i), this).add(" ) {");
            this.src.INCR().accept(ifStmt.thenList.get(i), this).DECR();
            if ( i + 1 < exprSize ) {
                nlIndent();
                this.src.add("} ");
            }
        }
    }

    protected void generateCodeFromWait(WaitStmt wait) {
        List<Stmt> sl = wait.statements.sl;
        int groups = sl.size();
        boolean first = true;
        for ( int i = 0; i < groups; i++ ) {
            if ( first ) {
                this.src.nlI().add("wait if");
                first = false;
            } else {
                this.src.add(" else wait if");
            }
            RepeatStmt group = (RepeatStmt) sl.get(i);
            this.src.add(" ( ").accept(group.expr, this).add(" ) {").INCR().accept(group.list, this).DECR().nlI().add("}");
        }
        this.src.add(";");
    }

    protected void generateCodeFromElse(IfStmt ifStmt) {
        if ( !ifStmt.elseList.get().isEmpty() ) {
            nlIndent();
            this.src.add("} else {");
            incrIndentation();
            ifStmt.elseList.accept(this);
            decrIndentation();
        }
        nlIndent();
        this.src.add("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr expr, StmtList sl) {
        this.src.nlI().add(stmtType).add(" ( ").accept(expr, this).add(" )").add(" {").INCR().accept(sl, this).DECR().nlI().add("};");
    }

    private void generateCodeFromStmtConditionFor(Expr expr, StmtList sl) {
        List<Expr> el = ((ExprList) expr).get();
        // TODO: really bad design. Copied from Java code generator. Refactor! Can introduce suble errors
        int posOpenBracket = el.toString().lastIndexOf("[");
        int posClosedBracket = el.toString().lastIndexOf("]");
        int counterPos = el.toString().lastIndexOf("-");
        String compareOp = (counterPos > posOpenBracket && counterPos < posClosedBracket) ? " > " : " < ";

        this.src.nlI().add("for ( ").accept(el.get(0), this).add(" = ").accept(el.get(1), this).add("; ").accept(el.get(0), this);
        this.src.add(compareOp).accept(el.get(2), this).add("; ").accept(el.get(0), this).add(" += ").accept(el.get(3), this).add(" ) {");
        this.src.INCR().accept(sl, this).DECR().nlI().add("};");
    }

    protected static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry(key, value);
    }

    protected static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
        return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
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

    protected void genParameterList(List<Expr> parameters) {
        boolean first = true;
        for ( Expr expr : parameters ) {
            first = src.addIf(first, ", ");
            genParameter((VarDeclaration) expr);
        }
    }

    protected void genParameter(VarDeclaration param) {
        this.src.add(param.getBlocklyType().getBlocklyName(), " ", param.name);
    }

    protected void genExpressionList(List<Expr> parameters) {
        boolean first = true;
        for ( Expr expr : parameters ) {
            first = src.addIf(first, ", ");
            this.src.accept(expr, this);
        }
    }

    protected String getBinaryOperatorSymbol(Op op) {
        return binaryOpSymbols().get(op);
    }

    protected String getUnaryOperatorSymbol(Unary.Op op) {
        return unaryOpSymbols().get(op);
    }

    protected static Map<Op, String> binaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(
                        entry(Op.ADD, "+"),
                        entry(Op.MINUS, "-"),
                        entry(Op.MULTIPLY, "*"),
                        entry(Op.DIVIDE, "/"),
                        entry(Op.MOD, "%"),
                        entry(Op.EQ, "=="),
                        entry(Op.NEQ, "!="),
                        entry(Op.LT, "<"),
                        entry(Op.LTE, "<="),
                        entry(Op.GT, ">"),
                        entry(Op.GTE, ">="),
                        entry(Op.AND, "&&"),
                        entry(Op.OR, "||"),
                        entry(Op.IN, ":"),
                        entry(Op.ASSIGNMENT, "="),
                        entry(Op.ADD_ASSIGNMENT, "+="),
                        entry(Op.MINUS_ASSIGNMENT, "-="),
                        entry(Op.MULTIPLY_ASSIGNMENT, "*="),
                        entry(Op.DIVIDE_ASSIGNMENT, "/="),
                        entry(Op.MOD_ASSIGNMENT, "%=")

                    )
                    .collect(entriesToMap()));
    }

    protected static Map<Unary.Op, String> unaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(

                        entry(Unary.Op.PLUS, "+"),
                        entry(Unary.Op.NEG, "-"),
                        entry(Unary.Op.NOT, "!"),
                        entry(Unary.Op.POSTFIX_INCREMENTS, "++"),
                        entry(Unary.Op.PREFIX_INCREMENTS, "++")

                    )
                    .collect(entriesToMap()));
    }

}
