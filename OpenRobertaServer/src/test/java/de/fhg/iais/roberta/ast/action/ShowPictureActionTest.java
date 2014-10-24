package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class ShowPictureActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-74, y=64], ShowPictureAction [EYESOPEN, NumConst [0], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPicture.xml"));
    }

    @Test
    public void getPicture() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals("EYESOPEN", spa.getPicture().name());
    }

    @Test
    public void getX() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals("NumConst [0]", spa.getX().toString());
    }

    @Test
    public void getY() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_ShowPicture.xml");
        ShowPictureAction<Void> spa = (ShowPictureAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals("NumConst [0]", spa.getY().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-9, y=3], ShowPictureAction [FLOWERS, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ShowPictureAction [OLDGLASSES, NumConst [0], EmptyExpr [defVal=class java.lang.Integer]], ShowPictureAction [TACHO, EmptyExpr [defVal=class java.lang.Integer], NumConst [0]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ShowPictureMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPicture.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPicture1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ShowPictureMissing.xml");
    }
}
