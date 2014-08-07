package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class ShowTextActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ShowTextAction [StringConst [Hallo], NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_ShowText.xml"));
    }

    @Test
    public void getMsg() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowText.xml");

        ShowTextAction spa = (ShowTextAction) transformer.getTree().get(0);

        Assert.assertEquals("StringConst [Hallo]", spa.getMsg().toString());
    }

    @Test
    public void getX() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowText.xml");

        ShowTextAction spa = (ShowTextAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowText.xml");

        ShowTextAction spa = (ShowTextAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }
}
