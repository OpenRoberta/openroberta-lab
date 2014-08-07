package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class ShowPictureActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ShowPictureAction [SMILEY1, NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_ShowPicture.xml"));
    }

    @Test
    public void getPicture() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowPicture.xml");

        ShowPictureAction spa = (ShowPictureAction) transformer.getTree().get(0);

        Assert.assertEquals("SMILEY1", spa.getPicture());
    }

    @Test
    public void getX() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowPicture.xml");

        ShowPictureAction spa = (ShowPictureAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_ShowPicture.xml");

        ShowPictureAction spa = (ShowPictureAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }
}
