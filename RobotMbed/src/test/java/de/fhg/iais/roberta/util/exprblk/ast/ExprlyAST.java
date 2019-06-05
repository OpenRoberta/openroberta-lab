package de.fhg.iais.roberta.util.exprblk.ast;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;

public class ExprlyAST extends ExprlyBaseVisitor<Expr<Void>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<Void> visitBinary(ExprlyParser.BinaryContext ctx) {
        Expr<Void> n0 = visit(ctx.expr(0));
        Expr<Void> n1 = visit(ctx.expr(1));
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
        if ( ctx.op.getType() == ExprlyParser.AND ) {
            return Binary.make(Op.AND, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.OR ) {
            return Binary.make(Op.OR, n0, n1, "");
        }
        if ( ctx.op.getType() == ExprlyParser.EQUAL ) {
            return Binary.make(Op.EQ, n0, n1, "");
        }
        // Here I should throw an exception.
        return null;
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
    public Expr<Void> visitUnary(ExprlyParser.UnaryContext ctx) {
        Expr<Void> e = visit(ctx.expr());
        if ( ctx.op.getType() == ExprlyParser.SUB ) {
            return Binary.make(Op.MULTIPLY, NumConst.make("-1"), e, "");
        }
        //if(ctx.op.getType()==ExprlyParser.NOT) return Binary.make(Op.?, NumConst.make("-1"), e, "");
        // Error Handling as well
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<Void> visitParentheses(ExprlyParser.ParenthesesContext ctx) {
        return visit(ctx.expr());
    }

}