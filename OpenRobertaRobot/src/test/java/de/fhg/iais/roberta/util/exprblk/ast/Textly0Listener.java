package de.fhg.iais.roberta.util.exprblk.ast;

// Generated from Textly0.g4 by ANTLR 4.3
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Textly0Parser}.
 */
public interface Textly0Listener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link Textly0Parser#expression}.
     * 
     * @param ctx the parse tree
     */
    void enterExpression(@NotNull Textly0Parser.ExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link Textly0Parser#expression}.
     * 
     * @param ctx the parse tree
     */
    void exitExpression(@NotNull Textly0Parser.ExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code VarName}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterVarName(@NotNull Textly0Parser.VarNameContext ctx);

    /**
     * Exit a parse tree produced by the {@code VarName}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitVarName(@NotNull Textly0Parser.VarNameContext ctx);

    /**
     * Enter a parse tree produced by the {@code Binary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterBinary(@NotNull Textly0Parser.BinaryContext ctx);

    /**
     * Exit a parse tree produced by the {@code Binary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitBinary(@NotNull Textly0Parser.BinaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code IntConst}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterIntConst(@NotNull Textly0Parser.IntConstContext ctx);

    /**
     * Exit a parse tree produced by the {@code IntConst}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitIntConst(@NotNull Textly0Parser.IntConstContext ctx);

    /**
     * Enter a parse tree produced by the {@code Unary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterUnary(@NotNull Textly0Parser.UnaryContext ctx);

    /**
     * Exit a parse tree produced by the {@code Unary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitUnary(@NotNull Textly0Parser.UnaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code Parentheses}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterParentheses(@NotNull Textly0Parser.ParenthesesContext ctx);

    /**
     * Exit a parse tree produced by the {@code Parentheses}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitParentheses(@NotNull Textly0Parser.ParenthesesContext ctx);
}