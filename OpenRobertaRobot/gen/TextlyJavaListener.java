// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/textly/generated/TextlyJava.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TextlyJavaParser}.
 */
public interface TextlyJavaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(TextlyJavaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(TextlyJavaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link TextlyJavaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(TextlyJavaParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link TextlyJavaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(TextlyJavaParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link TextlyJavaParser#mainBlock}.
	 * @param ctx the parse tree
	 */
	void enterMainFunc(TextlyJavaParser.MainFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link TextlyJavaParser#mainBlock}.
	 * @param ctx the parse tree
	 */
	void exitMainFunc(TextlyJavaParser.MainFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link TextlyJavaParser#userFunc}.
	 * @param ctx the parse tree
	 */
	void enterFuncUser(TextlyJavaParser.FuncUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link TextlyJavaParser#userFunc}.
	 * @param ctx the parse tree
	 */
	void exitFuncUser(TextlyJavaParser.FuncUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link TextlyJavaParser#nameDecl}.
	 * @param ctx the parse tree
	 */
	void enterParamsMethod(TextlyJavaParser.ParamsMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link TextlyJavaParser#nameDecl}.
	 * @param ctx the parse tree
	 */
	void exitParamsMethod(TextlyJavaParser.ParamsMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(TextlyJavaParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(TextlyJavaParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmtFunc(TextlyJavaParser.StmtFuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmtFunc(TextlyJavaParser.StmtFuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtUsedDefCall}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmtUsedDefCall(TextlyJavaParser.StmtUsedDefCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtUsedDefCall}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmtUsedDefCall(TextlyJavaParser.StmtUsedDefCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBinaryVarAssign(TextlyJavaParser.BinaryVarAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBinaryVarAssign(TextlyJavaParser.BinaryVarAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterConditionStatementBlock(TextlyJavaParser.ConditionStatementBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitConditionStatementBlock(TextlyJavaParser.ConditionStatementBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatFor(TextlyJavaParser.RepeatForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatFor(TextlyJavaParser.RepeatForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(TextlyJavaParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(TextlyJavaParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatForEach(TextlyJavaParser.RepeatForEachContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatForEach(TextlyJavaParser.RepeatForEachContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWaitStatement(TextlyJavaParser.WaitStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWaitStatement(TextlyJavaParser.WaitStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterFlowControl(TextlyJavaParser.FlowControlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitFlowControl(TextlyJavaParser.FlowControlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWaitTimeStatement(TextlyJavaParser.WaitTimeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWaitTimeStatement(TextlyJavaParser.WaitTimeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterUserFuncIfStmt(TextlyJavaParser.UserFuncIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitUserFuncIfStmt(TextlyJavaParser.UserFuncIfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotStatement(TextlyJavaParser.RobotStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotStatement(TextlyJavaParser.RobotStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#robotStmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotStmt(TextlyJavaParser.RobotStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#robotStmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotStmt(TextlyJavaParser.RobotStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(TextlyJavaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(TextlyJavaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExp(TextlyJavaParser.LiteralExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExp(TextlyJavaParser.LiteralExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIfElseOp(TextlyJavaParser.IfElseOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIfElseOp(TextlyJavaParser.IfElseOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryB(TextlyJavaParser.BinaryBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryB(TextlyJavaParser.BinaryBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryN(TextlyJavaParser.BinaryNContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryN(TextlyJavaParser.BinaryNContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMathConst(TextlyJavaParser.MathConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMathConst(TextlyJavaParser.MathConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFuncExp(TextlyJavaParser.FuncExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFuncExp(TextlyJavaParser.FuncExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNullConst(TextlyJavaParser.NullConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNullConst(TextlyJavaParser.NullConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRobotExpression(TextlyJavaParser.RobotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRobotExpression(TextlyJavaParser.RobotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryB(TextlyJavaParser.UnaryBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryB(TextlyJavaParser.UnaryBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterImageExpression(TextlyJavaParser.ImageExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitImageExpression(TextlyJavaParser.ImageExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryN(TextlyJavaParser.UnaryNContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryN(TextlyJavaParser.UnaryNContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarName(TextlyJavaParser.VarNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarName(TextlyJavaParser.VarNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthese(TextlyJavaParser.ParentheseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthese(TextlyJavaParser.ParentheseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConnExp(TextlyJavaParser.ConnExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConnExp(TextlyJavaParser.ConnExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PredefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void enterPredefinedImage(TextlyJavaParser.PredefinedImageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PredefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void exitPredefinedImage(TextlyJavaParser.PredefinedImageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserDefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void enterUserDefinedImage(TextlyJavaParser.UserDefinedImageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void exitUserDefinedImage(TextlyJavaParser.UserDefinedImageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageShift}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void enterImageShift(TextlyJavaParser.ImageShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageShift}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void exitImageShift(TextlyJavaParser.ImageShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageInvert}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void enterImageInvert(TextlyJavaParser.ImageInvertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageInvert}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 */
	void exitImageInvert(TextlyJavaParser.ImageInvertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Col}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterCol(TextlyJavaParser.ColContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Col}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitCol(TextlyJavaParser.ColContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntConst(TextlyJavaParser.IntConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntConst(TextlyJavaParser.IntConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterFloatConst(TextlyJavaParser.FloatConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitFloatConst(TextlyJavaParser.FloatConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolConstB(TextlyJavaParser.BoolConstBContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolConstB(TextlyJavaParser.BoolConstBContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterConstStr(TextlyJavaParser.ConstStrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitConstStr(TextlyJavaParser.ConstStrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(TextlyJavaParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(TextlyJavaParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link TextlyJavaParser#connExpr}.
	 * @param ctx the parse tree
	 */
	void enterConn(TextlyJavaParser.ConnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link TextlyJavaParser#connExpr}.
	 * @param ctx the parse tree
	 */
	void exitConn(TextlyJavaParser.ConnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Func}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 */
	void enterFunc(TextlyJavaParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Func}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 */
	void exitFunc(TextlyJavaParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 */
	void enterUserDefCall(TextlyJavaParser.UserDefCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 */
	void exitUserDefCall(TextlyJavaParser.UserDefCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#robotExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotExpr(TextlyJavaParser.RobotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#robotExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotExpr(TextlyJavaParser.RobotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotMicrobitv2Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Expr}.
	 * @param ctx the parse tree
	 */
	void enterRobotMicrobitv2Expression(TextlyJavaParser.RobotMicrobitv2ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotMicrobitv2Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Expr}.
	 * @param ctx the parse tree
	 */
	void exitRobotMicrobitv2Expression(TextlyJavaParser.RobotMicrobitv2ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#microbitv2SensorExpr}.
	 * @param ctx the parse tree
	 */
	void enterMicrobitv2SensorExpr(TextlyJavaParser.Microbitv2SensorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#microbitv2SensorExpr}.
	 * @param ctx the parse tree
	 */
	void exitMicrobitv2SensorExpr(TextlyJavaParser.Microbitv2SensorExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotMicrobitv2SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotMicrobitv2SensorStatement(TextlyJavaParser.RobotMicrobitv2SensorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotMicrobitv2SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotMicrobitv2SensorStatement(TextlyJavaParser.RobotMicrobitv2SensorStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotMicrobitv2ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotMicrobitv2ActuatorStatement(TextlyJavaParser.RobotMicrobitv2ActuatorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotMicrobitv2ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotMicrobitv2ActuatorStatement(TextlyJavaParser.RobotMicrobitv2ActuatorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#microbitv2SensorStmt}.
	 * @param ctx the parse tree
	 */
	void enterMicrobitv2SensorStmt(TextlyJavaParser.Microbitv2SensorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#microbitv2SensorStmt}.
	 * @param ctx the parse tree
	 */
	void exitMicrobitv2SensorStmt(TextlyJavaParser.Microbitv2SensorStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#microbitv2ActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void enterMicrobitv2ActuatorStmt(TextlyJavaParser.Microbitv2ActuatorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#microbitv2ActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void exitMicrobitv2ActuatorStmt(TextlyJavaParser.Microbitv2ActuatorStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotWeDoExpression}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoExpr}.
	 * @param ctx the parse tree
	 */
	void enterRobotWeDoExpression(TextlyJavaParser.RobotWeDoExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotWeDoExpression}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoExpr}.
	 * @param ctx the parse tree
	 */
	void exitRobotWeDoExpression(TextlyJavaParser.RobotWeDoExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#weDoSensorExpr}.
	 * @param ctx the parse tree
	 */
	void enterWeDoSensorExpr(TextlyJavaParser.WeDoSensorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#weDoSensorExpr}.
	 * @param ctx the parse tree
	 */
	void exitWeDoSensorExpr(TextlyJavaParser.WeDoSensorExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotWeDoSensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotWeDoSensorStatement(TextlyJavaParser.RobotWeDoSensorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotWeDoSensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotWeDoSensorStatement(TextlyJavaParser.RobotWeDoSensorStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotWedoActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotWedoActuatorStatement(TextlyJavaParser.RobotWedoActuatorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotWedoActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotWedoActuatorStatement(TextlyJavaParser.RobotWedoActuatorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#wedoSensorStmt}.
	 * @param ctx the parse tree
	 */
	void enterWedoSensorStmt(TextlyJavaParser.WedoSensorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#wedoSensorStmt}.
	 * @param ctx the parse tree
	 */
	void exitWedoSensorStmt(TextlyJavaParser.WedoSensorStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#wedoActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void enterWedoActuatorStmt(TextlyJavaParser.WedoActuatorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#wedoActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void exitWedoActuatorStmt(TextlyJavaParser.WedoActuatorStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotEv3Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Expr}.
	 * @param ctx the parse tree
	 */
	void enterRobotEv3Expression(TextlyJavaParser.RobotEv3ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotEv3Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Expr}.
	 * @param ctx the parse tree
	 */
	void exitRobotEv3Expression(TextlyJavaParser.RobotEv3ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#ev3SensorExpr}.
	 * @param ctx the parse tree
	 */
	void enterEv3SensorExpr(TextlyJavaParser.Ev3SensorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#ev3SensorExpr}.
	 * @param ctx the parse tree
	 */
	void exitEv3SensorExpr(TextlyJavaParser.Ev3SensorExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotEv3SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotEv3SensorStatement(TextlyJavaParser.RobotEv3SensorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotEv3SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotEv3SensorStatement(TextlyJavaParser.RobotEv3SensorStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotEv3ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotEv3ActuatorStatement(TextlyJavaParser.RobotEv3ActuatorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotEv3ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotEv3ActuatorStatement(TextlyJavaParser.RobotEv3ActuatorStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RobotEv3NeuralNetworks}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void enterRobotEv3NeuralNetworks(TextlyJavaParser.RobotEv3NeuralNetworksContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RobotEv3NeuralNetworks}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 */
	void exitRobotEv3NeuralNetworks(TextlyJavaParser.RobotEv3NeuralNetworksContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#ev3SensorStmt}.
	 * @param ctx the parse tree
	 */
	void enterEv3SensorStmt(TextlyJavaParser.Ev3SensorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#ev3SensorStmt}.
	 * @param ctx the parse tree
	 */
	void exitEv3SensorStmt(TextlyJavaParser.Ev3SensorStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#ev3ActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void enterEv3ActuatorStmt(TextlyJavaParser.Ev3ActuatorStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#ev3ActuatorStmt}.
	 * @param ctx the parse tree
	 */
	void exitEv3ActuatorStmt(TextlyJavaParser.Ev3ActuatorStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link TextlyJavaParser#ev3xNN}.
	 * @param ctx the parse tree
	 */
	void enterEv3xNN(TextlyJavaParser.Ev3xNNContext ctx);
	/**
	 * Exit a parse tree produced by {@link TextlyJavaParser#ev3xNN}.
	 * @param ctx the parse tree
	 */
	void exitEv3xNN(TextlyJavaParser.Ev3xNNContext ctx);
}