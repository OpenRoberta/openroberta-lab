package expr_block.ast;
//package de.fhg.iais.roberta.syntax.expr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.TypecheckVisitor;

public class TextlyASTTest {

    /**
     * create a correct AST programmatically. Check that the AST is ok.<br>
     * create an incorrect AST programmatically. Check that the AST is wrong.<br>
     */
    @Test
    public void test1add2text() throws Exception {
        Expr<BlocklyType> add = expr2AST("1+2");
        TypecheckVisitor tcAdd = TypecheckVisitor.makeVisitorAndTypecheck(add);
        Assert.assertEquals(0, tcAdd.getErrorCount());
    }

    private Expr<BlocklyType> expr2AST(String expr) throws Exception {
        /*Textly0Parser parser = mkParser(expr);
        ExprContext tree = parser.expr();
        return tree.toStringTree(parser);*/
        Textly0Parser parser = mkParser(expr);
        ParseTree tree = parser.expr(); // parse
        TextlyAST eval = new TextlyAST();
        return eval.visit(tree);
    }

    private Textly0Parser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        Textly0Lexer lexer = new Textly0Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Textly0Parser parser = new Textly0Parser(tokens);
        return parser;
    }

}
