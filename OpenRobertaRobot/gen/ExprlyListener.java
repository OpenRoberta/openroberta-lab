// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/exprly/generated/Exprly.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprlyParser}.
 */
public interface ExprlyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExprlyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ExprlyParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprlyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ExprlyParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExp(ExprlyParser.LiteralExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExp(ExprlyParser.LiteralExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIfElseOp(ExprlyParser.IfElseOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIfElseOp(ExprlyParser.IfElseOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryB(ExprlyParser.UnaryBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryB(ExprlyParser.UnaryBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryB(ExprlyParser.BinaryBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryB(ExprlyParser.BinaryBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryN(ExprlyParser.UnaryNContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryN(ExprlyParser.UnaryNContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryN(ExprlyParser.BinaryNContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryN(ExprlyParser.BinaryNContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarName(ExprlyParser.VarNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarName(ExprlyParser.VarNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthese(ExprlyParser.ParentheseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthese(ExprlyParser.ParentheseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMathConst(ExprlyParser.MathConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMathConst(ExprlyParser.MathConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConnExp(ExprlyParser.ConnExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConnExp(ExprlyParser.ConnExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFuncExp(ExprlyParser.FuncExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFuncExp(ExprlyParser.FuncExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNullConst(ExprlyParser.NullConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNullConst(ExprlyParser.NullConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Col}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterCol(ExprlyParser.ColContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Col}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitCol(ExprlyParser.ColContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntConst(ExprlyParser.IntConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntConst(ExprlyParser.IntConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFloatConst(ExprlyParser.FloatConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFloatConst(ExprlyParser.FloatConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolConstB(ExprlyParser.BoolConstBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolConstB(ExprlyParser.BoolConstBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterConstStr(ExprlyParser.ConstStrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitConstStr(ExprlyParser.ConstStrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(ExprlyParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(ExprlyParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link ExprlyParser#connExpr}.
	 * @param ctx the parse tree
	 */
	void enterConn(ExprlyParser.ConnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link ExprlyParser#connExpr}.
	 * @param ctx the parse tree
	 */
	void exitConn(ExprlyParser.ConnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Func}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 */
	void enterFunc(ExprlyParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Func}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 */
	void exitFunc(ExprlyParser.FuncContext ctx);
}