package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class ToneActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ToneAction [NumConst [300], NumConst [100]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlaySound.xml"));
    }

    @Test
    public void getFrequency() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [300]", ta.getFrequency().toString());
    }

    @Test
    public void getDuration() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [100]", ta.getDuration().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[ToneAction [EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]], ToneAction [NumConst [300], EmptyExpr [defVal=class java.lang.Integer]], ToneAction [EmptyExpr [defVal=class java.lang.Integer], NumConst [100]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlaySoundMissing.xml"));
    }
}
