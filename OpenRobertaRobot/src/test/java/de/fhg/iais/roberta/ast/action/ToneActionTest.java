package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.ev3.ToneAction;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ToneActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=9, y=47], ToneAction [NumConst [300], NumConst [100]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlaySound.xml"));
    }

    @Test
    public void getFrequency() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [300]", ta.getFrequency().toString());
    }

    @Test
    public void getDuration() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [100]", ta.getDuration().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-8, y=1], ToneAction [EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ToneAction [NumConst [300], EmptyExpr [defVal=class java.lang.Integer]], ToneAction [EmptyExpr [defVal=class java.lang.Integer], NumConst [100]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlaySoundMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_PlaySound.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_PlaySound1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_PlaySoundMissing.xml");
    }
}
