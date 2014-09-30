package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class ShowPictureActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ShowPictureAction [SMILEY1, NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPicture.xml"));
    }

    @Test
    public void getPicture() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("SMILEY1", spa.getPicture());
    }

    @Test
    public void getX() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[ShowPictureAction [SMILEY1, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ShowPictureAction [SMILEY1, NumConst [0], EmptyExpr [defVal=class java.lang.Integer]], ShowPictureAction [SMILEY1, EmptyExpr [defVal=class java.lang.Integer], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPictureMissing.xml"));
    }
}
