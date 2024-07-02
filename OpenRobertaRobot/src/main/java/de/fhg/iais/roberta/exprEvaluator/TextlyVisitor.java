package de.fhg.iais.roberta.exprEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
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
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
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
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.BlocklyRegion;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

public class TextlyVisitor<T> extends ExprlyBaseVisitor<T> {
    /**
     * @return AST instance for the whole expression
     */
    @Override
    public T visitExpression(ExpressionContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public T visitStatementList(ExprlyParser.StatementListContext ctx) {

        StmtList stmtList = new StmtList();

        for ( ExprlyParser.StmtContext stmt : ctx.stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            stmtList.addStmt(statement);
        }

        return (T) stmtList;
    }

    @Override
    public T visitNullConst(ExprlyParser.NullConstContext ctx) {
        return (T) new NullConst(mkPropertyFromClass(ctx, NullConst.class));
    }

    /**
     * @return AST instance of a color
     */
    @Override
    public T visitCol(ExprlyParser.ColContext ctx) {
        String colorText = ctx.COLOR().getText();
        String colorHex;
        switch ( colorText ) {
            case "#black":
                colorHex = "#000000";
                break;
            case "#blue":
                colorHex = "#0057A6";
                break;
            case "#green":
                colorHex = "#00642E";
                break;
            case "#yellow":
                colorHex = "#F7D117";
                break;
            case "#red":
                colorHex = "#B30006";
                break;
            case "#white":
                colorHex = "#FFFFFF";
                break;
            case "#brown":
                colorHex = "#532115";
                break;
            case "#none":
                colorHex = "#585858";
                break;
            default:
                colorHex = colorText.substring(5, 11);
        }
        return (T) new ColorConst(mkInlineProperty(ctx, "robColour_picker"), colorHex);
    }

    /**
     * @return AST instance of a binary boolean operation
     */
    @Override
    public T visitBinaryB(ExprlyParser.BinaryBContext ctx) throws UnsupportedOperationException {
        Expr p = (Expr) visit(ctx.expr(0));
        Expr q = (Expr) visit(ctx.expr(1));
        p.setReadOnly();
        q.setReadOnly();
        if ( p instanceof ExprList ) {
            p.setReadOnly();
            p = new ListCreate(BlocklyType.VOID, (ExprList) p, mkInlineProperty(ctx, "robLists_create_with"));
        }
        if ( q instanceof ExprList ) {
            q.setReadOnly();
            q = new ListCreate(BlocklyType.VOID, (ExprList) q, mkInlineProperty(ctx, "robLists_create_with"));
        }

        if ( ctx.op.getType() == ExprlyParser.AND ) {
            return (T) new Binary(Binary.Op.AND, p, q, "", mkInlineProperty(ctx, "logic_operation"));
        }
        if ( ctx.op.getType() == ExprlyParser.OR ) {
            return (T) new Binary(Binary.Op.OR, p, q, "", mkInlineProperty(ctx, "logic_operation"));
        }
        if ( ctx.op.getType() == ExprlyParser.EQUAL ) {
            return (T) new Binary(Binary.Op.EQ, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == ExprlyParser.NEQUAL ) {
            return (T) new Binary(Binary.Op.NEQ, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == ExprlyParser.GET ) {
            return (T) new Binary(Binary.Op.GT, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == ExprlyParser.LET ) {
            return (T) new Binary(Binary.Op.LT, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == ExprlyParser.GEQ ) {
            return (T) new Binary(Binary.Op.GTE, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == ExprlyParser.LEQ ) {
            return (T) new Binary(Binary.Op.LTE, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a binary number operation
     */
    @Override
    public T visitBinaryN(ExprlyParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr n0 = (Expr) visit(ctx.expr(0));
        Expr n1 = (Expr) visit(ctx.expr(1));
        n0.setReadOnly();
        n1.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.POW ) {
            List<Expr> args = new LinkedList();
            args.add(n0);
            args.add(n1);
            return (T) new MathPowerFunct(mkInlineProperty(ctx, "math_arithmetic"), FunctionNames.POWER, args);
        }
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return (T) new Binary(Binary.Op.ADD, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return (T) new Binary(Binary.Op.MINUS, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == ExprlyParser.MUL ) {
            return (T) new Binary(Binary.Op.MULTIPLY, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == ExprlyParser.DIV ) {
            return (T) new Binary(Binary.Op.DIVIDE, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == ExprlyParser.MOD ) {
            return (T) new Binary(Binary.Op.MOD, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a bool const
     */
    @Override
    public T visitBoolConstB(ExprlyParser.BoolConstBContext ctx) {
        return (T) new BoolConst(mkInlineProperty(ctx, "logic_boolean"), Boolean.parseBoolean(ctx.BOOL().getText().toLowerCase()));
    }

    /**
     * @return AST instance of a string const
     */
    @Override
    public T visitConstStr(ExprlyParser.ConstStrContext ctx) {
        String s = "";
        int c = ctx.getChildCount();
        for ( int i = 1; i < c - 1; i++ ) {
            s = s + ctx.getChild(i).toString();
            if ( i != c - 2 ) {
                s += " ";
            }
        }
        return (T) new StringConst(mkPropertyFromClass(ctx, StringConst.class), s);
    }

    /**
     * @return AST instance of a math const
     */
    @Override
    public T visitMathConst(ExprlyParser.MathConstContext ctx) {
        String c = ctx.CONST().getText();
        if ( c.equals("phi") ) {
            c = "golden_ratio";
        }
        if ( c.equals("inf") ) {
            c = "infinity";
        }
        if ( c.equals("sqrt_1_2") ) {
            c = "sqrt1_2";
        }
        return (T) new MathConst(mkPropertyFromClass(ctx, MathConst.class), MathConst.Const.get(c));
    }

    /**
     * @return AST instance of a num const
     */
    @Override
    public T visitIntConst(ExprlyParser.IntConstContext ctx) {
        return (T) new NumConst(null, ctx.INT().getText());
    }

    /**
     * @return AST instance of a function
     */
    @Override
    public T visitFunc(ExprlyParser.FuncContext ctx) throws UnsupportedOperationException {
        String f = ctx.FNAME().getText();
        List<Expr> args = new LinkedList();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr ast = (Expr) visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }
        for ( int i = 0; i < args.size(); i++ ) {
            if ( args.get(i) instanceof ExprList ) {
                ExprList e = (ExprList) args.get(i);
                e.setReadOnly();
                args.set(i, new ListCreate(BlocklyType.ARRAY, e, mkInlineProperty(ctx, "robLists_create_with")));
            }
        }
        return (T) mkExpr(f, args, ctx);
    }

    private T mkExpr(String f, List<Expr> args, ExprlyParser.FuncContext ctx) {
        ExprList list = new ExprList();
        for ( Expr e : args ) {
            e.setReadOnly();
            list.addExpr(e);
        }
        list.setReadOnly();
        Sig signature = FunctionNames.get(f).signature;
        int numberParams = signature.paramTypes.length;
        if ( signature.varargParamType == null && args.size() == numberParams ) {
            switch ( f ) {
                case "sin":
                case "cos":
                case "tan":
                case "asin":
                case "acos":
                case "atan":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_trig")));
                case "exp":
                case "sqrt":
                case "abs":
                case "log10":
                case "log":
                case "square":
                case "pow10":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_single")));
                case "round":
                case "ceil":
                case "floor":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_round")));
                case "randomInt":
                    return (T) new FunctionExpr(new MathRandomIntFunct(mkExternalProperty(ctx, "math_random_int"), args.get(0), args.get(1)));
                case "randomFloat":
                    return (T) new FunctionExpr(new MathRandomFloatFunct(mkExternalProperty(ctx, "math_random_float")));
                case "isEven":
                case "isOdd":
                case "isPrime":
                case "isWhole":
                case "isPositive":
                case "isNegative":
                    return (T) new FunctionExpr(new MathNumPropFunct(FunctionNames.get(f), args, mkPropertyFromClass(ctx, MathNumPropFunct.class)));
                case "isDivisibleBy":
                    return (T) new FunctionExpr(new MathNumPropFunct(FunctionNames.DIVISIBLE_BY, args, mkPropertyFromClass(ctx, MathNumPropFunct.class)));
                case "average":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.AVERAGE, args.get(0)));
                case "stddev":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "randomItem":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "sum":
                case "min":
                case "max":
                case "median":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "size":
                    return (T) new FunctionExpr(new LengthOfListFunct(mkExternalProperty(ctx, "robLists_length"), args.get(0)));
                case "indexOfFirst":
                    return (T) new FunctionExpr(new IndexOfFunct(mkExternalProperty(ctx, "robLists_indexOf"), IndexLocation.FIRST, args.get(0), args.get(1)));
                case "indexOfLast":
                    return (T) new FunctionExpr(new IndexOfFunct(mkExternalProperty(ctx, "robLists_indexOf"), IndexLocation.LAST, args.get(0), args.get(1)));
                case "get":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FROM_START, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getFromEnd":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FROM_END, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getFirst":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FIRST, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getLast":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.LAST, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getAndRemove":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FROM_START, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveFromEnd":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FROM_END, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveFirst":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FIRST, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveLast":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.LAST, args, null, mkExternalProperty(ctx, "robLists_getIndex")));
                case "createListWith":
                    return (T) new FunctionExpr(new ListRepeat(BlocklyType.VOID, args, mkExternalProperty(ctx, "robLists_repeat")));
                case "subList":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_START)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromIndexToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.LAST)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromIndexToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_END)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToIndex":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_START)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.LAST)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_END)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToIndex":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_START)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.LAST)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_END)), args, mkExternalProperty(ctx, "robLists_getSublist")));
                case "print":
                    return (T) new FunctionExpr(new TextPrintFunct(args, mkPropertyFromClass(ctx, TextPrintFunct.class)));
                case "createTextWith":
                    return (T) new FunctionExpr(new TextJoinFunct(list, mkInlineProperty(ctx, "robText_join")));
                case "constrain":
                    return (T) new FunctionExpr(new MathConstrainFunct(mkPropertyFromClass(ctx, MathConstrainFunct.class), args.get(0), args.get(1), args.get(2)));
                case "isEmpty":
                    return (T) new FunctionExpr(new IsListEmptyFunct(mkInlineProperty(ctx, "robLists_isEmpty"), args.get(0)));
                default:
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTcError("invalid function name " + f, false);
                    return (T) result;
            }
        } else if ( "getRGB".equals(f) ) {
            Expr empty = new EmptyExpr(BlocklyType.NUMBER_INT);
            if ( args.size() == 3 ) {
                return (T) new RgbColor(mkInlineProperty(ctx, "robColour_rgb"), args.get(0), args.get(1), args.get(2), empty);
            } else if ( args.size() == 4 ) {
                return (T) new RgbColor(mkInlineProperty(ctx, "robColour_rgb"), args.get(0), args.get(1), args.get(2), args.get(3));
            }
        } else if ( "createTextWith".equals(f) ) {
            return (T) new FunctionExpr(new TextJoinFunct(list, mkInlineProperty(ctx, "robText_join")));
        }
        Expr result = new EmptyExpr(BlocklyType.NOTHING);
        result.addTcError("number of parameters don't match", true);
        return (T) result;
    }

    /**
     * @return AST instance of a float const
     */
    @Override
    public T visitFloatConst(ExprlyParser.FloatConstContext ctx) {
        return (T) new NumConst(null, ctx.FLOAT().getText());
    }

    /**
     * @return AST instance of a unary boolean operation
     */
    @Override
    public T visitUnaryB(ExprlyParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr e = (Expr) visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return (T) new Unary(Unary.Op.NOT, e, mkInlineProperty(ctx, "logic_negate"));
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a unary number operation
     */
    @Override
    public T visitUnaryN(ExprlyParser.UnaryNContext ctx) throws UnsupportedOperationException {
        Expr e = (Expr) visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return (T) new Unary(Unary.Op.PLUS, e, mkPropertyFromClass(ctx, Unary.class));
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return (T) new Unary((Unary.Op.NEG), e, mkInlineProperty(ctx, "math_single"));
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a var
     */
    @Override
    public T visitVarName(ExprlyParser.VarNameContext ctx) {
        // By default we use VOID for the types of the variables, the type can be
        // checked later when compiling the program with the typechecker
        return (T) new Var(BlocklyType.VOID, ctx.VAR().getText(), mkPropertyFromClass(ctx, Var.class));
    }

    /**
     * @return AST instance of a list expression
     */
    @Override
    public T visitListExpr(ExprlyParser.ListExprContext ctx) {
        ExprList list = new ExprList();
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr e = (Expr) visit(expr);
            e.setReadOnly();
            list.addExpr(e);
        }
        list.setReadOnly();
        return (T) list;
    }

    /**
     * @return AST instance of the expression within a set of parentheses
     */
    @Override
    public T visitParenthese(ExprlyParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * @return AST instance of a connection const
     */
    @Override
    public T visitConn(ExprlyParser.ConnContext ctx) {
        return (T) new ConnectConst(mkPropertyFromClass(ctx, ConnectConst.class), ctx.op1.getText(), ctx.op0.getText());
    }

    /**
     * @return AST instance of the ternary op
     */
    @Override
    public T visitIfElseOp(ExprlyParser.IfElseOpContext ctx) {
        Expr q = (Expr) visit(ctx.expr(1));
        Expr r = (Expr) visit(ctx.expr(2));
        if ( q instanceof ExprList ) {
            q.setReadOnly();
            q = new ListCreate(BlocklyType.VOID, (ExprList) q, mkPropertyFromClass(ctx, ListCreate.class));
        }
        if ( r instanceof ExprList ) {
            r.setReadOnly();
            r = new ListCreate(BlocklyType.VOID, (ExprList) r, mkPropertyFromClass(ctx, ListCreate.class));
        }
        q.setReadOnly();
        r.setReadOnly();
        StmtList thenList = new StmtList();
        StmtList elseList = new StmtList();
        thenList.addStmt(new ExprStmt(q));
        elseList.addStmt(new ExprStmt(r));
        thenList.setReadOnly();
        elseList.setReadOnly();
        TernaryExpr ternaryExpr = new TernaryExpr(mkPropertyFromClass(ctx, TernaryExpr.class), (Expr) visit(ctx.expr(0)), (Expr) visit(ctx.expr(1)), (Expr) visit(ctx.expr(2)));
        return (T) ternaryExpr;
    }


    @Override
    public T visitStmtFunc(ExprlyParser.StmtFuncContext ctx) throws UnsupportedOperationException {
        String f = ctx.FNAMESTMT().getText();
        List<Expr> args = new LinkedList();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr ast = (Expr) visit(expr);

            //ExprStmt ast = (ExprStmt) visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }

        return (T) mkStmtExpr(f, args, ctx);
    }

    private T mkStmtExpr(String f, List<Expr> args, ExprlyParser.StmtFuncContext ctx) {
        List<Expr> argsStatements = new LinkedList();

        Sig signature = FunctionNames.get(f).signature;
        int numberParams = signature.paramTypes.length;

        if ( signature.varargParamType == null && args.size() == numberParams ) {
            switch ( f ) {
                case "set":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setFromEnd":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setFirst":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FIRST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setLast":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.LAST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insert":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertFromEnd":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertFirst":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FIRST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertLast":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.LAST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "remove":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_START, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeFromEnd":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_END, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeFirst":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FIRST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeLast":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.LAST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                default:
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTcError("invalid function name " + f, false);
                    return (T) statementList;
            }
        } else {
            StmtList statementList = new StmtList();
            statementList.setReadOnly();
            statementList.addTcError("number of parameters don't match", true);
            return (T) statementList;
        }
    }

    @Override
    public T visitBinaryVarAssign(ExprlyParser.BinaryVarAssignContext ctx) throws UnsupportedOperationException {

        Var n0 = new Var(BlocklyType.VOID, ctx.VAR().getText(), mkPropertyFromClass(ctx, Var.class));
        Expr n1 = (Expr) visit(ctx.expr());

        if ( ctx.op.getText().equals("SET") ) {
            return (T) new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
        }

        return (T) new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
    }

    @Override
    public T visitConditionStatementBlock(ExprlyParser.ConditionStatementBlockContext ctx) throws UnsupportedOperationException {
        List<Expr> conditionsList = new ArrayList<>();
        List<StmtList> listOfStatementList = new ArrayList<>();
        StmtList statementElseList = new StmtList();
        statementElseList.setReadOnly();

        for ( ExprlyParser.StatementListContext stmt : ctx.statementList() ) {
            StmtList statement = (StmtList) visit(stmt);
            statement.setReadOnly();
            listOfStatementList.add(statement);
        }

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr condition = (Expr) visit(expr);
            condition.setReadOnly();
            conditionsList.add(condition);
        }


        if ( ctx.op != null && ctx.op.getType() == ExprlyParser.ELSE ) {

            statementElseList = (StmtList) visit(ctx.statementList(ctx.statementList().size() - 1)); // Get the last statementList context
            statementElseList.setReadOnly();
            listOfStatementList.remove(ctx.statementList().size() - 1);
        }


        return (T) new IfStmt(mknullProperty(ctx, statementElseList.sl.size() >= 0 ? "robControls_ifElse" : "robControls_if"), conditionsList, listOfStatementList, statementElseList, statementElseList.sl.size(), listOfStatementList.size() - 1);

    }

    @Override
    public T visitRepeatStatement(ExprlyParser.RepeatStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        Expr exprBoolean = (Expr) visit(ctx.expr());
        exprBoolean.setReadOnly();
        if ( exprBoolean instanceof BoolConst ) {
            if ( ((BoolConst) exprBoolean).value == true ) {
                Expr trueExpr = new BoolConst(mkInlineProperty(ctx, "logic_boolean"), true);
                trueExpr.setReadOnly();
                statementList.setReadOnly();
                return (T) new RepeatStmt(RepeatStmt.Mode.FOREVER, trueExpr, statementList, mkExternalProperty(ctx, "robControls_loopForever"));
            }
        } else {
            return (T) new RepeatStmt(RepeatStmt.Mode.WHILE, exprBoolean, statementList, mkExternalProperty(ctx, "controls_whileUntil"));
        }
        return null;
    }

    @Override
    public T visitRepeatForEach(ExprlyParser.RepeatForEachContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        Phrase emptyExpression = new EmptyExpr(type);
        emptyExpression.setReadOnly();
        VarDeclaration var = new VarDeclaration(type, ctx.VAR().getText(), emptyExpression, false, false, mkExternalProperty(ctx, "robControls_forEach"));
        var.setReadOnly();

        Expr expr = (Expr) visit(ctx.expr());
        expr.setReadOnly();

        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        Binary exprBinary = new Binary(Binary.Op.IN, var, expr, "", mkExternalProperty(ctx, "robControls_forEach"));
        exprBinary.setReadOnly();

        return (T) new RepeatStmt(RepeatStmt.Mode.FOR_EACH, exprBinary, statementList, mkExternalProperty(ctx, "robControls_forEach"));

    }

    @Override
    public T visitWaitStatement(ExprlyParser.WaitStatementContext ctx) throws UnsupportedOperationException {
        //StmtList statementListWait = new StmtList();
        ExprList conditionsList = new ExprList();
        StmtList waitStatementList = new StmtList();

        Expr conditionWait = (Expr) visit(ctx.expr(0));
        conditionWait.setReadOnly();
        StmtList statementListWait = (StmtList) visit(ctx.statementList(0));
        statementListWait.setReadOnly();
        waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionWait, statementListWait, mkExternalProperty(ctx, "robControls_wait")));

        if ( ctx.op != null && ctx.op.getType() == ExprlyParser.ORWAITFOR ) {
            int numberOrWaitFor = ctx.expr().size() - 1;

            for ( int i = 0; i < numberOrWaitFor; i++ ) {
                Expr conditionOrWait = (Expr) visit(ctx.expr(i + 1));
                conditionOrWait.setReadOnly();
                StmtList statementListOrWait = new StmtList();
                statementListOrWait = (StmtList) visit(ctx.statementList(i + 1));
                statementListOrWait.setReadOnly();
                waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionOrWait, statementListOrWait, mkExternalProperty(ctx, "robControls_wait")));
            }
        }

