package de.fhg.iais.roberta.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationLexer;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ConnectorlContext;
import de.fhg.iais.roberta.conf.transformer.BrickConfigurationTreeToAst;

public class Antlr4BrickConfigurationTest {
    private static final boolean DO_ASSERT = true;
    private static final boolean DO_PRINT = true;

    @Test
    public void testExpr1() throws Exception {
        String p = expr2String("brick Craesy-PID-2014 { sensor port 1 touch; actor port B left middle motor; }");
        String r = "(connectorl brick Craesy-PID-2014 { (connector sensor port 1 touch ;) (connector actor port B (attachActor left middle motor) ;) })";
        assertEquals(r, p);
    }

    @Test
    public void testparseTree2Ast1() throws Exception {
        BrickConfiguration conf =
            BrickConfigurationTreeToAst.startWalkForVisiting("brick Craesy-PID-2014 { sensor port 3 touch; actor port A left large motor; }");
        System.out.println(conf);
    }

    private String expr2String(String expr) throws Exception {
        BrickConfigurationParser parser = mkParser(expr);
        ConnectorlContext tree = parser.connectorl();
        return tree.toStringTree(parser);
    }

    private BrickConfigurationParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        BrickConfigurationLexer lex = new BrickConfigurationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        BrickConfigurationParser parser = new BrickConfigurationParser(tokens);
        return parser;
    }

    private void assertEquals(String r, String p) {
        if ( DO_PRINT ) {
            System.out.println(p);
        }
        if ( DO_ASSERT ) {
            Assert.assertEquals(r, p);
        }
    }
}
