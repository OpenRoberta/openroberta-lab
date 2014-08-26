package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class ShowTextActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ShowTextAction [StringConst [Hallo], NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowText.xml"));
    }

    @Test
    public void getMsg() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("StringConst [Hallo]", spa.getMsg().toString());
    }

    @Test
    public void getX() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowText.xml");
        ShowTextAction<Void> spa = (ShowTextAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }
}
