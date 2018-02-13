package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ShowTextActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=1], ShowTextAction [StringConst [Hallo], NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_ShowText.xml"));
    }

    @Test
    public void getMsg() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("StringConst [Hallo]", spa.getMsg().toString());
    }

    @Test
    public void getX() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-7, y=1], ShowTextAction [EmptyExpr [defVal=STRING], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ShowTextAction [StringConst [Hallo], EmptyExpr [defVal=NUMBER_INT], NumConst [0]], ShowTextAction [StringConst [Hallo], NumConst [0], EmptyExpr [defVal=NUMBER_INT]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_ShowTextMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_ShowText.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_ShowText1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_ShowTextMissing.xml");
    }
}
