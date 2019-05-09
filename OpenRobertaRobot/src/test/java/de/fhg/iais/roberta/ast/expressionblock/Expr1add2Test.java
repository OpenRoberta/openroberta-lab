package de.fhg.iais.roberta.ast.expressionblock;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.TypecheckVisitor;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;

public class Expr1add2Test {
    private static final Logger LOG = LoggerFactory.getLogger(Expr1add2Test.class);

    private final GenericHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void test0ok() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/expressionblock/expr_1add2.xml");
        LOG.info("" + ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }
}
