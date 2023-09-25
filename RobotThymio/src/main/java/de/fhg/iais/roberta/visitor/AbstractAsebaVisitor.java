package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.antlr.v4.runtime.misc.OrderedHashSet;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
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
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
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
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.util.visitor.SourceBuilder;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public abstract class AbstractAsebaVisitor extends AbstractLanguageVisitor {
    protected Set<String> usedGlobalVarInFunctions = new OrderedHashSet<>();
    protected int stateCounter = 0;
    protected int loopCounter = -1;
    private int nestedBinaryCounter = -1;
    private int maxNestedBinaries = 1;

    private int nestedMethodCallsCounter = -1;
    private int maxNestedMethodCalls = 1;
    List<Integer> loopsStart = new ArrayList<>();
    List<Integer> loopsBody = new ArrayList<>();
    List<Integer> loopsEnd = new ArrayList<>();
    List<Integer> funcStart = new ArrayList<>();
    protected int funcCounter = -1;
    protected boolean ifOnce = false;

    protected ArrayList<String> myMethods;
    List<Unary.Op> unarySyms = new ArrayList<>();


    protected AbstractAsebaVisitor(List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    protected void nestedBinaryCounterPlus() {
        this.nestedBinaryCounter++;
        this.maxNestedBinaries = Math.max(this.maxNestedBinaries, this.nestedBinaryCounter + 1);
    }

    protected void nestedBinaryCounterMinus() {
        this.nestedBinaryCounter--;
    }

    protected void nestedMethodCallsCounterPlus() {
        this.nestedMethodCallsCounter++;
        this.maxNestedMethodCalls = Math.max(this.maxNestedMethodCalls, this.nestedMethodCallsCounter + 1);
    }

    protected void nestedMethodCallsCounterMinus() {
        this.nestedMethodCallsCounter--;
    }

    public int getMaxNestedBinaries() {
        return this.maxNestedBinaries;
    }

    public int getMaxNestedMethodCalls() {
        return this.maxNestedMethodCalls;
    }

    protected static Map<Binary.Op, String> binaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(
                        AbstractLanguageVisitor.entry(Binary.Op.ADD, "+"),
                        AbstractLanguageVisitor.entry(Binary.Op.MINUS, "-"),
                        AbstractLanguageVisitor.entry(Binary.Op.MULTIPLY, "*"),
                        AbstractLanguageVisitor.entry(Binary.Op.DIVIDE, "/"),
                        AbstractLanguageVisitor.entry(Binary.Op.MOD, "%"),
                        AbstractLanguageVisitor.entry(Binary.Op.EQ, "=="),
                        AbstractLanguageVisitor.entry(Binary.Op.NEQ, "!="),
                        AbstractLanguageVisitor.entry(Binary.Op.LT, "<"),
                        AbstractLanguageVisitor.entry(Binary.Op.LTE, "<="),
                        AbstractLanguageVisitor.entry(Binary.Op.GT, ">"),
                        AbstractLanguageVisitor.entry(Binary.Op.GTE, ">="),
                        AbstractLanguageVisitor.entry(Binary.Op.AND, "and"),
                        AbstractLanguageVisitor.entry(Binary.Op.OR, "or"),
                        AbstractLanguageVisitor.entry(Binary.Op.IN, "in"),
                        AbstractLanguageVisitor.entry(Binary.Op.ASSIGNMENT, "="),
                        AbstractLanguageVisitor.entry(Binary.Op.ADD_ASSIGNMENT, "+="),
                        AbstractLanguageVisitor.entry(Binary.Op.MINUS_ASSIGNMENT, "-="),
                        AbstractLanguageVisitor.entry(Binary.Op.MULTIPLY_ASSIGNMENT, "*="),
                        AbstractLanguageVisitor.entry(Binary.Op.DIVIDE_ASSIGNMENT, "/="),
                        AbstractLanguageVisitor.entry(Binary.Op.MOD_ASSIGNMENT, "%=")

                    )
                    .collect(AbstractLanguageVisitor.entriesToMap()));
    }

    protected static Map<Unary.Op, String> unaryOpSymbols() {
        return Collections
            .unmodifiableMap(
                Stream
                    .of(
                        AbstractLanguageVisitor.entry(Unary.Op.PLUS, "+"),
                        AbstractLanguageVisitor.entry(Unary.Op.NEG, "-"),
                        AbstractLanguageVisitor.entry(Unary.Op.NOT, "not"),
                        AbstractLanguageVisitor.entry(Unary.Op.POSTFIX_INCREMENTS, "++"),
                        AbstractLanguageVisitor.entry(Unary.Op.PREFIX_INCREMENTS, "++")

                    )
                    .collect(AbstractLanguageVisitor.entriesToMap()));
    }

    @Override
    public String getEnumCode(IMode value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    public String getEnumCode(String value) {
        return "'" + value.toLowerCase() + "'";
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        throw new DbcException("Block not supported");
    }

    @Override
    protected void generateSubExpr(SourceBuilder src, boolean minusAdaption, Expr expr, Binary binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && !expr.getKind().hasName("BINARY") ) {
            // parentheses are omitted
            if ( expr.getKind().hasName("NUM_CONST") ) {
                src.add(((NumConst) expr).value);
            } else {
                expr.accept(this);
            }
        } else {
            src.add("( ");
            expr.accept(this);
            src.add(" )");
        }
    }

    @Override
    public Void visitAssignStmt(AssignStmt assignStmt) {
        if ( assignStmt.name.getBlocklyType() == BlocklyType.COLOR ) {
            assignStmt.expr.accept(this);
            nlIndent();
            this.src.add(((Var) assignStmt.name).getCodeSafeName(), " = ___color_");
        } else if ( assignStmt.name.getBlocklyType() == BlocklyType.NUMBER ) {
            assignStmt.expr.accept(this);
            nlIndent();
            this.src.add(((Var) assignStmt.name).getCodeSafeName(), " = _A");
        } else {
            if ( assignStmt.name.getBlocklyType() == BlocklyType.ARRAY_NUMBER ) {
                this.src.add(((Var) assignStmt.name).getCodeSafeName(), " = ");
                assignStmt.expr.accept(this);
                nlIndent();
            } else {
                if ( assignStmt.name.getBlocklyType() == BlocklyType.ARRAY_COLOUR ) {
                    if ( assignStmt.expr.getClass().equals(Var.class) ) {
                        this.src.add(((Var) assignStmt.name).getCodeSafeName(), "_r = ", ((Var) assignStmt.expr).getCodeSafeName(), "_r");
                        nlIndent();
                        this.src.add(((Var) assignStmt.name).getCodeSafeName(), "_g = ", ((Var) assignStmt.expr).getCodeSafeName(), "_g");
                        nlIndent();
                        this.src.add(((Var) assignStmt.name).getCodeSafeName(), "_b = ", ((Var) assignStmt.expr).getCodeSafeName(), "_b");
                    } else {
                        if ( assignStmt.expr.getClass().equals(ListCreate.class) ) {
                            this.visitListCreateColor(((Var) assignStmt.name).getCodeSafeName(), (ListCreate) assignStmt.expr, false);
                        } else if ( assignStmt.expr.getClass().equals(FunctionExpr.class) ) {
                            ListRepeat listRepeat = (ListRepeat) ((FunctionExpr) assignStmt.expr).function;
                            this.visitListRepeatColor(((Var) assignStmt.name).getCodeSafeName(), listRepeat, false);
                        }
                    }
                } else {
                    throw new DbcException("Invalid variable type used");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        this.nestedBinaryCounterPlus();
        switch ( binary.op ) {
            case ADD:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case ASSIGNMENT: {
                visitBinaryBothSides(binary.left, binary.right);
                this.src.add("_A = _B[", this.nestedBinaryCounter, "] ", getBinaryOperatorSymbol(binary.op), " _C[", this.nestedBinaryCounter, "]");
                break;
            }
            case EQ:
            case NEQ:
            case LT:
            case LTE:
            case GT:
            case GTE: {
                visitBinaryBothSides(binary.left, binary.right);
                this.src.add("if _B[", this.nestedBinaryCounter, "] ", getBinaryOperatorSymbol(binary.op), " _C[", this.nestedBinaryCounter, "] then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                break;
            }
            case AND:
            case OR: {
                visitBinaryBothSides(binary.left, binary.right);
                this.src.add("if _B[", this.nestedBinaryCounter, "] == 1 ", getBinaryOperatorSymbol(binary.op), " _C[", this.nestedBinaryCounter, "] == 1 then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                break;
            }
            default:
                throw new DbcException("Binary op not supported.");
        }
        this.nestedBinaryCounterMinus();
        return null;
    }

    private void visitBinaryBothSides(Expr left, Expr right) {
        if ( left.getKind().hasName("VAR") ) {
            this.src.add("_A = ");
            this.src.add(((Var) left).getCodeSafeName());
        } else {
            left.accept(this);
        }
        nlIndent();
        this.src.add("_B[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
        right.accept(this);
        nlIndent();
        this.src.add("_C[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
    }

    @Override
    public Void visitVar(Var var) {
        if ( var.getBlocklyType() == (BlocklyType.NUMBER) ) {
            this.src.add("_A = ", var.getCodeSafeName());
        } else if ( var.getBlocklyType() == (BlocklyType.COLOR) ) {
            this.src.add("___color_ = ", var.getCodeSafeName());
        } else {
            this.src.add(var.getCodeSafeName());
        }
        return null;
    }


    @Override
    public Void visitBoolConst(BoolConst boolConst) {
        String value = boolConst.value ? "1" : "0";
        this.src.add("_A = ", value);
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.src.add("print(");
        debugAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case BOOLEAN:
                this.src.add("1");
                break;
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                this.src.add("[0]");
                break;
            case NULL:
                break;
            default:
                this.src.add("[[EmptyExpr [defVal=", emptyExpr.getDefVal(), "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        if ( getSubFunct.functName == FunctionNames.GET_SUBLIST ) {
            getSubFunct.param.get(0).accept(this);
            this.src.add("[");
            switch ( (IndexLocation) getSubFunct.strParam.get(0) ) {
                case FIRST:
                    this.src.add("0:");
                    break;
                case FROM_END:
                    this.src.add("-1 -");
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(":");
                    break;
                case FROM_START:
                    getSubFunct.param.get(1).accept(this);
                    this.src.add(":");
                    break;
                default:
                    break;
            }
            switch ( (IndexLocation) getSubFunct.strParam.get(1) ) {
                case LAST:
                    // append nothing
                    break;
                case FROM_END:
                    this.src.add("-1 -");
                    try {
                        getSubFunct.param.get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.param.get(1).accept(this);
                    }
                    break;
                case FROM_START:
                    try {
                        getSubFunct.param.get(2).accept(this);
                    } catch ( IndexOutOfBoundsException e ) { // means that our start index does not have a variable
                        getSubFunct.param.get(1).accept(this);
                    }
                    break;
                default:
                    break;
            }
            this.src.add("]");
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.location ) {
            case FIRST:
                indexOfFunct.value.accept(this);
                this.src.add(".index(");
                indexOfFunct.find.accept(this);
                this.src.add(")");
                break;
            case LAST:
                this.src.add("(len(");
                indexOfFunct.value.accept(this);
                this.src.add(") - 1) - ");
                indexOfFunct.value.accept(this);
                this.src.add("[::-1].index(");
                indexOfFunct.find.accept(this);
                this.src.add(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        this.src.add("len( ");
        lengthOfListFunct.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        this.src.add("not ");
        isListEmptyFunct.value.accept(this);
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        if ( listCreate.getBlocklyType().toString().equals("NUMBER") ) {
            int listSize = listCreate.exprList.get().size();
            this.src.add("[");
            for ( int i = 0; i < listSize; i++ ) {
                if ( listCreate.exprList.get().get(i).getKind().hasName("NUM_CONST") ) {
                    NumConst a = ((NumConst) listCreate.exprList.get().get(i));
                    this.src.add(a.value);
                    if ( i < listSize - 1 ) {
                        this.src.add(", ");
                    }
                }
            }
            this.src.add("]");
        } else if ( listCreate.getBlocklyType().toString().equals("COLOR") ) {
            throw new DbcException("ListCreate can only be evaluated from a function!");
        }
        return null;
    }

    private void visitListRepeatColor(String var, ListRepeat listRepeat, boolean withVar) {
        int listSize;
        try {
            listSize = Integer.parseInt(((NumConst) listRepeat.param.get(1)).value);
            if ( withVar ) {
                this.src.add("var ", var, "_r[] = [");
            } else {
                this.src.add(var, "_r = [");
            }
            for ( int i = 0; i < listSize; i++ ) {
                this.src.add(((ColorConst) listRepeat.param.get(0)).getRedChannelInt());
                if ( i != listSize - 1 ) {
                    this.src.add(", ");
                }
            }
            this.src.add("]");
            nlIndent();
            if ( withVar ) {
                this.src.add("var ", var, "_g[] = [");
            } else {
                this.src.add(var, "_g = [");
            }
            for ( int i = 0; i < listSize; i++ ) {
                this.src.add(((ColorConst) listRepeat.param.get(0)).getGreenChannelInt());
                if ( i != listSize - 1 ) {
                    this.src.add(", ");
                }
            }
            this.src.add("]");
            nlIndent();
            if ( withVar ) {
                this.src.add("var ", var, "_b[] = [");
            } else {
                this.src.add(var, "_b = [");
            }
            for ( int i = 0; i < listSize; i++ ) {
                this.src.add(((ColorConst) listRepeat.param.get(0)).getBlueChannelInt());
                if ( i != listSize - 1 ) {
                    this.src.add(", ");
                }
            }
            this.src.add("]");
        } catch ( Exception e ) {
            throw new DbcException(e.getMessage());
        }
    }

    private void visitListCreateColor(String var, ListCreate listCreate, boolean withVar) {
        int listSize = listCreate.exprList.get().size();
        if ( withVar ) {
            this.src.add("var ", var, "_r[] = [");
        } else {
            this.src.add(var, "_r = [");
        }
        for ( int i = 0; i < listSize; i++ ) {
            if ( listCreate.exprList.get().get(i).getKind().hasName("COLOR_CONST") ) {
                this.src.add(((ColorConst) listCreate.exprList.get().get(i)).getRedChannelInt());
            } else if ( listCreate.exprList.get().get(i).getKind().hasName("RGB___color_") ) {
                ((RgbColor) listCreate.exprList.get().get(i)).R.accept(this);
            }
            if ( i != listSize - 1 ) {
                this.src.add(", ");
            }
        }
        this.src.add("]");
        nlIndent();
        if ( withVar ) {
            this.src.add("var ", var, "_g[] = [");
        } else {
            this.src.add(var, "_g = [");
        }
        for ( int i = 0; i < listSize; i++ ) {
            if ( listCreate.exprList.get().get(i).getKind().hasName("COLOR_CONST") ) {
                this.src.add(((ColorConst) listCreate.exprList.get().get(i)).getGreenChannelInt());
            } else if ( listCreate.exprList.get().get(i).getKind().hasName("RGB___color_") ) {
                ((RgbColor) listCreate.exprList.get().get(i)).G.accept(this);
            }
            if ( i != listSize - 1 ) {
                this.src.add(", ");
            }
        }
        this.src.add("]");
        nlIndent();
        if ( withVar ) {
            this.src.add("var ", var, "_b[] = [");
        } else {
            this.src.add(var, "_b = [");
        }
        for ( int i = 0; i < listSize; i++ ) {
            if ( listCreate.exprList.get().get(i).getKind().hasName("COLOR_CONST") ) {
                this.src.add(((ColorConst) listCreate.exprList.get().get(i)).getBlueChannelInt());
            } else if ( listCreate.exprList.get().get(i).getKind().hasName("RGB___color_") ) {
                ((RgbColor) listCreate.exprList.get().get(i)).B.accept(this);
            }
            if ( i != listSize - 1 ) {
                this.src.add(", ");
            }
        }
        this.src.add("]");
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        listGetIndex.param.get(1).accept(this);
        nlIndent();
        if ( listGetIndex.param.get(0).getBlocklyType().toString().equals("ARRAY_COLOUR") ) {
            this.src.add("___color_[0] = ");
            listGetIndex.param.get(0).accept(this);
            this.src.add("_r[_A]");
            nlIndent();
            this.src.add("___color_[1] = ");
            listGetIndex.param.get(0).accept(this);
            this.src.add("_g[_A]");
            nlIndent();
            this.src.add("___color_[2] = ");
            listGetIndex.param.get(0).accept(this);
            this.src.add("_b[_A]");
            nlIndent();
        } else if ( listGetIndex.param.get(0).getBlocklyType().toString().equals("ARRAY_NUMBER") ) {
            if ( listGetIndex.mode == GET ) {
                this.src.add("_A = ");
                listGetIndex.param.get(0).accept(this);
                this.src.add("[_A]");
                nlIndent();
            }
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat listRepeat) {
        if ( listRepeat.typeVar.toString().equals("NUMBER") ) {
            try {
                int listSize = Integer.parseInt(((NumConst) listRepeat.param.get(1)).value);
                int value = Integer.parseInt(((NumConst) listRepeat.param.get(0)).value);
                this.src.add("[");
                for ( int i = 0; i < listSize; i++ ) {
                    this.src.add(value);
                    if ( i < listSize - 1 ) {
                        this.src.add(", ");
                    }
                }
                this.src.add("]");
            } catch ( Exception e ) {
                throw new DbcException(e.getMessage());
            }
        } else if ( listRepeat.typeVar.toString().equals("COLOR") ) {
            throw new DbcException("ListRepeat can only be evaluated from a function!");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        listSetIndex.param.get(1).accept(this);
        nlIndent();
        if ( listSetIndex.param.get(0).getBlocklyType().toString().equals("ARRAY_COLOUR") ) {
            listSetIndex.param.get(2).accept(this);
            nlIndent();
            listSetIndex.param.get(0).accept(this);
            this.src.add("_r[_A] = ___color_[0]");
            nlIndent();
            listSetIndex.param.get(0).accept(this);
            this.src.add("_g[_A] = ___color_[1]");
            nlIndent();
            listSetIndex.param.get(0).accept(this);
            this.src.add("_b[_A] = ___color_[2]");
            nlIndent();
        } else if ( listSetIndex.param.get(0).getBlocklyType().toString().equals("ARRAY_NUMBER") ) {
            this.nestedBinaryCounterPlus();
            this.src.add("_B[", this.nestedBinaryCounter, "] = _A");
            nlIndent();
            listSetIndex.param.get(2).accept(this);
            nlIndent();
            listSetIndex.param.get(0).accept(this);
            this.src.add("[_A] = _B[", this.nestedBinaryCounter, "]");
            this.nestedBinaryCounterMinus();
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        throw new DbcException("Characters not supported.");
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        throw new DbcException("Strings not supported.");
    }

    @Override
    public Void visitMathConst(MathConst mathConst) {
        throw new DbcException("No floats in Aseba");
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        throw new DbcException("Function not supported");
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        if ( mathNumPropFunct.functName != FunctionNames.DIVISIBLE_BY ) {
            mathNumPropFunct.param.get(0).accept(this);
            nlIndent();
        }
        switch ( mathNumPropFunct.functName ) {
            case EVEN:
                this.src.add("_A = _A % 2");
                break;
            case ODD:
                this.src.add("_A = _A % 2");
                nlIndent();
                this.src.add("_A = abs(_A - 1)");
                break;
            case WHOLE:
                this.src.add("_A = _A % 1");
                nlIndent();
                this.src.add("if _A > 0 then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                break;
            case POSITIVE:
                this.src.add("if _A > 0 then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                break;
            case NEGATIVE:
                this.src.add("if _A < 0 then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                break;
            case DIVISIBLE_BY:
                this.nestedBinaryCounterPlus();
                mathNumPropFunct.param.get(0).accept(this);
                this.src.add("_B[", this.nestedBinaryCounter, "] = _A");
                nlIndent();
                mathNumPropFunct.param.get(1).accept(this);
                this.src.add("_C[", this.nestedBinaryCounter, "] = _A");
                nlIndent();
                this.src.add("_A =  _B[", this.nestedBinaryCounter, "] % _C[", this.nestedBinaryCounter, "]");
                nlIndent();
                this.src.add("if _A == 0 then");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 1");
                decrIndentation();
                nlIndent();
                this.src.add("else");
                incrIndentation();
                nlIndent();
                this.src.add("_A = 0");
                decrIndentation();
                nlIndent();
                this.src.add("end");
                this.nestedBinaryCounterMinus();
                break;
            case PRIME:
                throw new DbcException("Statement not supported by Aseba!");
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        throw new DbcException("Statement not supported by Aseba!");
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        throw new DbcException("Floats not supported by Aseba!");
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.nestedBinaryCounterPlus();
        mathRandomIntFunct.from.accept(this);
        nlIndent();
        this.src.add("_B[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
        mathRandomIntFunct.to.accept(this);
        nlIndent();
        this.src.add("_C[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
        this.src.add("call math.rand(_A)");
        nlIndent();
        this.src.add("_A = (_A / (32767 / (_C[", this.nestedBinaryCounter, "] - _B[", this.nestedBinaryCounter, "] + 1)) / 2) + (_C[", this.nestedBinaryCounter, "] - _B[", this.nestedBinaryCounter, "]) / 2 + _B[", this.nestedBinaryCounter, "]");
        nlIndent();
        this.nestedBinaryCounterMinus();
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        this.nestedBinaryCounterPlus();
        switch ( mathOnListFunct.functName ) {
            case MIN:
                this.src.add("call math.stat(");
                mathOnListFunct.list.accept(this);
                this.src.add(", _A, _B[", this.nestedBinaryCounter, "], _B[", this.nestedBinaryCounter, "])");
                nlIndent();
                break;
            case MAX:
                this.src.add("call math.stat(");
                mathOnListFunct.list.accept(this);
                this.src.add(", _B[", this.nestedBinaryCounter, "], _A, _B[", this.nestedBinaryCounter, "])");
                nlIndent();
                break;
            case AVERAGE:
                this.src.add("call math.stat(");
                mathOnListFunct.list.accept(this);
                this.src.add(", _B[", this.nestedBinaryCounter, "], _B[", this.nestedBinaryCounter, "], _A)");
                nlIndent();
                break;
            default:
                throw new DbcException("Unsupported math on list mode");
        }
        this.nestedBinaryCounterMinus();
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        mathSingleFunct.param.get(0).accept(this);
        nlIndent();
        switch ( mathSingleFunct.functName ) {
            case ROOT:
                this.src.add("call math.sqrt(_A, _A)");
                break;
            case SIN:
                this.src.add("call math.sin(_A, _A)");
                break;
            case COS:
                this.src.add("call math.cos(_A, _A)");
                break;
            case SQUARE:
                this.src.add("call math.mul(_A, _A, _A)");
                break;
            case ABS:
                this.src.add("_A = abs(_A)");
                break;
            default:
                throw new DbcException("Statement not supported by Aseba!");
        }
        return null;
    }

    @Override
    public Void visitMathModuloFunct(MathModuloFunct mathModuloFunct) {
        this.nestedBinaryCounterPlus();
        visitBinaryBothSides(mathModuloFunct.dividend, mathModuloFunct.divisor);
        this.src.add("_A = _B[", this.nestedBinaryCounter, "] % _C[", this.nestedBinaryCounter, "]");
        this.nestedBinaryCounterMinus();
        return null;
    }

    @Override
    public Void visitMathChangeStmt(MathChangeStmt mathChangeStmt) {
        this.nestedBinaryCounterPlus();
        visitBinaryBothSides(mathChangeStmt.var, mathChangeStmt.delta);
        this.src.add("_B[", this.nestedBinaryCounter, "] += _C[", this.nestedBinaryCounter, "]");
        nlIndent();
        this.src.add(" = _B[", this.nestedBinaryCounter, "]");
        this.nestedBinaryCounterMinus();
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        throw new DbcException("BLOCK_NOT_SUPPORTED");
    }

    @Override
    public Void visitMethodCall(MethodCall methodCall) {
        this.nestedMethodCallsCounterPlus();
        this.stateCounter++;
        this.src.add("_method_count++");
        nlIndent();
        this.src.add("_return_state[_method_count] = ", this.stateCounter);
        nlIndent();
        this.src.add("callsub ", methodCall.getCodeSafeMethodName());
        decrIndentation();
        nlIndent();
        this.src.add("elseif _state == ", this.stateCounter, " then");
        incrIndentation();
        nlIndent();
        this.src.add("_method_count--");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.src.add("if ");
        methodIfReturn.oraCondition.accept(this);
        if ( !methodIfReturn.oraReturnValue.getKind().hasName("EMPTY_EXPR") ) {
            this.src.add(": return ");
            methodIfReturn.oraReturnValue.accept(this);
        } else {
            this.src.add(": return None");
        }
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn methodReturn) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public Void visitMethodVoid(MethodVoid methodVoid) {
        this.funcCounter++;
        this.src.add(this.getIfElse(), " _state == ", this.funcStart.get(this.funcCounter), " then");
        incrIndentation();
        methodVoid.body.accept(this);
        nlIndent();
        this.src.add("_state = _return_state[_method_count]");
        decrIndentation();
        return null;
    }

    String getIfElse() {
        String ifElse = "";
        if ( this.ifOnce ) {
            ifElse = "elseif";
        } else {
            ifElse = "if";
            this.ifOnce = true;
        }
        return ifElse;
    }

    @Override
    public Void visitNullConst(NullConst nullConst) {
        throw new DbcException("No NULL in Aseba");
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        this.src.add("_A = ", numConst.value);
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        loopCounter++;
        this.loopsStart.add(0);
        this.loopsEnd.add(0);
        this.loopsBody.add(0);
        this.stateCounter++;
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        nlIndent();
        this.src.add("elseif _state == ", this.stateCounter, " then");
        incrIndentation();
        nlIndent();
        switch ( repeatStmt.mode.toString() ) {
            case "FOREVER":
            case "UNTIL":
            case "WHILE":
            case "WAIT":
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case "TIMES":
            case "FOR":
                generateCodeFromStmtConditionFor("if", repeatStmt.expr);
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        this.loopsStart.set(this.loopCounter, this.stateCounter);
        incrIndentation();
        nlIndent();
        this.stateCounter++;
        this.loopsBody.set(this.loopCounter, this.stateCounter);
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        nlIndent();
        this.src.add("else");
        incrIndentation();
        nlIndent();
        this.stateCounter++;
        this.loopsEnd.set(loopCounter, this.stateCounter);
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        nlIndent();
        this.src.add("end");
        decrIndentation();
        nlIndent();
        this.src.add("elseif _state == ", this.loopsBody.get(this.loopCounter), " then");
        incrIndentation();
        repeatStmt.list.accept(this);
        nlIndent();
        if ( repeatStmt.mode.equals(RepeatStmt.Mode.TIMES) || repeatStmt.mode.equals(RepeatStmt.Mode.FOR) ) {
            this.src.add(((Var) ((ExprList) repeatStmt.expr).get().get(0)).getCodeSafeName(), " += _C[", this.nestedBinaryCounter, "]");
            this.nestedBinaryCounterMinus();
            nlIndent();
        }
        this.src.add("_state = ", this.loopsStart.get(this.loopCounter));
        decrIndentation();
        nlIndent();
        this.stateCounter++;
        this.src.add("elseif _state == ", this.loopsEnd.get(this.loopCounter), " then");
        incrIndentation();
        loopCounter--;
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        if ( stmtFlowCon.flow.name().toLowerCase().equals(C.BREAK) ) {
            this.src.add("_state = ", this.loopsEnd.get(this.loopCounter));
        } else if ( stmtFlowCon.flow.name().toLowerCase().equals(C.CONTINUE) ) {
            this.src.add("_state = ", this.loopsStart.get(this.loopCounter));
        } else {
            throw new DbcException("Invalid flow control statement!");
        }
        decrIndentation();
        nlIndent();
        this.stateCounter++;
        this.src.add("elseif _state == ", this.stateCounter, " then");
        incrIndentation();
        return null;
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment stmtTextComment) {
        this.src.add("# ", stmtTextComment.textComment.replace("\n", " "));
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        this.src.add("ord(");
        textCharCastNumberFunct.value.accept(this);
        this.src.add("[");
        textCharCastNumberFunct.atIndex.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        this.src.add("\"\".join(str(arg) for arg in [");
        textJoinFunct.param.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        this.src.add("print(");
        textPrintFunct.param.get(0).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        throw new DbcException("Floats not supported in Aseba!");
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
        this.unarySyms.add(unary.op);
        unary.expr.accept(this);
        nlIndent();
        int lastIndex = this.unarySyms.size() - 1;
        switch ( this.unarySyms.get(lastIndex) ) {
            case PLUS:
            case NEG:
                this.src.add("_A = ", getUnaryOperatorSymbol(this.unarySyms.get(lastIndex)), "_A");
                break;
            case NOT:
                this.src.add("_A = abs(_A - 1)");
                break;
            case POSTFIX_INCREMENTS:
            case PREFIX_INCREMENTS:
                this.src.add("_A = _A", getUnaryOperatorSymbol(this.unarySyms.get(lastIndex)));
                break;
            default:
                throw new DbcException("Unsupported unary symbol");

        }
        this.unarySyms.remove(lastIndex);
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        if ( var.getBlocklyType() == BlocklyType.NUMBER || var.getBlocklyType() == BlocklyType.COLOR ) {
            if ( var.value.getKind().hasName("NUM_CONST") ) {
                this.src.add("var ", var.getCodeSafeName(), " = ");
                this.src.add(((NumConst) var.value).value);
            } else if ( var.value.getKind().hasName("COLOR_CONST") ) {
                this.src.add("var ", var.getCodeSafeName(), "[] = ");
                this.src.add("[", ((ColorConst) var.value).getRedChannelInt(), ", ", ((ColorConst) var.value).getGreenChannelInt(), ", ", ((ColorConst) var.value).getBlueChannelInt(), "]");
                nlIndent();
            }
        } else if ( var.value.getKind().getName().equals("LIST_CREATE") ) {
            if ( ((ListCreate) var.value).getBlocklyType().toString().equals("COLOR") ) {
                this.visitListCreateColor(var.getCodeSafeName(), ((ListCreate) var.value), true);
                return null;
            } else if ( ((ListCreate) var.value).getBlocklyType().toString().equals("NUMBER") ) {
                this.src.add("var ", var.getCodeSafeName(), "[] = ");
                var.value.accept(this);
            } else {
                throw new DbcException("Unsupported list type");
            }
        } else if ( ((FunctionExpr) var.value).function.getClass().equals(ListRepeat.class) ) {
            ListRepeat listRepeat = (ListRepeat) ((FunctionExpr) var.value).function;
            if ( listRepeat.typeVar.toString().equals("COLOR") ) {
                this.visitListRepeatColor(var.getCodeSafeName(), listRepeat, true);
                return null;
            } else if ( listRepeat.typeVar.toString().equals("NUMBER") ) {
                this.src.add("var ", var.getCodeSafeName(), "[] = ");
                var.value.accept(this);
            } else {
                throw new DbcException("Unsupported list type");
            }
        } else {
            throw new DbcException("Unsupported variable declaration constant");
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.stateCounter++;
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        nlIndent();
        this.src.add("elseif _state == ", this.stateCounter, " then");
        incrIndentation();
        nlIndent();
        int stmtSize = waitStmt.statements.get().size();
        int statesSize = stmtSize + 1;
        int[] states = new int[statesSize];
        int i = 0;
        for ( ; i < stmtSize; i++ ) {
            if ( i != 0 ) {
                this.src.add("else");
                incrIndentation();
                nlIndent();
            }
            generateCodeFromStmtCondition("if", ((RepeatStmt) waitStmt.statements.get().get(i)).expr);
            incrIndentation();
            nlIndent();
            this.stateCounter++;
            states[i] = this.stateCounter;
            this.src.add("_state = ", this.stateCounter);
            decrIndentation();
            nlIndent();
        }
        this.stateCounter++;
        states[i] = this.stateCounter;
        for ( int j = 0; j < stmtSize; j++ ) {
            this.src.add("end");
            decrIndentation();
            nlIndent();
        }
        for ( int j = 0; j < stmtSize; j++ ) {
            this.src.add("elseif _state == ", states[j], " then");
            incrIndentation();
            StmtList then = ((RepeatStmt) waitStmt.statements.get().get(j)).list;
            if ( !then.get().isEmpty() ) {
                then.accept(this);
            }
            nlIndent();
            this.src.add("_state = ", states[statesSize - 1]);
            decrIndentation();
            nlIndent();
        }
        this.src.add("elseif _state == ", states[statesSize - 1], " then");
        incrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return null;
    }

    @Override
    protected void generateCodeFromTernary(TernaryExpr ternaryExpr) {
        throw new DbcException("No Ternary Expressions in Aseba!");
    }

    @Override
    protected void generateCodeFromIfElse(IfStmt ifStmt) {
        int stmtSize = ifStmt.expr.size();
        int statesSize = stmtSize + 1 + (ifStmt.elseList.get().isEmpty() ? 0 : 1);
        int[] states = new int[statesSize];
        int i = 0;
        for ( ; i < stmtSize; i++ ) {
            generateCodeFromStmtCondition("if", ifStmt.expr.get(i));
            incrIndentation();
            nlIndent();
            this.stateCounter++;
            states[i] = this.stateCounter;
            this.src.add("_state = ", this.stateCounter);
            decrIndentation();
            nlIndent();
            this.src.add("else");
            incrIndentation();
            nlIndent();
        }
        this.stateCounter++;
        states[i] = this.stateCounter;
        i++;
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        if ( !ifStmt.elseList.get().isEmpty() ) {
            this.stateCounter++;
            states[i] = this.stateCounter;
        }
        for ( int j = 0; j < stmtSize; j++ ) {
            nlIndent();
            this.src.add("end");
            decrIndentation();
        }
        nlIndent();
        for ( int j = 0; j < stmtSize; j++ ) {
            this.src.add("elseif _state == ", states[j], " then");
            incrIndentation();
            StmtList then = ifStmt.thenList.get(j);
            if ( !then.get().isEmpty() ) {
                then.accept(this);
            }
            nlIndent();
            this.src.add("_state = ", states[statesSize - 1]);
            decrIndentation();
            nlIndent();
        }
        if ( !ifStmt.elseList.get().isEmpty() ) {
            this.src.add("elseif _state == ", states[statesSize - 2], " then");
            incrIndentation();
            ifStmt.elseList.accept(this);
            nlIndent();
            this.src.add("_state = ", states[statesSize - 1]);
            decrIndentation();
            nlIndent();
        }
        this.src.add("elseif _state == ", states[statesSize - 1], " then");
        incrIndentation();
    }

    @Override
    protected void generateCodeFromElse(IfStmt ifStmt) {
        // nothing to do, integrated in generateCodeFromIfElse()
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        return "";
    }

    protected void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        expr.accept(this);
        nlIndent();
        this.src.add(stmtType, " ", "_A == 1 then");
    }

    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        ExprList expressions = (ExprList) expr;
        expressions.get().get(1).accept(this);
        nlIndent();
        this.src.add(((Var) expressions.get().get(0)).getCodeSafeName(), " = _A");
        nlIndent();
        this.nestedBinaryCounterPlus();
        expressions.get().get(2).accept(this);
        nlIndent();
        this.src.add("_B[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
        expressions.get().get(3).accept(this);
        nlIndent();
        this.src.add("_C[", this.nestedBinaryCounter, "] = _A");
        nlIndent();
        this.stateCounter++;
        this.src.add("_state = ", this.stateCounter);
        decrIndentation();
        nlIndent();
        this.src.add("elseif _state == ", this.stateCounter, " then");
        incrIndentation();
        nlIndent();
        this.src.add(stmtType, " ", ((Var) expressions.get().get(0)).getCodeSafeName(), " < _B[", this.nestedBinaryCounter, "] then");
    }

    @Override
    protected String getBinaryOperatorSymbol(Binary.Op op) {
        return AbstractAsebaVisitor.binaryOpSymbols().get(op);
    }

    @Override
    protected String getUnaryOperatorSymbol(Unary.Op op) {
        return AbstractAsebaVisitor.unaryOpSymbols().get(op);
    }
}
