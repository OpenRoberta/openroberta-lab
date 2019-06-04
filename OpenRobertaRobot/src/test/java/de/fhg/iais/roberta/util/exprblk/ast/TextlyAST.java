package de.fhg.iais.roberta.util.exprblk.ast;

import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class TextlyAST extends Textly0BaseVisitor<Expr<BlocklyType>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Binary<BlocklyType> visitBinary(Textly0Parser.BinaryContext ctx) {
        Expr<BlocklyType> n0 = visit(ctx.expr(0));
        Expr<BlocklyType> n1 = visit(ctx.expr(1));
        if ( ctx.op.getType() == Textly0Parser.ADD ) {
            return Binary.make(Op.ADD, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.SUB ) {
            return Binary.make(Op.MINUS, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.MUL ) {
            return Binary.make(Op.MULTIPLY, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.DIV ) {
            return Binary.make(Op.DIVIDE, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.AND ) {
            return Binary.make(Op.AND, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.OR ) {
            return Binary.make(Op.OR, n0, n1, "");
        }
        if ( ctx.op.getType() == Textly0Parser.EQUAL ) {
            return Binary.make(Op.EQ, n0, n1, "");
        }
        // Here I should throw an exception.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NumConst<BlocklyType> visitIntConst(Textly0Parser.IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<BlocklyType> visitUnary(Textly0Parser.UnaryContext ctx) {
        Expr<BlocklyType> e = visit(ctx.expr());
        if ( ctx.op.getType() == Textly0Parser.SUB ) {
            return Binary.make(Op.MULTIPLY, NumConst.make("-1"), e, "");
        }
        //if(ctx.op.getType()==Textly0Parser.NOT) return Binary.make(Op.?, NumConst.make("-1"), e, "");
        // Error Handling as well
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expr<BlocklyType> visitParentheses(Textly0Parser.ParenthesesContext ctx) {
        return visit(ctx.expr());
    }

}