// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/textly/generated/TextlyJava.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TextlyJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TextlyJavaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(TextlyJavaParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link TextlyJavaParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(TextlyJavaParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MainFunc}
	 * labeled alternative in {@link TextlyJavaParser#mainBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainFunc(TextlyJavaParser.MainFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncUser}
	 * labeled alternative in {@link TextlyJavaParser#userFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncUser(TextlyJavaParser.FuncUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParamsMethod}
	 * labeled alternative in {@link TextlyJavaParser#nameDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamsMethod(TextlyJavaParser.ParamsMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(TextlyJavaParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtFunc}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtFunc(TextlyJavaParser.StmtFuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtUsedDefCall}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtUsedDefCall(TextlyJavaParser.StmtUsedDefCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryVarAssign}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryVarAssign(TextlyJavaParser.BinaryVarAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConditionStatementBlock}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStatementBlock(TextlyJavaParser.ConditionStatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatFor}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatFor(TextlyJavaParser.RepeatForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(TextlyJavaParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RepeatForEach}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatForEach(TextlyJavaParser.RepeatForEachContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WaitStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitStatement(TextlyJavaParser.WaitStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FlowControl}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowControl(TextlyJavaParser.FlowControlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WaitTimeStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitTimeStatement(TextlyJavaParser.WaitTimeStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserFuncIfStmt}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserFuncIfStmt(TextlyJavaParser.UserFuncIfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotStatement}
	 * labeled alternative in {@link TextlyJavaParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotStatement(TextlyJavaParser.RobotStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#robotStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotStmt(TextlyJavaParser.RobotStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(TextlyJavaParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExp(TextlyJavaParser.LiteralExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfElseOp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseOp(TextlyJavaParser.IfElseOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryB(TextlyJavaParser.BinaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryN(TextlyJavaParser.BinaryNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MathConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathConst(TextlyJavaParser.MathConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncExp(TextlyJavaParser.FuncExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullConst}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullConst(TextlyJavaParser.NullConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotExpression(TextlyJavaParser.RobotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryB}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryB(TextlyJavaParser.UnaryBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImageExpression}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImageExpression(TextlyJavaParser.ImageExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryN}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryN(TextlyJavaParser.UnaryNContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarName}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarName(TextlyJavaParser.VarNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parenthese}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthese(TextlyJavaParser.ParentheseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConnExp}
	 * labeled alternative in {@link TextlyJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnExp(TextlyJavaParser.ConnExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PredefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredefinedImage(TextlyJavaParser.PredefinedImageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserDefinedImage}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDefinedImage(TextlyJavaParser.UserDefinedImageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImageShift}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImageShift(TextlyJavaParser.ImageShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImageInvert}
	 * labeled alternative in {@link TextlyJavaParser#imageExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImageInvert(TextlyJavaParser.ImageInvertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Col}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCol(TextlyJavaParser.ColContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConst(TextlyJavaParser.IntConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatConst}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatConst(TextlyJavaParser.FloatConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolConstB}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolConstB(TextlyJavaParser.BoolConstBContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstStr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstStr(TextlyJavaParser.ConstStrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListExpr}
	 * labeled alternative in {@link TextlyJavaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpr(TextlyJavaParser.ListExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conn}
	 * labeled alternative in {@link TextlyJavaParser#connExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConn(TextlyJavaParser.ConnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Func}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(TextlyJavaParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserDefCall}
	 * labeled alternative in {@link TextlyJavaParser#funCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDefCall(TextlyJavaParser.UserDefCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#robotExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotExpr(TextlyJavaParser.RobotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotMicrobitv2Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotMicrobitv2Expression(TextlyJavaParser.RobotMicrobitv2ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#microbitv2SensorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMicrobitv2SensorExpr(TextlyJavaParser.Microbitv2SensorExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotMicrobitv2SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotMicrobitv2SensorStatement(TextlyJavaParser.RobotMicrobitv2SensorStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotMicrobitv2ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotMicrobitv2Stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotMicrobitv2ActuatorStatement(TextlyJavaParser.RobotMicrobitv2ActuatorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#microbitv2SensorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMicrobitv2SensorStmt(TextlyJavaParser.Microbitv2SensorStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#microbitv2ActuatorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMicrobitv2ActuatorStmt(TextlyJavaParser.Microbitv2ActuatorStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotWeDoExpression}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotWeDoExpression(TextlyJavaParser.RobotWeDoExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#weDoSensorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWeDoSensorExpr(TextlyJavaParser.WeDoSensorExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotWeDoSensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotWeDoSensorStatement(TextlyJavaParser.RobotWeDoSensorStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotWedoActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotWeDoStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotWedoActuatorStatement(TextlyJavaParser.RobotWedoActuatorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#wedoSensorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWedoSensorStmt(TextlyJavaParser.WedoSensorStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#wedoActuatorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWedoActuatorStmt(TextlyJavaParser.WedoActuatorStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotEv3Expression}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotEv3Expression(TextlyJavaParser.RobotEv3ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#ev3SensorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEv3SensorExpr(TextlyJavaParser.Ev3SensorExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotEv3SensorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotEv3SensorStatement(TextlyJavaParser.RobotEv3SensorStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotEv3ActuatorStatement}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotEv3ActuatorStatement(TextlyJavaParser.RobotEv3ActuatorStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RobotEv3NeuralNetworks}
	 * labeled alternative in {@link TextlyJavaParser#robotEv3Stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobotEv3NeuralNetworks(TextlyJavaParser.RobotEv3NeuralNetworksContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#ev3SensorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEv3SensorStmt(TextlyJavaParser.Ev3SensorStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#ev3ActuatorStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEv3ActuatorStmt(TextlyJavaParser.Ev3ActuatorStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link TextlyJavaParser#ev3xNN}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEv3xNN(TextlyJavaParser.Ev3xNNContext ctx);
}