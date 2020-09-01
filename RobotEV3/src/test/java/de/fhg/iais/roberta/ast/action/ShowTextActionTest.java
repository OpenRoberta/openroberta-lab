package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowTextActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=1], ShowTextAction [StringConst [Hallo], NumConst [0], NumConst [0]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_ShowText.xml");
    }

    @Test
    public void getMsg() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("StringConst [Hallo]", spa.getMsg().toString());
    }

    @Test
    public void getX() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-7, y=1], ShowTextAction [EmptyExpr [defVal=STRING], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=NUMBER_INT], NumConst [0]], ShowTextAction [StringConst [Hallo], NumConst [0], EmptyExpr [defVal=NUMBER_INT]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_ShowTextMissing.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ShowText.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ShowText1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ShowTextMissing.xml");
    }
}
