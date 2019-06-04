package de.fhg.iais.roberta.util.exprblk.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.TypecheckVisitor;
import de.fhg.iais.roberta.util.exprblk.ast.Textly0Parser.ExpressionContext;

public class TextlyASTTest {

    /**
     * create a correct AST programmatically. Check that the AST is ok.<br>
     */
    @Test
    public void test1add2text() throws Exception {
        Expr<BlocklyType> add = expr2AST("1+2");
        TypecheckVisitor tcAdd = TypecheckVisitor.makeVisitorAndTypecheck(add);
        Assert.assertEquals(0, tcAdd.getErrorCount());
    }

    private Expr<BlocklyType> expr2AST(String expr) throws Exception {
        Textly0Parser parser = mkParser(expr);
        TextlyAST eval = new TextlyAST();
        ExpressionContext expression = parser.expression();
        Expr<BlocklyType> block = eval.visitExpression(expression);
        return block;
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
