package de.fhg.iais.roberta.ast.typecheck;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.TypecheckVisitor;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class TypecheckTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void test0ok() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/ast/expressions/expr_typecorrect0.xml");
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void test1ok() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/ast/expressions/expr_typecorrect1.xml");
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertEquals(1, typechecker.getErrorCount());
    }

    @Test
    public void test2ok() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/ast/expressions/expr_typecorrect2.xml");
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void test0error() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/ast/expressions/expr_typeerror0.xml");
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
    }

    @Test
    public void test1error() throws Exception {
        Phrase<BlocklyType> ast = this.h.generateAST("/ast/expressions/expr_typeerror1.xml");
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
    }
}
