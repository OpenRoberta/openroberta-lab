package expr_block.ast;

// Generated from Textly0.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Textly0Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *        operations with no return type.
 */
public interface Textly0Visitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by the {@code VarName}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVarName(Textly0Parser.VarNameContext ctx);

    /**
     * Visit a parse tree produced by the {@code Binary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBinary(Textly0Parser.BinaryContext ctx);

    /**
     * Visit a parse tree produced by the {@code IntConst}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntConst(Textly0Parser.IntConstContext ctx);

    /**
     * Visit a parse tree produced by the {@code Unary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnary(Textly0Parser.UnaryContext ctx);

    /**
     * Visit a parse tree produced by the {@code Parentheses}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParentheses(Textly0Parser.ParenthesesContext ctx);
}