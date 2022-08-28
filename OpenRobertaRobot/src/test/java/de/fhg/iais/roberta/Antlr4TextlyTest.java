//package de.fhg.iais.roberta.antlr4;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//
//import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.junit.Assert;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.fhg.iais.roberta.textly.generated.TextlyLexer;
//import de.fhg.iais.roberta.textly.generated.TextlyParser;
//import de.fhg.iais.roberta.textly.generated.TextlyParser.ExprContext;
//
//public class Antlr4TextlyTest {
//    private static final Logger LOG = LoggerFactory.getLogger(Antlr4TextlyTest.class);
//
//    private static final boolean DO_ASSERT = true;
//    private static final boolean DO_PRINT = false;
//
//    @Test
//    public void testExpr1() throws Exception {
//        String p = expr2String("a*b + c*(a+2*e)");
//        String r = "(expr (expr (expr a) * (expr b)) + (expr (expr c) * (expr ( (expr (expr a) + (expr (expr 2) * (expr e))) ))))";
//        assertEquals(r, p);
//    }
//
//    @Test
//    public void testExpr2() throws Exception {
//        String p = expr2String("a+b*c-(-a+2*e)");
//        String r = "(expr (expr (expr a) + (expr (expr b) * (expr c))) - (expr ( (expr (expr - (expr a)) + (expr (expr 2) * (expr e))) )))";
//        assertEquals(r, p);
//    }
//
//    @Test
//    public void testExpr3() throws Exception {
//        String p = expr2String("1+2");
//        String r = "(expr (expr 1) + (expr 2))";
//        assertEquals(r, p);
//    }
//
//    @Test
//    public void testExpr4() throws Exception {
//        String p = expr2String("-2 - -2");
//        String r = "(expr (expr - (expr 2)) - (expr - (expr 2)))";
//        assertEquals(r, p);
//    }
//
//    @Test
//    public void testExpr5() throws Exception {
//        String p1 = expr2String("2 - -2");
//        String p2 = expr2String("2--2");
//        String r = "(expr (expr 2) - (expr - (expr 2)))";
//        assertEquals(r, p1);
//        assertEquals(r, p2);
//    }
//
//    private String expr2String(String expr) throws Exception {
//        TextlyParser parser = mkParser(expr);
//        ExprContext tree = parser.expr();
//        return tree.toStringTree(parser);
//    }
//
//    private TextlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
//        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
//        ANTLRInputStream input = new ANTLRInputStream(inputStream);
//        TextlyLexer lex = new TextlyLexer(input);
//        CommonTokenStream tokens = new CommonTokenStream(lex);
//        TextlyParser parser = new TextlyParser(tokens);
//        return parser;
//    }
//
//    private void assertEquals(String r, String p) {
//        if ( DO_PRINT ) {
//            LOG.info(p);
//        }
//        if ( DO_ASSERT ) {
//            Assert.assertEquals(r, p);
//        }
//    }
//}