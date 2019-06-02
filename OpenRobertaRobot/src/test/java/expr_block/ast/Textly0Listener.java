package expr_block.ast;

// Generated from Textly0.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Textly0Parser}.
 */
public interface Textly0Listener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the {@code VarName}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterVarName(Textly0Parser.VarNameContext ctx);

    /**
     * Exit a parse tree produced by the {@code VarName}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitVarName(Textly0Parser.VarNameContext ctx);

    /**
     * Enter a parse tree produced by the {@code Binary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterBinary(Textly0Parser.BinaryContext ctx);

    /**
     * Exit a parse tree produced by the {@code Binary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitBinary(Textly0Parser.BinaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code IntConst}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterIntConst(Textly0Parser.IntConstContext ctx);

    /**
     * Exit a parse tree produced by the {@code IntConst}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitIntConst(Textly0Parser.IntConstContext ctx);

    /**
     * Enter a parse tree produced by the {@code Unary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterUnary(Textly0Parser.UnaryContext ctx);

    /**
     * Exit a parse tree produced by the {@code Unary}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitUnary(Textly0Parser.UnaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code Parentheses}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void enterParentheses(Textly0Parser.ParenthesesContext ctx);

    /**
     * Exit a parse tree produced by the {@code Parentheses}
     * labeled alternative in {@link Textly0Parser#expr}.
     * 
     * @param ctx the parse tree
     */
    void exitParentheses(Textly0Parser.ParenthesesContext ctx);
}