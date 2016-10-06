package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ShowTextActionTest {

    @Test
    public void makeHelloWorld() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=1], ShowHelloWorldAction]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowHelloWorld.xml"));
    }

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=1], ShowTextAction [StringConst [Hallo], NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowText.xml"));
    }

    @Test
    public void getMsg() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("StringConst [Hallo]", spa.getMsg().toString());
    }

    @Test
    public void getX() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-7, y=1], ShowTextAction [EmptyExpr [defVal=class java.lang.String], EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=class java.lang.Integer], NumConst [0]], ShowTextAction [StringConst [Hallo], NumConst [0], EmptyExpr [defVal=class java.lang.Integer]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowTextMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowText.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowText1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowTextMissing.xml");
    }
}
