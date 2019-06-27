package de.fhg.iais.roberta.util.exprblk.ast;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

public class ExprlyAST extends ExprlyBaseVisitor<Expr<Void>> {

    @Override
    public ColorConst<Void> visitCol(ExprlyParser.ColContext ctx) {
        return ColorConst.make(ctx.COLOR().getText());
    }

    @Override
    public Binary<Void> visitBinaryB(@NotNull ExprlyParser.BinaryBContext ctx) throws UnsupportedOperationException {
        Expr<Void> p = visit(ctx.expr(0));
        Expr<Void> q = visit(ctx.expr(1));
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
    public Expr<Void> visitBinaryN(@NotNull ExprlyParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr<Void> n0 = visit(ctx.expr(0));
        Expr<Void> n1 = visit(ctx.expr(1));
        if ( ctx.op.getType() == ExprlyParser.POW ) {
            List<Expr<Void>> args = new LinkedList<Expr<Void>>();
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
    public BoolConst<Void> visitBoolConstB(@NotNull ExprlyParser.BoolConstBContext ctx) {
        return BoolConst.make(ctx.BOOL().getText());
    }

    @Override
    public StringConst<Void> visitConstStr(@NotNull ExprlyParser.ConstStrContext ctx) {
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
    public RgbColor<Void> visitRGB(@NotNull ExprlyParser.RGBContext ctx) throws IllegalArgumentException {
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
    public MathConst<Void> visitMathConst(@NotNull ExprlyParser.MathConstContext ctx) {
        return MathConst.make(ctx.CONST().getText());
    }

    @Override
    public NumConst<Void> visitIntConst(@NotNull ExprlyParser.IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    @Override
    public FunctionExpr<Void> visitFunc(@NotNull ExprlyParser.FuncContext ctx) throws UnsupportedOperationException {

        String f = ctx.FNAME().getText();
        List<Expr<Void>> args = new LinkedList<Expr<Void>>();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            args.add(visit(expr));
        }

        if ( f.equals("randInt") ) {
            return FunctionExpr.make(MathRandomIntFunct.make(args));
        }
        if ( f.equals("randFloat") ) {
            if ( args.size() > 2 ) {
                // throw exception
                return null;
            }
            return FunctionExpr.make(MathRandomFloatFunct.make());
        }
        if ( f.equals("e^") ) {
            f = "exp";
        }
        if ( f.equals("10^") ) {
            f = "pow10";
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
        }
        if ( f.equals("avg") ) {
            f = "average";
        }
        if ( f.equals("sd") ) {
            f = "std_dev";
        }
        try {
            return FunctionExpr.make(MathSingleFunct.make(f, args));
        } catch ( Exception e ) {
            throw new UnsupportedOperationException("Invalid function name: " + f);
        }
    }

    @Override
    public NumConst<Void> visitFloatConst(@NotNull ExprlyParser.FloatConstContext ctx) {
        return NumConst.make(ctx.FLOAT().getText());
    }

    @Override
    public Unary<Void> visitUnaryB(@NotNull ExprlyParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr<Void> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return Unary.make(Unary.Op.NOT, e);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    @Override
    public Unary<Void> visitUnaryN(@NotNull ExprlyParser.UnaryNContext ctx) throws UnsupportedOperationException {
        Expr<Void> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return Unary.make(Unary.Op.PLUS, e);
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Unary.make(Unary.Op.NEG, e);
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    @Override
    public Var<Void> visitVarName(@NotNull ExprlyParser.VarNameContext ctx) {
        return Var.make("VOID", ctx.VAR().getText());
    }

    @Override
    public ExprList<Void> visitListExpr(@NotNull ExprlyParser.ListExprContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    @Override
    public Expr<Void> visitParenthese(@NotNull ExprlyParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ConnectConst<Void> visitConn(@NotNull ExprlyParser.ConnContext ctx) {
        return ConnectConst.make(ctx.op0.getText(), ctx.op1.getText());
    }

}
