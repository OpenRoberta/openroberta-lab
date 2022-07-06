package de.fhg.iais.roberta.exprEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.RuleNode;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
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
import de.fhg.iais.roberta.syntax.lang.expr.MathConst.Const;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
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
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

public class ExprlyVisitor<V> extends ExprlyBaseVisitor<Expr<V>> {

    private static final BlocklyProperties BCMAKE = BlocklyProperties.make("Exprly", "1");

    /**
     * @return AST instance for the whole expression
     */
    @Override
    public Expr<V> visitExpression(ExpressionContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * @return AST instance of null const
     */
    @Override
    public NullConst<V> visitNullConst(ExprlyParser.NullConstContext ctx) {
        return new NullConst<V>(BCMAKE);
    }

    /**
     * @return AST instance of a color
     */
    @Override
    public ColorConst<V> visitCol(ExprlyParser.ColContext ctx) {
        return new ColorConst<>(BCMAKE, ctx.COLOR().getText());
    }

    /**
     * @return AST instance of a binary boolean operation
     */
    @Override
    public Binary<V> visitBinaryB(ExprlyParser.BinaryBContext ctx) throws UnsupportedOperationException {
        Expr<V> p = visit(ctx.expr(0));
        Expr<V> q = visit(ctx.expr(1));
        p.setReadOnly();
        q.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.AND ) {
            return new Binary<>(Binary.Op.AND, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.OR ) {
            return new Binary<>(Binary.Op.OR, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.EQUAL ) {
            return new Binary<>(Binary.Op.EQ, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.NEQUAL ) {
            return new Binary<>(Binary.Op.NEQ, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.GET ) {
            return new Binary<>(Binary.Op.GT, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.LET ) {
            return new Binary<>(Binary.Op.LT, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.GEQ ) {
            return new Binary<>(Binary.Op.GTE, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.LEQ ) {
            return new Binary<>(Binary.Op.LTE, p, q, "", BlocklyProperties.make("BINARY", "1"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a binary number operation
     */
    @Override
    public Expr<V> visitBinaryN(ExprlyParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr<V> n0 = visit(ctx.expr(0));
        Expr<V> n1 = visit(ctx.expr(1));
        n0.setReadOnly();
        n1.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.POW ) {
            List<Expr<V>> args = new LinkedList<>();
            args.add(n0);
            args.add(n1);
            return new MathPowerFunct<V>(BCMAKE, FunctionNames.POWER, args);
        }
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return new Binary<>(Binary.Op.ADD, n0, n1, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return new Binary<>(Binary.Op.MINUS, n0, n1, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.MUL ) {
            return new Binary<>(Binary.Op.MULTIPLY, n0, n1, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.DIV ) {
            return new Binary<>(Binary.Op.DIVIDE, n0, n1, "", BlocklyProperties.make("BINARY", "1"));
        }
        if ( ctx.op.getType() == ExprlyParser.MOD ) {
            return new Binary<>(Binary.Op.MOD, n0, n1, "", BlocklyProperties.make("BINARY", "1"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a bool const
     */
    @Override
    public BoolConst<V> visitBoolConstB(ExprlyParser.BoolConstBContext ctx) {
        return new BoolConst<>(BCMAKE, Boolean.parseBoolean(ctx.BOOL().getText().toLowerCase()));
    }

    /**
     * @return AST instance of a string const
     */
    @Override
    public StringConst<V> visitConstStr(ExprlyParser.ConstStrContext ctx) {
        String s = "";
        int c = ctx.getChildCount();
        for ( int i = 1; i < c - 1; i++ ) {
            s = s + ctx.getChild(i).toString();
            if ( i != c - 2 ) {
                s += " ";
            }
        }
        return new StringConst<V>(BCMAKE, s);
    }

    /**
     * @return AST instance of a math const
     */
    @Override
    public MathConst<V> visitMathConst(ExprlyParser.MathConstContext ctx) {
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
        return new MathConst<V>(BCMAKE, Const.get(c));
    }

    /**
     * @return AST instance of a num const
     */
    @Override
    public NumConst<V> visitIntConst(ExprlyParser.IntConstContext ctx) {
        return new NumConst<>(null, ctx.INT().getText());
    }

    /**
     * @return AST instance of a function
     */
    @Override
    public Expr<V> visitFunc(ExprlyParser.FuncContext ctx) throws UnsupportedOperationException {

        String f = ctx.FNAME().getText();
        List<Expr<V>> args = new LinkedList<>();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr<V> ast = visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }

        for ( int i = 0; i < args.size(); i++ ) {
            if ( args.get(i) instanceof ExprList<?> ) {
                ExprList<V> e = (ExprList<V>) args.get(i);
                e.setReadOnly();
                args.set(i, new ListCreate<V>(BlocklyType.ARRAY, e, BCMAKE));
            }
        }

        // check the function name and return the corresponfing one
        if ( f.equals("randInt") ) {
            return new FunctionExpr<V>(new MathRandomIntFunct<V>(args, BCMAKE));
        }
        if ( f.equals("randFloat") ) {
            return new FunctionExpr<V>(new MathRandomFloatFunct<V>(BCMAKE));
        }
        if ( f.equals("sqrt") ) {
            f = "root";
        }
        if ( f.equals("isEven") || f.equals("isOdd") || f.equals("isPrime") || f.equals("isWhole") || f.equals("isPositive") || f.equals("isNegative") ) {
            f = f.substring(2);
            return new FunctionExpr<V>(new MathNumPropFunct<V>(FunctionNames.get(f), args, BCMAKE));
        }
        if ( f.equals("isDivisibleBy") ) {
            return new FunctionExpr<V>(new MathNumPropFunct<V>(FunctionNames.DIVISIBLE_BY, args, BCMAKE));
        }
        if ( f.equals("avg") ) {
            f = "average";
            return new FunctionExpr<V>(new MathOnListFunct<V>(FunctionNames.get(f), args, BCMAKE));
        }
        if ( f.equals("sd") ) {
            f = "std_dev";
            return new FunctionExpr<V>(new MathOnListFunct<V>(FunctionNames.get(f), args, BCMAKE));
        }
        if ( f.equals("randItem") ) {
            f = "random";
            return new FunctionExpr<V>(new MathOnListFunct<V>(FunctionNames.get(f), args, BCMAKE));
        }
        if ( f.equals("min") || f.equals("max") || f.equals("sum") || f.equals("median") ) {
            return new FunctionExpr<V>(new MathOnListFunct<V>(FunctionNames.get(f), args, BCMAKE));
        }
        if ( f.equals("lengthOf") ) {
            return new FunctionExpr<V>(new LengthOfIsEmptyFunct<V>(FunctionNames.LIST_LENGTH, args, BCMAKE));
        }
        if ( f.equals("indexOfFirst") ) {
            return new FunctionExpr<V>(new IndexOfFunct<V>(IndexLocation.FIRST, args, BCMAKE));
        }
        if ( f.equals("indexOfLast") ) {
            return new FunctionExpr<V>(new IndexOfFunct<V>(IndexLocation.LAST, args, BCMAKE));
        }
        if ( f.contains("setIndex") ) {
            if ( f.equals("setIndex") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.SET, IndexLocation.FROM_START, args, BCMAKE));
            }
            if ( f.equals("setIndexFromEnd") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.SET, IndexLocation.FROM_END, args, BCMAKE));
            }
            if ( f.equals("setIndexFirst") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.SET, IndexLocation.FIRST, args, BCMAKE));
            }
            if ( f.equals("setIndexLast") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.SET, IndexLocation.LAST, args, BCMAKE));
            }
        }
        if ( f.contains("insertIndex") ) {
            if ( f.equals("insertIndex") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.INSERT, IndexLocation.FROM_START, args, BCMAKE));
            }
            if ( f.equals("insertIndexFromEnd") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.INSERT, IndexLocation.FROM_END, args, BCMAKE));
            }
            if ( f.equals("insertIndexFirst") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.INSERT, IndexLocation.FIRST, args, BCMAKE));
            }
            if ( f.equals("insertIndexLast") ) {
                return new FunctionExpr<V>(new ListSetIndex<V>(ListElementOperations.INSERT, IndexLocation.LAST, args, BCMAKE));
            }
        }
        if ( f.contains("getIndex") ) {
            if ( f.equals("getIndex") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET, IndexLocation.FROM_START, args, "VOID", BCMAKE));
            }
            if ( f.equals("getIndexFromEnd") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET, IndexLocation.FROM_END, args, "VOID", BCMAKE));
            }
            if ( f.equals("getIndexFirst") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET, IndexLocation.FIRST, args, "VOID", BCMAKE));
            }
            if ( f.equals("getIndexLast") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET, IndexLocation.LAST, args, "VOID", BCMAKE));
            }
        }
        if ( f.contains("getAndRemoveIndex") ) {
            if ( f.equals("getAndRemoveIndex") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET_REMOVE, IndexLocation.FROM_START, args, "VOID", BCMAKE));
            }
            if ( f.equals("getAndRemoveIndexFromEnd") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET_REMOVE, IndexLocation.FROM_END, args, "VOID", BCMAKE));
            }
            if ( f.equals("getAndRemoveIndexFirst") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET_REMOVE, IndexLocation.FIRST, args, "VOID", BCMAKE));
            }
            if ( f.equals("getAndRemoveIndexLast") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.GET_REMOVE, IndexLocation.LAST, args, "VOID", BCMAKE));
            }
        }
        if ( f.contains("removeIndex") ) {
            if ( f.equals("removeIndex") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.REMOVE, IndexLocation.FROM_START, args, "VOID", BCMAKE));
            }
            if ( f.equals("removeIndexFromEnd") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.REMOVE, IndexLocation.FROM_END, args, "VOID", BCMAKE));
            }
            if ( f.equals("removeIndexFirst") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.REMOVE, IndexLocation.FIRST, args, "VOID", BCMAKE));
            }
            if ( f.equals("removeIndexLast") ) {
                return new FunctionExpr<V>(new ListGetIndex<V>(ListElementOperations.REMOVE, IndexLocation.LAST, args, "VOID", BCMAKE));
            }
        }
        if ( f.equals("repeatList") ) {
            return new FunctionExpr<V>(new ListRepeat<V>(BlocklyType.VOID, args, BCMAKE));
        }
        if ( f.contains("subList") ) {

            if ( f.equals("subList") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_START)), args, BCMAKE));
            }
            if ( f.equals("subListFromIndexToLast") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.LAST)), args, BCMAKE));
            }
            if ( f.equals("subListFromIndexToEnd") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_END)), args, BCMAKE));
            }
            if ( f.equals("subListFromFirstToIndex") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_START)), args, BCMAKE));
            }
            if ( f.equals("subListFromFirstToLast") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.LAST)), args, BCMAKE));
            }
            if ( f.equals("subListFromFirstToEnd") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_END)), args, BCMAKE));
            }
            if ( f.equals("subListFromEndToIndex") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_START)), args, BCMAKE));
            }
            if ( f.equals("subListFromEndToLast") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.LAST)), args, BCMAKE));
            }
            if ( f.equals("subListFromEndToEnd") ) {
                return new FunctionExpr<V>(new GetSubFunct<V>(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_END)), args, BCMAKE));
            }
        }
        if ( f.equals("print") ) {
            return new FunctionExpr<V>(new TextPrintFunct<V>(args, BCMAKE));
        }
        if ( f.equals("createTextWith") ) {
            ExprList<V> args0 = new ExprList<V>();
            for ( Expr<V> e : args ) {
                e.setReadOnly();
                args0.addExpr(e);
            }
            args0.setReadOnly();
            return new FunctionExpr<V>(new TextJoinFunct<V>(args0, BCMAKE));

        }
        if ( f.equals("constrain") ) {
            return new FunctionExpr<V>(new MathConstrainFunct<V>(args, BCMAKE));
        }
        if ( f.equals("isEmpty") ) {
            return new FunctionExpr<V>(new LengthOfIsEmptyFunct<V>(FunctionNames.LIST_IS_EMPTY, args, BCMAKE));
        }
        if ( f.equals("getRGB") ) {
            if ( args.size() == 3 ) {
                return (Expr<V>) (RgbColor<V>) new RgbColor(BCMAKE, args.get(0), args.get(1), args.get(2), new EmptyExpr<V>(BlocklyType.NUMBER_INT));
            } else if ( args.size() == 4 ) {
                return (Expr<V>) (RgbColor<V>) new RgbColor(BCMAKE, args.get(0), args.get(1), args.get(2), args.get(3));
            } else {
                Expr<V> empty = new EmptyExpr<V>(BlocklyType.NUMBER_INT);
                return (Expr<V>) (RgbColor<V>) new RgbColor(BCMAKE, empty, empty, empty, empty);
            }
        }
        try {
            return new FunctionExpr<V>(new MathSingleFunct<V>(FunctionNames.get(f), args, BCMAKE));
        } catch ( Exception e ) {
            throw new UnsupportedOperationException("Invalid function name: " + f);
        }
    }

    /**
     * @return AST instance of a float const
     */
    @Override
    public NumConst<V> visitFloatConst(ExprlyParser.FloatConstContext ctx) {
        return new NumConst<>(null, ctx.FLOAT().getText());
    }

    /**
     * @return AST instance of a unary boolean operation
     */
    @Override
    public Unary<V> visitUnaryB(ExprlyParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr<V> e = visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return new Unary<V>(Unary.Op.NOT, e, BCMAKE);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a unary number operation
     */
    @Override
    public Unary<V> visitUnaryN(ExprlyParser.UnaryNContext ctx) throws UnsupportedOperationException {
        Expr<V> e = visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return new Unary<V>(Unary.Op.PLUS, e, BCMAKE);
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return new Unary<V>(Unary.Op.NEG, e, BCMAKE);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a var
     */
    @Override
    public Var<V> visitVarName(ExprlyParser.VarNameContext ctx) {
        // By default we use VOID for the types of the variables, the type can be
        // checked later when compiling the program with the typechecker
        return new Var<>(BlocklyType.VOID, ctx.VAR().getText(), BCMAKE);
    }

    /**
     * @return AST instance of a list expression
     */
    @Override
    public ExprList<V> visitListExpr(ExprlyParser.ListExprContext ctx) {
        ExprList<V> list = new ExprList<V>();
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr<V> e = visit(expr);
            e.setReadOnly();
            list.addExpr(e);
        }
        list.setReadOnly();
        return list;
    }

    /**
     * @return AST instance of the expression within a set of parentheses
     */
    @Override
    public Expr<V> visitParenthese(ExprlyParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * @return AST instance of a connection const
     */
    @Override
    public ConnectConst<V> visitConn(ExprlyParser.ConnContext ctx) {
        return new ConnectConst<V>(BCMAKE, ctx.op1.getText(), ctx.op0.getText());
    }

    /**
     * @return AST instance of the ternary op
     */
    @Override
    public Expr<V> visitIfElseOp(ExprlyParser.IfElseOpContext ctx) {
        Expr<V> q = visit(ctx.expr(1));
        Expr<V> r = visit(ctx.expr(2));
        if ( q instanceof ExprList<?> ) {
            q.setReadOnly();
            q = new ListCreate<V>(BlocklyType.VOID, (ExprList<V>) q, BCMAKE);
        }
        if ( r instanceof ExprList<?> ) {
            r.setReadOnly();
            r = new ListCreate<V>(BlocklyType.VOID, (ExprList<V>) r, BCMAKE);
        }
        q.setReadOnly();
        r.setReadOnly();
        StmtList<V> thenList = new StmtList<V>();
        StmtList<V> elseList = new StmtList<V>();
        thenList.addStmt(new ExprStmt<V>(q));
        elseList.addStmt(new ExprStmt<V>(r));
        thenList.setReadOnly();
        elseList.setReadOnly();
        TernaryExpr<V> ternaryExpr = new TernaryExpr<V>(BCMAKE, visit(ctx.expr(0)), visit(ctx.expr(1)), visit(ctx.expr(2)));
        return ternaryExpr;
    }

    @Override
    public Expr<V> visitChildren(RuleNode node) {
        Expr<V> result = super.visitChildren(node);
        result.setReadOnly();
        return result;
    }
}
