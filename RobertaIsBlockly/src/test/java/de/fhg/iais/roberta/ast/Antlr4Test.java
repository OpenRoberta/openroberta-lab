package de.fhg.iais.roberta.ast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.text.generated.TextlyLexer;
import de.fhg.iais.roberta.text.generated.TextlyParser;
import de.fhg.iais.roberta.text.generated.TextlyParser.ExprContext;

public class Antlr4Test {
    @Test
    public void test1() throws Exception {
        String p = parse2String("a*b + c*(a+2*e)");
        String r = "(expr (expr (expr a) * (expr b)) + (expr (expr c) * (expr ( (expr (expr a) + (expr (expr 2) * (expr e))) ))))";
        Assert.assertEquals(r, p);
    }

    @Test
    public void test2() throws Exception {
        String p = parse2String("a+b*c-(a+2*e)");
        String r = "(expr (expr (expr a) + (expr (expr b) * (expr c))) - (expr ( (expr (expr a) + (expr (expr 2) * (expr e))) )))";
        Assert.assertEquals(r, p);
    }

    @Test
    public void test3() throws Exception {
        String p = parse2String("-2 - 2");
        String r = "(expr (expr (unary - 2)) - (expr 2))";
        Assert.assertEquals(r, p);
    }

    @Test
    public void test4() throws Exception {
        String p = parse2String("-2 - -2");
        String r = "(expr (expr (unary - 2)) - (expr (unary - 2)))";
        Assert.assertEquals(r, p);
    }

    @Test
    public void test5() throws Exception {
        String p = parse2String("2 - -2");
        String r = "(expr (expr 2) - (expr (unary - 2)))";
        Assert.assertEquals(r, p);
    }

    private String parse2String(String expr) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        TextlyLexer lex = new TextlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TextlyParser parser = new TextlyParser(tokens);
        ExprContext tree = parser.expr();
        return tree.toStringTree(parser);
    }
}
