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
	 * Visit a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamsMethod(ExprlyParser.ParamsMethodContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryB(ExprlyParser.BinaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryN(ExprlyParser.BinaryNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathConst(ExprlyParser.MathConstContext ctx);
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
	 * Visit a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotExpression(ExprlyParser.RobotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryB(ExprlyParser.UnaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryN(ExprlyParser.UnaryNContext ctx);
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
	 * Visit a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnExp(ExprlyParser.ConnExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprlyParser#robotExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotExpr(ExprlyParser.RobotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MicrobitV2SensorExpression}
	 * labeled alternative in {@link ExprlyParser#robotSensorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMicrobitV2SensorExpression(ExprlyParser.MicrobitV2SensorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprlyParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(ExprlyParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtFunc(ExprlyParser.StmtFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryVarAssign(ExprlyParser.BinaryVarAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStatementBlock(ExprlyParser.ConditionStatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatFor(ExprlyParser.RepeatForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(ExprlyParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatForEach(ExprlyParser.RepeatForEachContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitStatement(ExprlyParser.WaitStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowControl(ExprlyParser.FlowControlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitTimeStatement(ExprlyParser.WaitTimeStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncUser(ExprlyParser.FuncUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserFuncIfStmt(ExprlyParser.UserFuncIfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprlyParser#robotStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotStmt(ExprlyParser.RobotStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MicrobitV2SensorStatement}
	 * labeled alternative in {@link ExprlyParser#robotSensorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMicrobitV2SensorStatement(ExprlyParser.MicrobitV2SensorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprlyParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ExprlyParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link ExprlyParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(ExprlyParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link ExprlyParser#mainBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainFunc(ExprlyParser.MainFuncContext ctx);
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
	/**
	 * Visit a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDefCall(ExprlyParser.UserDefCallContext ctx);
}