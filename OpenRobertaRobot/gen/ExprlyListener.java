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
	 * Enter a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParamsMethod(ExprlyParser.ParamsMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParamsMethod(ExprlyParser.ParamsMethodContext ctx);
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
	 * Enter a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRobotExpression(ExprlyParser.RobotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link ExprlyParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRobotExpression(ExprlyParser.RobotExpressionContext ctx);
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
	 * Enter a parse tree produced by {@link ExprlyParser#robotExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotExpr(ExprlyParser.RobotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprlyParser#robotExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotExpr(ExprlyParser.RobotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotSensorExpression}
	 * labeled alternative in {@link ExprlyParser#robotSensorExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotSensorExpression(ExprlyParser.RobotSensorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotSensorExpression}
	 * labeled alternative in {@link ExprlyParser#robotSensorExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotSensorExpression(ExprlyParser.RobotSensorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotActuatorExpression}
	 * labeled alternative in {@link ExprlyParser#robotActuatorExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotActuatorExpression(ExprlyParser.RobotActuatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotActuatorExpression}
	 * labeled alternative in {@link ExprlyParser#robotActuatorExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotActuatorExpression(ExprlyParser.RobotActuatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotSpecificExpression}
	 * labeled alternative in {@link ExprlyParser#robotSpecificExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotSpecificExpression(ExprlyParser.RobotSpecificExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotSpecificExpression}
	 * labeled alternative in {@link ExprlyParser#robotSpecificExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotSpecificExpression(ExprlyParser.RobotSpecificExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprlyParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(ExprlyParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprlyParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(ExprlyParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmtFunc(ExprlyParser.StmtFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmtFunc(ExprlyParser.StmtFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBinaryVarAssign(ExprlyParser.BinaryVarAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBinaryVarAssign(ExprlyParser.BinaryVarAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterConditionStatementBlock(ExprlyParser.ConditionStatementBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitConditionStatementBlock(ExprlyParser.ConditionStatementBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatFor(ExprlyParser.RepeatForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatFor(ExprlyParser.RepeatForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(ExprlyParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(ExprlyParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatForEach(ExprlyParser.RepeatForEachContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatForEach(ExprlyParser.RepeatForEachContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWaitStatement(ExprlyParser.WaitStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWaitStatement(ExprlyParser.WaitStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterFlowControl(ExprlyParser.FlowControlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitFlowControl(ExprlyParser.FlowControlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWaitTimeStatement(ExprlyParser.WaitTimeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWaitTimeStatement(ExprlyParser.WaitTimeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncUser(ExprlyParser.FuncUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncUser(ExprlyParser.FuncUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterUserFuncIfStmt(ExprlyParser.UserFuncIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link ExprlyParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitUserFuncIfStmt(ExprlyParser.UserFuncIfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprlyParser#robotStmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotStmt(ExprlyParser.RobotStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprlyParser#robotStmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotStmt(ExprlyParser.RobotStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotSensorStatement}
	 * labeled alternative in {@link ExprlyParser#robotSensorStmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotSensorStatement(ExprlyParser.RobotSensorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotSensorStatement}
	 * labeled alternative in {@link ExprlyParser#robotSensorStmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotSensorStatement(ExprlyParser.RobotSensorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprlyParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ExprlyParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprlyParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ExprlyParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link ExprlyParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(ExprlyParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link ExprlyParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(ExprlyParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link ExprlyParser#mainBlock}.
	 * @param ctx the parse tree
	 */
	void enterMainFunc(ExprlyParser.MainFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link ExprlyParser#mainBlock}.
	 * @param ctx the parse tree
	 */
	void exitMainFunc(ExprlyParser.MainFuncContext ctx);
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
	/**
	 * Enter a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 */
	void enterUserDefCall(ExprlyParser.UserDefCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link ExprlyParser#funCall}.
	 * @param ctx the parse tree
	 */
	void exitUserDefCall(ExprlyParser.UserDefCallContext ctx);
}