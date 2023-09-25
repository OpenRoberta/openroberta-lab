package de.fhg.iais.roberta.typecheck;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;

public class TypecheckTest extends AstTest {
    private ClassToInstanceMap<IProjectBean.IBuilder> beans;

    @Before
    public void setup() throws Exception {
        UsedHardwareBean.Builder usedHardware = new UsedHardwareBean.Builder();
        usedHardware.addDeclaredVariable("n", BlocklyType.NUMBER);
        usedHardware.addDeclaredVariable("b", BlocklyType.BOOLEAN);
        usedHardware.addDeclaredVariable("s", BlocklyType.STRING);
        usedHardware.addDeclaredVariable("c", BlocklyType.COLOR);
        usedHardware.addDeclaredVariable("nl", BlocklyType.ARRAY_NUMBER);
        usedHardware.addDeclaredVariable("bl", BlocklyType.ARRAY_BOOLEAN);
        usedHardware.addDeclaredVariable("sl", BlocklyType.ARRAY_STRING);
        usedHardware.addDeclaredVariable("cl", BlocklyType.ARRAY_COLOUR);
        ImmutableClassToInstanceMap.Builder beansBuilder = new ImmutableClassToInstanceMap.Builder();
        beansBuilder.put(UsedHardwareBean.Builder.class, usedHardware);
        this.beans = beansBuilder.build();
    }

    @Test
    public void test0ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect0.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void testAssignOk() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_assign_ok.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void testAssignFail() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_assign_fail.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
    }

    @Test
    public void test1ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect1.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void test2ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect2.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    @Test
    public void test0error() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typeerror0.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
    }

    @Test
    public void test1error() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typeerror1.xml");
        TypecheckCommonLanguageVisitor typechecker = TypecheckCommonLanguageVisitor.makeVisitorAndTypecheck(ast, beans);
        Assert.assertTrue(typechecker.getErrorCount() > 0);
    }
}
