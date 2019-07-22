package de.fhg.iais.roberta.syntax.lang.expr.eval.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
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
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
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
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class ExprlyAST<V> extends ExprlyBaseVisitor<Expr<V>> {

    /**
     * @return AST instance of a color
     */
    @Override
    public ColorConst<V> visitCol(ExprlyParser.ColContext ctx) {
        return ColorConst.make(ctx.COLOR().getText());
    }

    /**
     * @return AST instance of a binary boolean operation
     */
    @Override
    public Binary<V> visitBinaryB(@NotNull ExprlyParser.BinaryBContext ctx) throws UnsupportedOperationException {
        Expr<V> p = visit(ctx.expr(0));
        Expr<V> q = visit(ctx.expr(1));
        if ( ctx.op.getType() == ExprlyParser.AND ) {
            return Binary.make(Binary.Op.AND, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.OR ) {
            return Binary.make(Binary.Op.OR, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.EQUAL ) {
            return Binary.make(Binary.Op.EQ, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.NEQUAL ) {
            return Binary.make(Binary.Op.NEQ, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.GET ) {
            return Binary.make(Binary.Op.GT, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.LET ) {
            return Binary.make(Binary.Op.LT, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.GEQ ) {
            return Binary.make(Binary.Op.GTE, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.LEQ ) {
            return Binary.make(Binary.Op.LTE, p, q, "");
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a binary number operation
     */
    @Override
    public Expr<V> visitBinaryN(@NotNull ExprlyParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr<V> n0 = visit(ctx.expr(0));
        Expr<V> n1 = visit(ctx.expr(1));
        if ( ctx.op.getType() == ExprlyParser.POW ) {
            List<Expr<V>> args = new LinkedList<Expr<V>>();
            args.add(n0);
            args.add(n1);
            return MathPowerFunct.make("power", args);
        }
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return Binary.make(Binary.Op.ADD, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Binary.make(Binary.Op.MINUS, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.MUL ) {
            return Binary.make(Binary.Op.MULTIPLY, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.DIV ) {
            return Binary.make(Binary.Op.DIVIDE, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.MOD ) {
            return Binary.make(Binary.Op.MOD, n0, n1, "");
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a bool const
     */
    @Override
    public BoolConst<V> visitBoolConstB(@NotNull ExprlyParser.BoolConstBContext ctx) {
        return BoolConst.make(ctx.BOOL().getText());
    }

    /**
     * @return AST instance of a string const
     */
    @Override
    public StringConst<V> visitConstStr(@NotNull ExprlyParser.ConstStrContext ctx) {
        String s = "";
        int c = ctx.getChildCount();
        for ( int i = 1; i < c - 1; i++ ) {
            s = s + ctx.getChild(i).toString();
            if ( i != c - 2 ) {
                s += " ";
            }
        }
        return StringConst.make(s);
    }

    /**
     * @return AST instance of a math const
     */
    @Override
    public MathConst<V> visitMathConst(@NotNull ExprlyParser.MathConstContext ctx) {
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
        return MathConst.make(c);
    }

    /**
     * @return AST instance of a num const
     */
    @Override
    public NumConst<V> visitIntConst(@NotNull ExprlyParser.IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    /**
     * @return AST instance of a function
     */
    @Override
    public Expr<V> visitFunc(@NotNull ExprlyParser.FuncContext ctx) throws UnsupportedOperationException {

        String f = ctx.FNAME().getText();
        List<Expr<V>> args = new LinkedList<Expr<V>>();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            args.add(visit(expr));
        }

        for ( int i = 0; i < args.size(); i++ ) {
            if ( args.get(i) instanceof ExprList<?> ) {
                ExprList<V> e = (ExprList<V>) args.get(i);
                e.setReadOnly();
                args.set(i, ListCreate.make(BlocklyType.ARRAY, e));
            }
        }

        if ( f.equals("randInt") ) {
            return FunctionExpr.make(MathRandomIntFunct.make(args));
        }
        if ( f.equals("randFloat") ) {
            return FunctionExpr.make(MathRandomFloatFunct.make(args.size() > 0));
        }
        if ( f.equals("sqrt") ) {
            f = "root";
        }
        if ( f.equals("floor") ) {
            f = "rounddown";
        }
        if ( f.equals("ceil") ) {
            f = "roundup";
        }
        if ( f.equals("isEven") || f.equals("isOdd") || f.equals("isPrime") || f.equals("isWhole") ) {
            f = f.substring(2);
            return FunctionExpr.make(MathNumPropFunct.make(f, args));
        }
        if ( f.equals("avg") ) {
            f = "average";
            return FunctionExpr.make(MathOnListFunct.make(f, args));
        }
        if ( f.equals("median") ) {
            return FunctionExpr.make(MathOnListFunct.make(f, args));
        }
        if ( f.equals("sd") ) {
            f = "std_dev";
            return FunctionExpr.make(MathOnListFunct.make(f, args));
        }
        if ( f.equals("min") || f.equals("max") || f.equals("sum") ) {
            return FunctionExpr.make(MathOnListFunct.make(f, args));
        }
        if ( f.equals("lengthOf") ) {
            return FunctionExpr.make(LengthOfIsEmptyFunct.make("lists_length", args));
        }

        if ( f.contains("setIndex") ) {
            if ( f.equals("setIndex") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.SET, IndexLocation.FROM_START, args));
            }
            if ( f.equals("setIndexFromEnd") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.SET, IndexLocation.FROM_END, args));
            }
            if ( f.equals("setIndexFirst") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.SET, IndexLocation.FIRST, args));
            }
            if ( f.equals("setIndexLast") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.SET, IndexLocation.LAST, args));
            }
        }
        if ( f.contains("insertIndex") ) {
            if ( f.equals("insertIndex") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.INSERT, IndexLocation.FROM_START, args));
            }
            if ( f.equals("insertIndexFromEnd") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.INSERT, IndexLocation.FROM_END, args));
            }
            if ( f.equals("insertIndexFirst") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.INSERT, IndexLocation.FIRST, args));
            }
            if ( f.equals("insertIndexLast") ) {
                return FunctionExpr.make(ListSetIndex.make(ListElementOperations.INSERT, IndexLocation.LAST, args));
            }
        }
        if ( f.contains("getIndex") ) {
            if ( f.equals("getIndex") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET, IndexLocation.FROM_START, args, "VOID"));
            }
            if ( f.equals("getIndexFromEnd") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET, IndexLocation.FROM_END, args, "VOID"));
            }
            if ( f.equals("getIndexFirst") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET, IndexLocation.FIRST, args, "VOID"));
            }
            if ( f.equals("getIndexLast") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET, IndexLocation.LAST, args, "VOID"));
            }
        }
        if ( f.contains("getAndRemoveIndex") ) {
            if ( f.equals("getAndRemoveIndex") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET_REMOVE, IndexLocation.FROM_START, args, "VOID"));
            }
            if ( f.equals("getAndRemoveIndexFromEnd") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET_REMOVE, IndexLocation.FROM_END, args, "VOID"));
            }
            if ( f.equals("getAndRemoveIndexFirst") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET_REMOVE, IndexLocation.FIRST, args, "VOID"));
            }
            if ( f.equals("getAndRemoveIndexLast") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.GET_REMOVE, IndexLocation.LAST, args, "VOID"));
            }
        }
        if ( f.contains("removeIndex") ) {
            if ( f.equals("removeIndex") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.REMOVE, IndexLocation.FROM_START, args, "VOID"));
            }
            if ( f.equals("removeIndexFromEnd") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.REMOVE, IndexLocation.FROM_END, args, "VOID"));
            }
            if ( f.equals("removeIndexFirst") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.REMOVE, IndexLocation.FIRST, args, "VOID"));
            }
            if ( f.equals("removeIndexLast") ) {
                return FunctionExpr.make(ListGetIndex.make(ListElementOperations.REMOVE, IndexLocation.LAST, args, "VOID"));
            }
        }
        if ( f.equals("repeatList") ) {
            return FunctionExpr.make(ListRepeat.make(BlocklyType.VOID, args));
        }
        if ( f.contains("subList") ) {

            if ( f.equals("subList") ) {
                return FunctionExpr
                    .make(
                        GetSubFunct
                            .make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_START)), args));
            }
            if ( f.equals("subListFromIndexToLast") ) {
                return FunctionExpr
                    .make(GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.LAST)), args));
            }
            if ( f.equals("subListFromIndexToEnd") ) {
                return FunctionExpr
                    .make(
                        GetSubFunct
                            .make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_END)), args));
            }
            if ( f.equals("subListFromFirstToIndex") ) {
                return FunctionExpr
                    .make(
                        GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_START)), args));
            }
            if ( f.equals("subListFromFirstToLast") ) {
                return FunctionExpr
                    .make(GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.LAST)), args));
            }
            if ( f.equals("subListFromFirstToEnd") ) {
                return FunctionExpr
                    .make(GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_END)), args));
            }
            if ( f.equals("subListFromEndToIndex") ) {
                return FunctionExpr
                    .make(
                        GetSubFunct
                            .make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_START)), args));
            }
            if ( f.equals("subListFromEndToLast") ) {
                return FunctionExpr
                    .make(GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.LAST)), args));
            }
            if ( f.equals("subListFromEndToEnd") ) {
                return FunctionExpr
                    .make(
                        GetSubFunct.make(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_END)), args));
            }
        }
        if ( f.equals("print") ) {
            return FunctionExpr.make(TextPrintFunct.make(args));
        }
        if ( f.equals("append") ) {
            ExprList<V> args0 = ExprList.make();
            for ( Expr<V> e : args ) {
                args0.addExpr(e);
            }
            return FunctionExpr.make(TextJoinFunct.make(args0));

        }
        if ( f.equals("constrain") ) {
            return FunctionExpr.make(MathConstrainFunct.make(args));
        }
        if ( f.equals("isEmpty") ) {
            return FunctionExpr.make(LengthOfIsEmptyFunct.make("LIST_IS_EMPTY", args));
        }
        if ( f.equals("getRGB") ) {
            if ( args.size() == 3 ) {
                return RgbColor.make(args.get(0), args.get(1), args.get(2), NumConst.make(0));
            } else if ( args.size() == 4 ) {
                return RgbColor.make(args.get(0), args.get(1), args.get(2), args.get(3));
            } else {
                return NullConst.make();
            }
        }
        try {
            return FunctionExpr.make(MathSingleFunct.make(f, args));
        } catch ( Exception e ) {
            throw new UnsupportedOperationException("Invalid function name: " + f);
        }
    }

    /**
     * @return AST instance of a float const
     */
    @Override
    public NumConst<V> visitFloatConst(@NotNull ExprlyParser.FloatConstContext ctx) {
        return NumConst.make(ctx.FLOAT().getText());
    }

    /**
     * @return AST instance of a unary boolean operation
     */
    @Override
    public Unary<V> visitUnaryB(@NotNull ExprlyParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr<V> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return Unary.make(Unary.Op.NOT, e);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a unary number operation
     */
    @Override
    public Unary<V> visitUnaryN(@NotNull ExprlyParser.UnaryNContext ctx) throws UnsupportedOperationException {
        Expr<V> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return Unary.make(Unary.Op.PLUS, e);
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Unary.make(Unary.Op.NEG, e);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a var
     */
    @Override
    public Var<V> visitVarName(@NotNull ExprlyParser.VarNameContext ctx) {
        // By default we use VOID for the types of the variables, the type can be
        // checked later when compiling the program with the typechecker
        return Var.make("VOID", ctx.VAR().getText());
    }

    /**
     * @return AST instance of a list expression
     */
    @Override
    public ExprList<V> visitListExpr(@NotNull ExprlyParser.ListExprContext ctx) {
        ExprList<V> list = ExprList.make();
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * @return AST instance of the expression within a set of parentheses
     */
    @Override
    public Expr<V> visitParenthese(@NotNull ExprlyParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * @return AST instance of a connection const
     */
    @Override
    public ConnectConst<V> visitConn(@NotNull ExprlyParser.ConnContext ctx) {
        return ConnectConst.make(ctx.op0.getText(), ctx.op1.getText());
    }

}
