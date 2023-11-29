package de.fhg.iais.roberta.typecheck;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.AstTest;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.TestTypecheckCommonLanguageVisitor;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;

public class TypecheckTest extends AstTest {
    private UsedHardwareBean usedHardwareBean;

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
        this.usedHardwareBean = usedHardware.build();
    }

    @Test
    public void test0ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect0.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertEquals(0, infos.size());
    }

    @Test
    public void testSig() throws Exception {
        TypecheckCommonLanguageVisitor astVisitor = new TestTypecheckCommonLanguageVisitor(usedHardwareBean);
        Phrase astForAdd = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect0.xml");
        BlocklyType actual = Sig.of(BlocklyType.NUMBER).typeCheckPhrases(astForAdd, astVisitor);
        Assert.assertEquals(BlocklyType.NUMBER, actual);
        actual = Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(astForAdd, astVisitor);
        Assert.assertEquals(BlocklyType.BOOLEAN, actual);
    }

    @Test
    public void testAssignOk() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_assign_ok.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertEquals(0, infos.size());
    }

    @Test
    public void testAssignFail() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_assign_fail.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertTrue(infos.size() > 0);
    }

    @Test
    public void test1ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect1.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertEquals(0, infos.size());
    }

    @Test
    public void test2ok() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typecorrect2.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertEquals(0, infos.size());
    }

    @Test
    public void test0error() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typeerror0.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertTrue(infos.size() > 0);
    }

    @Test
    public void test1error() throws Exception {
        Phrase ast = UnitTestHelper.getGenericAstOfFirstBlock(testFactory, "/ast/expressions/expr_typeerror1.xml");
        List<NepoInfo> infos = typecheckAndReturnTypecheckResults(ast);
        Assert.assertTrue(infos.size() > 0);
    }

    private List<NepoInfo> typecheckAndReturnTypecheckResults(Phrase ast) {
        TypecheckCommonLanguageVisitor astVisitor = new TestTypecheckCommonLanguageVisitor(usedHardwareBean);
        ast.accept(astVisitor);
        return InfoCollector.collectInfos(ast);
    }
}
