package de.fhg.iais.roberta.exprEvaluator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;

import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.BlocklyRegion;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

public class ExprlyStatements extends ExprlyBaseVisitor<Stmt> {


    ExprlyVisitor exprlyVisitor = new ExprlyVisitor();
    private Jaxb2ProgramAst helper;


    @Override
    public StmtList visitStatementList(ExprlyParser.StatementListContext ctx) {

        StmtList stmtList = new StmtList();

        for ( ExprlyParser.StmtContext stmt : ctx.stmt() ) {
            Stmt statement = visit(stmt);
            stmtList.addStmt(statement);
        }

        return stmtList;
    }

    @Override
    public Stmt visitChildren(RuleNode node) {
        Stmt result = super.visitChildren(node);
        result.setReadOnly();
        return result;
    }

    @Override
    public Stmt visitStmtFunc(ExprlyParser.StmtFuncContext ctx) throws UnsupportedOperationException {
        String f = ctx.FNAMESTMT().getText();
        List<Expr> args = new LinkedList();

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr ast = exprlyVisitor.visit(expr);

            //ExprStmt ast = (ExprStmt) visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }

        return mkStmtExpr(f, args, ctx);
    }

    private Stmt mkStmtExpr(String f, List<Expr> args, ExprlyParser.StmtFuncContext ctx) {
        List<Expr> argsStatements = new LinkedList();

        Sig signature = FunctionNames.get(f).signature;
        int numberParams = signature.paramTypes.length;

        if ( signature.varargParamType == null && args.size() == numberParams ) {
            switch ( f ) {
                case "showText":

                case "setIndex":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "setIndexFromEnd":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "setIndexFirst":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FIRST, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "setIndexLast":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.LAST, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "insertIndex":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "insertIndexFromEnd":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "insertIndexFirst":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FIRST, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "insertIndexLast":
                    return new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.LAST, args, mkExternalProperty(ctx, "lists_setIndex")));
                case "removeIndex":
                    return new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_START, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeIndexFromEnd":
                    return new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_END, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeIndexFirst":
                    return new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FIRST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeIndexLast":
                    return new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.LAST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
            }
        }
        return null;
    }

    @Override
    public Stmt visitBinaryVarAssign(ExprlyParser.BinaryVarAssignContext ctx) throws UnsupportedOperationException {

        Var n0 = new Var(BlocklyType.VOID, ctx.VAR().getText(), mkPropertyFromClass(ctx, Var.class));
        Expr n1 = exprlyVisitor.visitExpressionforStatements(ctx.expr());

        if ( ctx.op.getText().equals("SET") ) {
            return new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
        }

        return new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
    }

    @Override
    public Stmt visitConditionStatementBlock(ExprlyParser.ConditionStatementBlockContext ctx) throws UnsupportedOperationException {
        List<Expr> conditionsList = new ArrayList<>();
        List<StmtList> listOfStatementList = new ArrayList<>();
        StmtList statementElseList = new StmtList();
        statementElseList.setReadOnly();

        for ( ExprlyParser.StatementListContext stmt : ctx.statementList() ) {
            StmtList statement = (StmtList) visit(stmt);
            statement.setReadOnly();
            listOfStatementList.add(statement);
        }

        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr condition = exprlyVisitor.visitExpressionforStatements(expr);
            condition.setReadOnly();
            conditionsList.add(condition);
        }


        if ( ctx.op != null && ctx.op.getType() == ExprlyParser.ELSE ) {

            statementElseList = (StmtList) visit(ctx.statementList(ctx.statementList().size() - 1)); // Get the last statementList context
            statementElseList.setReadOnly();
            listOfStatementList.remove(ctx.statementList().size() - 1);
        }


        return new IfStmt(mknullProperty(ctx, "robControls_if"), conditionsList, listOfStatementList, statementElseList, 0, 0);

    }

    @Override
    public Stmt visitRepeatIndefinitely(ExprlyParser.RepeatIndefinitelyContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();

        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        Expr trueExpr = new BoolConst(mkInlineProperty(ctx, "logic_boolean"), true);
        trueExpr.setReadOnly();
        statementList.setReadOnly();
        return new RepeatStmt(RepeatStmt.Mode.FOREVER, trueExpr, statementList, mkExternalProperty(ctx, "robControls_loopForever"));
    }

    @Override
    public Stmt visitRepeatStatement(ExprlyParser.RepeatStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        switch ( ctx.op.getType() ) {
            case ExprlyParser.REPEATTIMES:

                Expr exprTimes = exprlyVisitor.visitExpressionforStatements(ctx.expr());

                ExprList el = new ExprList();
                Var k0 = new Var(BlocklyType.NUMBER_INT, "k0", mkPropertyFromClass(ctx, Var.class));
                NumConst n0 = new NumConst(mkExternalProperty(ctx, "math_number"), "0");
                NumConst n1 = new NumConst(mkExternalProperty(ctx, "math_number"), "1");

                el.addExpr(k0);
                el.addExpr(n0);
                el.addExpr(exprTimes);
                el.addExpr(n1);

                el.setReadOnly();
                return new RepeatStmt(RepeatStmt.Mode.TIMES, el, statementList, mkExternalProperty(ctx, "controls_repeat_ext"));

            case ExprlyParser.REPEATUNTIL:

                Expr exprNegBoolean = exprlyVisitor.visitExpressionforStatements(ctx.expr());
                Unary unaryCondition = new Unary(Unary.Op.NOT, exprNegBoolean, mkExternalProperty(ctx, "controls_whileUntil"));
                unaryCondition.setReadOnly();
                return new RepeatStmt(RepeatStmt.Mode.UNTIL, unaryCondition, statementList, mkExternalProperty(ctx, "controls_whileUntil"));

            case ExprlyParser.REPEATWHILE:

                Expr exprBoolean = exprlyVisitor.visitExpressionforStatements(ctx.expr());
                exprBoolean.setReadOnly();
                return new RepeatStmt(RepeatStmt.Mode.WHILE, exprBoolean, statementList, mkExternalProperty(ctx, "controls_whileUntil"));


        }

        return null;
    }

    @Override
    public Stmt visitRepeatForEach(ExprlyParser.RepeatForEachContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        Phrase emptyExpression = new EmptyExpr(type);
        emptyExpression.setReadOnly();
        VarDeclaration var = new VarDeclaration(type, ctx.VAR().getText(), emptyExpression, false, false, mkExternalProperty(ctx, "robControls_forEach"));
        var.setReadOnly();

        Expr expr = exprlyVisitor.visitExpressionforStatements(ctx.expr());
        expr.setReadOnly();

        StmtList statementList = new StmtList();
        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        Binary exprBinary = new Binary(Binary.Op.IN, var, expr, "", mkExternalProperty(ctx, "robControls_forEach"));
        exprBinary.setReadOnly();

        return new RepeatStmt(RepeatStmt.Mode.FOR_EACH, exprBinary, statementList, mkExternalProperty(ctx, "robControls_forEach"));

    }

    @Override
    public Stmt visitWaitStatement(ExprlyParser.WaitStatementContext ctx) throws UnsupportedOperationException {
        //StmtList statementListWait = new StmtList();
        ExprList conditionsList = new ExprList();
        StmtList waitStatementList = new StmtList();

        Expr conditionWait = exprlyVisitor.visitExpressionforStatements(ctx.expr(0));
        conditionWait.setReadOnly();
        StmtList statementListWait = (StmtList) visit(ctx.statementList(0));
        statementListWait.setReadOnly();
        waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionWait, statementListWait, mkExternalProperty(ctx, "robControls_wait")));

        if ( ctx.op != null && ctx.op.getType() == ExprlyParser.ORWAITFOR ) {
            int numberOrWaitFor = ctx.expr().size() - 1;

            for ( int i = 0; i < numberOrWaitFor; i++ ) {
                Expr conditionOrWait = exprlyVisitor.visitExpressionforStatements(ctx.expr(i + 1));
                conditionOrWait.setReadOnly();
                StmtList statementListOrWait = new StmtList();
                statementListOrWait = (StmtList) visit(ctx.statementList(i + 1));
                statementListOrWait.setReadOnly();
                waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionOrWait, statementListOrWait, mkExternalProperty(ctx, "robControls_wait")));
            }
        }

        conditionsList.setReadOnly();
        waitStatementList.setReadOnly();
        return new WaitStmt(mkExternalProperty(ctx, "robControls_wait"), waitStatementList);
    }

    @Override
    public Stmt visitRepeatFor(ExprlyParser.RepeatForContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();

        for ( ExprlyParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        ExprList el = new ExprList();
        Var i = new Var(BlocklyType.NUMBER_INT, ctx.VAR().getText(), mkPropertyFromClass(ctx, Var.class));
        el.addExpr(i);
        for ( ExprlyParser.ExprContext expr : ctx.expr() ) {
            Expr condition = exprlyVisitor.visitExpressionforStatements(expr);
            condition.setReadOnly();
            el.addExpr(condition);
        }
        el.setReadOnly();
        return new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
    }

    @Override
    public Stmt visitFlowControl(ExprlyParser.FlowControlContext ctx) throws UnsupportedOperationException {
        switch ( ctx.op.getType() ) {
            case ExprlyParser.BREAK:
                return new StmtFlowCon(mkExternalProperty(ctx, "controls_flow_statements"), StmtFlowCon.Flow.BREAK);
            case ExprlyParser.CONTINUE:
                return new StmtFlowCon(mkExternalProperty(ctx, "controls_flow_statements"), StmtFlowCon.Flow.CONTINUE);
        }
        return null;
    }

    @Override
    public Stmt visitWaitTimeStatement(ExprlyParser.WaitTimeStatementContext ctx) throws UnsupportedOperationException {
        return new WaitTimeStmt(mkExternalProperty(ctx, "robControls_wait_time"), exprlyVisitor.visitExpressionforStatements(ctx.expr()));
    }

    private static BlocklyProperties mknullProperty(ParserRuleContext ctx, String type) {
        //TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
        BlocklyRegion br = new BlocklyRegion(false, false, null, null, null, true, null, null, null);
        return new BlocklyProperties(type, "1", br, null);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private <T> BlocklyProperties mkPropertyFromClass(ParserRuleContext ctx, Class<T> clazz) {
        String[] blocklyNames = AnnotationHelper.getBlocklyNamesOfAstClass(clazz);
        if ( blocklyNames.length != 1 ) {
            throw new DbcException("rework that! Too many blockly names to generate an ast object, that can be regenerated as XML");
        }
        return mkExternalProperty(ctx, blocklyNames[0]);
    }


}
