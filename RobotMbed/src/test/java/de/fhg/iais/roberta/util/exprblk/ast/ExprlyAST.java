package de.fhg.iais.roberta.util.exprblk.ast;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;

public class ExprlyAST extends ExprlyBaseVisitor<Expr<Void>> {

    Map<String, Expr<Void>> vars = new HashMap<String, Expr<Void>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityMath(ExprlyParser.EqualityMathContext ctx) {
        Expr<Void> n0 = visit(ctx.math(0));
        Expr<Void> n1 = visit(ctx.math(1));
        return Binary.make(Op.EQ, n0, n1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityColorL(ExprlyParser.EqualityColorLContext ctx) {
        Expr<Void> l0 = visit(ctx.list_color(0));
        Expr<Void> l1 = visit(ctx.list_color(1));
        return Binary.make(Op.EQ, l0, l1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringConst<Void> visitString(ExprlyParser.StringContext ctx) {
        return StringConst.make(ctx.STR().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitBinaryB(ExprlyParser.BinaryBContext ctx) {
        Expr<Void> p = visit(ctx.bool(0));
        Expr<Void> q = visit(ctx.bool(1));
        if ( ctx.op.getType() == ExprlyParser.AND ) {
            return Binary.make(Op.AND, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.OR ) {
            return Binary.make(Op.OR, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.EQUAL ) {
            return Binary.make(Op.EQ, p, q, "");
        }
        if ( ctx.op.getType() == ExprlyParser.NEQUAL ) {
            return Binary.make(Op.NEQ, p, q, "");
        }
        // Here I should throw an exception.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityMathL(ExprlyParser.EqualityMathLContext ctx) {
        Expr<Void> l0 = visit(ctx.list_math(0));
        Expr<Void> l1 = visit(ctx.list_math(1));
        return Binary.make(Op.EQ, l0, l1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignCon(ExprlyParser.AssignConContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.connection()));
        Var<Void> x = Var.make("CONNECTION", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RgbColor<Void> visitRGB(ExprlyParser.RGBContext ctx) {
        if ( 0 > Integer.parseInt(ctx.r.getText())
            || Integer.parseInt(ctx.r.getText()) > 255
            || 0 > Integer.parseInt(ctx.g.getText())
            || Integer.parseInt(ctx.g.getText()) > 255
            || 0 > Integer.parseInt(ctx.b.getText())
            || Integer.parseInt(ctx.b.getText()) > 255
            || 0 > Integer.parseInt(ctx.a.getText())
            || Integer.parseInt(ctx.a.getText()) > 255 ) {
            return null;
        }
        return RgbColor.make(ctx.r.getText(), ctx.g.getText(), ctx.b.getText(), ctx.a.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MathConst<Void> visitMathConst(ExprlyParser.MathConstContext ctx) {
        return MathConst.make(ctx.CONST().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignCol(ExprlyParser.AssignColContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.color()));
        Var<Void> x = Var.make("COLOR", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityConnection(ExprlyParser.EqualityConnectionContext ctx) {
        Expr<Void> c0 = visit(ctx.connection(0));
        Expr<Void> c1 = visit(ctx.connection(1));
        return Binary.make(Op.EQ, c0, c1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignColL(ExprlyParser.AssignColLContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.list_color()));
        Var<Void> x = Var.make("ARRAY_COLOUR", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignConL(ExprlyParser.AssignConLContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.list_connection()));
        Var<Void> x = Var.make("ARRAY_CONNECTION", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Unary<Void> visitUnaryB(ExprlyParser.UnaryBContext ctx) {
        Expr<Void> e = visit(ctx.bool());
        if ( ctx.op.getType() == ExprlyParser.NOT ) {
            return Unary.make(Unary.Op.NOT, e);
        }
        // Throw exception
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignBL(ExprlyParser.AssignBLContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.list_bool()));
        Var<Void> x = Var.make("ARRAY_BOOLEAN", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityBoolL(ExprlyParser.EqualityBoolLContext ctx) {
        Expr<Void> l0 = visit(ctx.list_bool(0));
        Expr<Void> l1 = visit(ctx.list_bool(1));
        return Binary.make(Op.EQ, l0, l1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectConst<Void> visitConnection(ExprlyParser.ConnectionContext ctx) {
        return ConnectConst.make(ctx.STR(0).getText(), ctx.STR(1).getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityString(ExprlyParser.EqualityStringContext ctx) {
        Expr<Void> s0 = visit(ctx.string(0));
        Expr<Void> s1 = visit(ctx.string(1));
        return Binary.make(Op.EQ, s0, s1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignS(ExprlyParser.AssignSContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.string()));
        Var<Void> x = Var.make("STRING", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitParentheses(ExprlyParser.ParenthesesContext ctx) {
        return visit(ctx.math());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignML(ExprlyParser.AssignMLContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.list_math()));
        Var<Void> x = Var.make("ARRAY_NUMBER", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorConst<Void> visitCol(ExprlyParser.ColContext ctx) {
        return ColorConst.make(ctx.COLOR().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExprList<Void> visitListB(ExprlyParser.ListBContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.BoolContext expr : ctx.bool() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignM(ExprlyParser.AssignMContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.math()));
        Var<Void> x = Var.make("NUMBER", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignSL(ExprlyParser.AssignSLContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.list_string()));
        Var<Void> x = Var.make("ARRAY_STRING", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityColor(ExprlyParser.EqualityColorContext ctx) {
        Expr<Void> c0 = visit(ctx.color(0));
        Expr<Void> c1 = visit(ctx.color(1));
        return Binary.make(Op.EQ, c0, c1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoolConst<Void> visitBoolConstB(ExprlyParser.BoolConstBContext ctx) {
        return BoolConst.make(ctx.BOOL().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitAssignB(ExprlyParser.AssignBContext ctx) {
        this.vars.put(ctx.VAR().getText(), visit(ctx.bool()));
        Var<Void> x = Var.make("BOOLEAN", ctx.VAR().getText());
        return StmtExpr.make(AssignStmt.make(x, this.vars.get(ctx.VAR().getText())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NumConst<Void> visitIntConst(ExprlyParser.IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Unary<Void> visitUnary(ExprlyParser.UnaryContext ctx) {
        Expr<Void> e = visit(ctx.math());
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return Unary.make(Unary.Op.PLUS, e);
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Unary.make(Unary.Op.NEG, e);
        }
        // Here I should throw an exception.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExprList<Void> visitListM(ExprlyParser.ListMContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.MathContext expr : ctx.math() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitVarNameB(ExprlyParser.VarNameBContext ctx) {
        if ( this.vars.get(ctx.VAR().toString()) == null ) {
            System.out.println("Unassinged variable");
        }
        return this.vars.get(ctx.VAR().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExprList<Void> visitListCol(ExprlyParser.ListColContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.ColorContext expr : ctx.color() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityStringL(ExprlyParser.EqualityStringLContext ctx) {
        Expr<Void> l0 = visit(ctx.list_string(0));
        Expr<Void> l1 = visit(ctx.list_string(1));
        return Binary.make(Op.EQ, l0, l1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExprList<Void> visitListCon(ExprlyParser.ListConContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.ConnectionContext expr : ctx.connection() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExprList<Void> visitListS(ExprlyParser.ListSContext ctx) {
        ExprList<Void> list = ExprList.make();
        for ( ExprlyParser.StringContext expr : ctx.string() ) {
            list.addExpr(visit(expr));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitEqualityConnectionL(ExprlyParser.EqualityConnectionLContext ctx) {
        Expr<Void> l0 = visit(ctx.list_connection(0));
        Expr<Void> l1 = visit(ctx.list_connection(1));
        return Binary.make(Op.EQ, l0, l1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitParenthesesB(ExprlyParser.ParenthesesBContext ctx) {
        return visit(ctx.bool());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitVarName(ExprlyParser.VarNameContext ctx) {
        return this.vars.get(ctx.VAR().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitBinary(ExprlyParser.BinaryContext ctx) {
        Expr<Void> n0 = visit(ctx.math(0));
        Expr<Void> n1 = visit(ctx.math(1));
        if ( ctx.op.getType() == ExprlyParser.ADD ) {
            return Binary.make(Op.ADD, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Binary.make(Op.MINUS, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.MUL ) {
            return Binary.make(Op.MULTIPLY, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.DIV ) {
            return Binary.make(Op.DIVIDE, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.MOD ) {
            return Binary.make(Op.MOD, n0, n1, "");
        }
        // Here I should throw an exception.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityMath(ExprlyParser.NEqualityMathContext ctx) {
        Expr<Void> e0 = visit(ctx.math(0));
        Expr<Void> e1 = visit(ctx.math(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityString(ExprlyParser.NEqualityStringContext ctx) {
        Expr<Void> e0 = visit(ctx.string(0));
        Expr<Void> e1 = visit(ctx.string(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityColor(ExprlyParser.NEqualityColorContext ctx) {
        Expr<Void> e0 = visit(ctx.color(0));
        Expr<Void> e1 = visit(ctx.color(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityConnection(ExprlyParser.NEqualityConnectionContext ctx) {
        Expr<Void> e0 = visit(ctx.connection(0));
        Expr<Void> e1 = visit(ctx.connection(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityMathL(ExprlyParser.NEqualityMathLContext ctx) {
        Expr<Void> e0 = visit(ctx.list_math(0));
        Expr<Void> e1 = visit(ctx.list_math(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityBoolL(ExprlyParser.NEqualityBoolLContext ctx) {
        Expr<Void> e0 = visit(ctx.list_bool(0));
        Expr<Void> e1 = visit(ctx.list_bool(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityStringL(ExprlyParser.NEqualityStringLContext ctx) {
        Expr<Void> e0 = visit(ctx.list_string(0));
        Expr<Void> e1 = visit(ctx.list_string(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityColorL(ExprlyParser.NEqualityColorLContext ctx) {
        Expr<Void> e0 = visit(ctx.list_color(0));
        Expr<Void> e1 = visit(ctx.list_color(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitNEqualityConnectionL(ExprlyParser.NEqualityConnectionLContext ctx) {
        Expr<Void> e0 = visit(ctx.list_connection(0));
        Expr<Void> e1 = visit(ctx.list_connection(1));
        return Binary.make(Op.NEQ, e0, e1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitInEqualityMath(ExprlyParser.InEqualityMathContext ctx) {
        Expr<Void> n0 = visit(ctx.math(0));
        Expr<Void> n1 = visit(ctx.math(1));
        if ( ctx.op.getType() == ExprlyParser.GET ) {
            return Binary.make(Op.GT, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.LET ) {
            return Binary.make(Op.LT, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.GEQ ) {
            return Binary.make(Op.GTE, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.LEQ ) {
            return Binary.make(Op.LTE, n0, n1, "");
        }
        // Here I should throw an exception.
        return null;
    }

}
