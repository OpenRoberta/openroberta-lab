package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

public class ExprlyAST<V> extends ExprlyBaseVisitor<Expr<V>> {

    @Override
    public ColorConst<V> visitCol(ExprlyParser.ColContext ctx) {
        return ColorConst.make(ctx.COLOR().getText());
    }

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

    @Override
    public Expr<V> visitBinaryN(@NotNull ExprlyParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr<V> n0 = visit(ctx.expr(0));
        Expr<V> n1 = visit(ctx.expr(1));
        if ( ctx.op.getType() == ExprlyParser.POW ) {
            List<Expr<V>> args = new LinkedList<Expr<V>>();
            args.add(n0);
            args.add(n1);
            return FunctionExpr.make(MathSingleFunct.make("power", args));
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

    @Override
    public BoolConst<V> visitBoolConstB(@NotNull ExprlyParser.BoolConstBContext ctx) {
        return BoolConst.make(ctx.BOOL().getText());
    }

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

    @Override
    public RgbColor<V> visitRGB(@NotNull ExprlyParser.RGBContext ctx) throws IllegalArgumentException {
        if ( 0 > Integer.parseInt(ctx.r.getText())
            || Integer.parseInt(ctx.r.getText()) > 255
            || 0 > Integer.parseInt(ctx.g.getText())
            || Integer.parseInt(ctx.g.getText()) > 255
            || 0 > Integer.parseInt(ctx.b.getText())
            || Integer.parseInt(ctx.b.getText()) > 255
            || 0 > Integer.parseInt(ctx.a.getText())
            || Integer.parseInt(ctx.a.getText()) > 255 ) {
            throw new IllegalArgumentException("Invalid RGB value, all values must be withn 0 and 255");
        }
        return RgbColor.make(ctx.r.getText(), ctx.g.getText(), ctx.b.getText(), ctx.a.getText());
    }

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

    @Override
    public NumConst<V> visitIntConst(@NotNull ExprlyParser.IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    @Override
    public FunctionExpr<V> visitFunc(@NotNull ExprlyParser.FuncContext ctx) throws UnsupportedOperationException {

        String f = ctx.FNAME().getText();
        List<Expr<V>> args = new LinkedList<Expr<V>>();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            args.add(visit(expr));
        }

        if ( f.equals("randInt") ) {
            return FunctionExpr.make(MathRandomIntFunct.make(args));
        }
        if ( f.equals("randFloat") ) {
            if ( args.size() > 0 ) {
                throw new UnsupportedOperationException("randFloat function takes 0 arguments");
            }
            return FunctionExpr.make(MathRandomFloatFunct.make());
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
        try {
            return FunctionExpr.make(MathSingleFunct.make(f, args));
        } catch ( Exception e ) {
            throw new UnsupportedOperationException("Invalid function name: " + f);
        }
    }

    @Override
    public NumConst<V> visitFloatConst(@NotNull ExprlyParser.FloatConstContext ctx) {
        return NumConst.make(ctx.FLOAT().getText());
    }

    @Override
    public Unary<V> visitUnaryB(@NotNull ExprlyParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr<V> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return Unary.make(Unary.Op.NOT, e);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

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

    @Override
    public Var<V> visitVarName(@NotNull ExprlyParser.VarNameContext ctx) {
        return Var.make("VOID", ctx.VAR().getText());
    }

    @Override
    public ExprList<V> visitListExpr(@NotNull ExprlyParser.ListExprContext ctx) {
        ExprList<V> list = ExprList.make();
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    @Override
    public Expr<V> visitParenthese(@NotNull ExprlyParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ConnectConst<V> visitConn(@NotNull ExprlyParser.ConnContext ctx) {
        return ConnectConst.make(ctx.op0.getText(), ctx.op1.getText());
    }

}
