// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/exprly/generated/Exprly.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprlyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprlyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprlyParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ExprlyParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(ExprlyParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseOp(ExprlyParser.IfElseOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryB(ExprlyParser.UnaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryB(ExprlyParser.BinaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryN(ExprlyParser.UnaryNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryN(ExprlyParser.BinaryNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarName(ExprlyParser.VarNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthese(ExprlyParser.ParentheseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathConst(ExprlyParser.MathConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnExp(ExprlyParser.ConnExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncExp(ExprlyParser.FuncExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullConst(ExprlyParser.NullConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Col}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol(ExprlyParser.ColContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConst(ExprlyParser.IntConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatConst(ExprlyParser.FloatConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolConstB(ExprlyParser.BoolConstBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstStr(ExprlyParser.ConstStrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link ExprlyParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpr(ExprlyParser.ListExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link ExprlyParser#connExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConn(ExprlyParser.ConnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Func}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(ExprlyParser.FuncContext ctx);
}