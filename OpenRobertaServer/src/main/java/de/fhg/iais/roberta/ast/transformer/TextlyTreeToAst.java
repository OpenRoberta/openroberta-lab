package de.fhg.iais.roberta.ast.transformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.Var.TypeVar;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.textly.generated.TextlyBaseVisitor;
import de.fhg.iais.roberta.textly.generated.TextlyLexer;
import de.fhg.iais.roberta.textly.generated.TextlyParser;
import de.fhg.iais.roberta.textly.generated.TextlyParser.AssignStmtContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.BinaryContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.ExprStmtContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.IfElseContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.IfElseIfContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.IfStmtContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.IfThenContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.IntConstContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.ParenthesesContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.RepeatStmtContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.StmtContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.StmtListContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.UnaryContext;
import de.fhg.iais.roberta.textly.generated.TextlyParser.VarNameContext;

public class TextlyTreeToAst extends TextlyBaseVisitor<Phrase> {

    /**
     * take a textly program as String, parse it, create a visitor as an instance of this class and visit the parse tree to create an AST.<br>
     * Factory method
     */
    public static Phrase startWalkForVisiting(String stmt) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(stmt.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        TextlyLexer lex = new TextlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TextlyParser parser = new TextlyParser(tokens);
        StmtContext tree = parser.stmt();
        new TextlyTreeToAst().visit(tree);
        TextlyTreeToAst visitor = new TextlyTreeToAst();
        return visitor.visit(tree);
    }

    @Override
    public Phrase visitExprStmt(ExprStmtContext ctx) {
        Phrase expr = visit(ctx.expr());
        return ExprStmt.make((Expr) expr);
    }

    @Override
    public Phrase visitBinary(BinaryContext ctx) {
        String op = ctx.getChild(1).getText();
        Phrase left = visit(ctx.getChild(0));
        Phrase right = visit(ctx.getChild(2));
        return Binary.make(Binary.Op.get(op), (Expr) left, (Expr) right);
    }

    @Override
    public Phrase visitStmtList(StmtListContext ctx) {
        StmtList sl = StmtList.make();
        for ( StmtContext stmt : ctx.stmt() ) {
            sl.addStmt((Stmt) visit(stmt));
        }
        sl.setReadOnly();
        return sl;
    }

    @Override
    public Phrase visitParentheses(ParenthesesContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Phrase visitVarName(VarNameContext ctx) {
        return Var.make(ctx.VAR().getText(), TypeVar.INTEGER);
    }

    @Override
    public Phrase visitIntConst(IntConstContext ctx) {
        return NumConst.make(ctx.INT().getText());
    }

    @Override
    public Phrase visitUnary(UnaryContext ctx) {
        String op = ctx.getChild(0).getText(); // strange name for the operand. Obviously from the last rule!
        Phrase expr = visit(ctx.getChild(1));
        return Unary.make(Unary.Op.get(op), (Expr) expr);
    }

    @Override
    public Phrase visitIfStmt(IfStmtContext ctx) {
        return visit(ctx.ifThenR());
    }

    @Override
    public Phrase visitIfThen(IfThenContext ctx) {
        return null;
        //        Phrase expr = visit(ctx.expr());
        //        Phrase thenList = visit(ctx.stmtl());
        //        IfElseRContext ifElse = ctx.ifElseR();
        //        if ( ifElse == null ) {
        //            return IfStmt.make((Expr) expr, (StmtList) thenList);
        //        } else {
        //            Phrase elseList = visit(ifElse);
        //            if ( elseList instanceof IfStmt ) {
        //                IfStmt elseIf = (IfStmt) elseList;
        //                return IfStmt.make((Expr) expr, (StmtList) thenList, elseIf);
        //            } else if ( elseList instanceof StmtList ) {
        //                StmtList elsel = (StmtList) elseList;
        //                return IfStmt.make((Expr) expr, (StmtList) thenList, elsel);
        //            } else {
        //                throw new DbcException("invalid pharses in if then elsif else: " + ctx.getText());
        //            }
        //        }
    }

    @Override
    public Phrase visitIfElseIf(IfElseIfContext ctx) {
        return visit(ctx.ifThenR());
    }

    @Override
    public Phrase visitIfElse(IfElseContext ctx) {
        return visit(ctx.stmtl());
    }

    @Override
    public Phrase visitRepeatStmt(RepeatStmtContext ctx) {
        Phrase expr = visit(ctx.expr());
        Phrase stmtl = visit(ctx.stmtl());
        return RepeatStmt.make(Mode.FOR, (Expr) expr, (StmtList) stmtl);
    }

    @Override
    public Phrase visitAssignStmt(AssignStmtContext ctx) {
        Phrase name = Var.make(ctx.VAR().getText(), TypeVar.INTEGER);
        Phrase expr = visit(ctx.expr());
        return AssignStmt.make((Var) name, (Expr) expr);
    }
}