        conditionsList.setReadOnly();
        waitStatementList.setReadOnly();
        return (T) new WaitStmt(mkExternalProperty(ctx, "robControls_wait"), waitStatementList);
    }

    @Override
    public T visitRepeatFor(ExprlyParser.RepeatForContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        if ( type.equalAsTypes(BlocklyType.NUMBER) ) {
            StmtList statementList = new StmtList();

            for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
                Stmt statement = (Stmt) visit(stmt);
                statement.setReadOnly();
                statementList.addStmt(statement);
            }
            statementList.setReadOnly();

            ExprList el = new ExprList();
            Var i = new Var(BlocklyType.NUMBER_INT, ctx.VAR().get(0).getText(), mkPropertyFromClass(ctx, Var.class));
            el.addExpr(i);
            for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
                Expr condition = (Expr) visit(expr);
                condition.setReadOnly();
                el.addExpr(condition);
            }

            el.setReadOnly();
            if ( el.el.get(1) instanceof NumConst && el.el.get(3) instanceof NumConst ) {
                if ( ((NumConst) el.el.get(1)).value.equals("0") && ((NumConst) el.el.get(3)).value.equals("1") ) {
                    return (T) new RepeatStmt(RepeatStmt.Mode.TIMES, el, statementList, mkExternalProperty(ctx, "controls_repeat_ext"));
                }
            }
            if ( ctx.op != null && ctx.op.getType() == ExprlyParser.STEP && el.el.get(3) instanceof Var ) {
                if ( ((Var) el.el.get(3)).name.equals(ctx.VAR().get(0).getText()) ) {
                    el.el.set(3, new NumConst(null, "1"));
                    return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
                } else {
                    Stmt resultError = new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
                    resultError.addTcError("Variable name should be the same for ++ notation", false);
                    return (T) resultError;
                }

            } else if ( ctx.op != null && ctx.op.getType() == ExprlyParser.STEP && !(el.el.get(3) instanceof Var) ) {
                Stmt resultError = new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
                resultError.addTcError("For ++ notation only variable is allowed", false);
                return (T) resultError;
            } else if ( ctx.op != null && ctx.op.getType() == ExprlyParser.SUB && el.el.get(3) instanceof NumConst ) {
                el.el.set(3, new Unary((Unary.Op.NEG), el.el.get(3), mkInlineProperty(ctx, "math_single")));
                return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
            } else {
                return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
            }
        } else {
            StmtList statementList = new StmtList();
            statementList.setReadOnly();
            statementList.addTcError("Invalid type for " + ctx.VAR(), true);
            return (T) statementList;
        }

    }

    @Override
    public T visitFlowControl(ExprlyParser.FlowControlContext ctx) throws UnsupportedOperationException {
        switch ( ctx.op.getType() ) {
            case ExprlyParser.BREAK:
                return (T) new StmtFlowCon(mkExternalProperty(ctx, "controls_flow_statements"), StmtFlowCon.Flow.BREAK);
            case ExprlyParser.CONTINUE:
                return (T) new StmtFlowCon(mkExternalProperty(ctx, "controls_flow_statements"), StmtFlowCon.Flow.CONTINUE);
            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTcError("Invalid flow control statement. Expected 'break' or 'continue' ", false);
                return (T) statementList;
        }
    }

    @Override
    public T visitWaitTimeStatement(ExprlyParser.WaitTimeStatementContext ctx) throws UnsupportedOperationException {
        return (T) new WaitTimeStmt(mkExternalProperty(ctx, "robControls_wait_time"), (Expr) visit(ctx.expr()));
    }

    @Override
    public T visitParamsMethod(ExprlyParser.ParamsMethodContext ctx) throws UnsupportedOperationException {

        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        Phrase emptyExpression = new EmptyExpr(type);
        emptyExpression.setReadOnly();
        VarDeclaration var = new VarDeclaration(type, ctx.VAR().getText(), emptyExpression, false, false, mkExternalProperty(ctx, "robControls_forEach"));
        var.setReadOnly();

        return (T) var;
    }

    @Override
    public T visitFuncUser(ExprlyParser.FuncUserContext ctx) throws UnsupportedOperationException {

        String methodName = ctx.FUNCTIONNAME().getText();
        ExprList parameters = new ExprList();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr param = (Expr) visit(expr);
            parameters.addExpr(param);
            param.setReadOnly();
        }
        parameters.setReadOnly();

        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();


        if ( ctx.op != null && ctx.op.getType() == ExprlyParser.RETURN ) {
            BlocklyType returnType = BlocklyType.get(ctx.PRIMITIVETYPE().getText());
            if ( ctx.VAR() != null ) {
                Var returnVar = new Var(BlocklyType.VOID, ctx.VAR().getText(), mkPropertyFromClass(ctx, Var.class));
                return (T) new MethodReturn(methodName, parameters, statementList, returnType, returnVar, mkExternalProperty(ctx, "robProcedures_defreturn"));
            } else {
                Expr returnExpr = parameters.el.get(parameters.el.size() - 1);
                parameters.delExpr(parameters.el.get(parameters.el.size() - 1), parameters.el.size() - 1);
                return (T) new MethodReturn(methodName, parameters, statementList, returnType, returnExpr, mkExternalProperty(ctx, "robProcedures_defreturn"));
            }

        }
        return null;
    }

    @Override
    public T visitMainFunc(ExprlyParser.MainFuncContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();
        return (T) statementList;
    }


    @Override
    public T visitVariableDeclaration(ExprlyParser.VariableDeclarationContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.get(typeAsString);
        Expr expr = (Expr) visit(ctx.expr());
        VarDeclaration var = new VarDeclaration(type, ctx.VAR().getText(), expr, true, true, mkExternalProperty(ctx, "robGlobalVariables_declare"));
        var.setReadOnly();
        return (T) var;
    }

    @Override
    public T visitProgram(ExprlyParser.ProgramContext ctx) {
        List<Phrase> phrases = new ArrayList<>();
        StmtList variablesDec = new StmtList();

        for ( ExprlyParser.DeclarationContext decla : ctx.declaration() ) {
            ExprStmt statement = new ExprStmt((Expr) visit(decla));
            variablesDec.addStmt(statement);
        }
        variablesDec.setReadOnly();
        MainTask main = new MainTask(mkExternalProperty(ctx, "robControls_start"), variablesDec, "TRUE", null);
        phrases.add(main);

        StmtList statements = (StmtList) visitMainFunc((ExprlyParser.MainFuncContext) ctx.mainBlock());
        phrases.add(statements);
        //return (T) main;
        return (T) phrases;
    }

    private static BlocklyProperties mknullProperty(ParserRuleContext ctx, String type) {
        //TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
        BlocklyRegion br = new BlocklyRegion(false, false, null, null, null, true, null, null, null);
        return new BlocklyProperties(type, "1", br, null);
    }

    @Override
    public T visitChildren(RuleNode node) {
        Expr result = (Expr) super.visitChildren(node);
        result.setReadOnly();
        return (T) result;
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private <T> BlocklyProperties mkPropertyFromClass(ParserRuleContext ctx, Class<T> clazz) {
        String[] blocklyNames = AnnotationHelper.getBlocklyNamesOfAstClass(clazz);
        if ( blocklyNames.length != 1 ) {
            throw new DbcException("rework that! Too many blockly names to generate an ast object, that can be regenerated as XML");
        }
        return mkExternalProperty(ctx, blocklyNames[0]);
    }
}




