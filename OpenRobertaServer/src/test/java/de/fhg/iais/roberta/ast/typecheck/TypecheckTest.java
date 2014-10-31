package de.fhg.iais.roberta.ast.typecheck;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TypecheckTest {
    private static final BrickConfiguration BRICK_CONFIGURATION = new BrickConfiguration.Builder().build();

    @Test
    public void test0ok() throws Exception {
        Phrase<BlocklyType> ast = Helper.generateAST("/ast/expressions/expr_typecorrect0.xml");
        System.out.println(ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck("test", BRICK_CONFIGURATION, ast);
        Assert.assertEquals(0, typechecker.getErrorCount());
        System.out.println(typechecker.getResultType());
    }

    @Test
    public void test1ok() throws Exception {
        Phrase<BlocklyType> ast = Helper.generateAST("/ast/expressions/expr_typecorrect1.xml");
        System.out.println(ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck("test", BRICK_CONFIGURATION, ast);
        Assert.assertEquals(1, typechecker.getErrorCount());
        System.out.println(typechecker.getResultType());
    }

    @Test
    public void test2ok() throws Exception {
        Phrase<BlocklyType> ast = Helper.generateAST("/ast/expressions/expr_typecorrect2.xml");
        System.out.println(ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck("test", BRICK_CONFIGURATION, ast);
        Assert.assertEquals(0, typechecker.getErrorCount());
        System.out.println(typechecker.getResultType());
    }

    @Test
    public void test0error() throws Exception {
        Phrase<BlocklyType> ast = Helper.generateAST("/ast/expressions/expr_typeerror0.xml");
        System.out.println(ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck("test", BRICK_CONFIGURATION, ast);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
        for ( NepoInfo error : typechecker.getInfos() ) {
            System.out.println(error);
        }
    }

    @Test
    public void test1error() throws Exception {
        Phrase<BlocklyType> ast = Helper.generateAST("/ast/expressions/expr_typeerror1.xml");
        System.out.println(ast);
        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck("test", BRICK_CONFIGURATION, ast);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
        for ( NepoInfo error : typechecker.getInfos() ) {
            System.out.println(error);
        }
    }
}
