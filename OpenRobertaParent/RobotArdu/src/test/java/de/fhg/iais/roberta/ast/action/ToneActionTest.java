package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ToneActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=9, y=47], ToneAction [NumConst [300], NumConst [100]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_PlaySound.xml"));
    }

    @Test
    public void getFrequency() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [300]", ta.getFrequency().toString());
    }

    @Test
    public void getDuration() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_PlaySound.xml");
        ToneAction<Void> ta = (ToneAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [100]", ta.getDuration().toString());
    }

    @Test
    public void missing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-8, y=1], ToneAction [EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], ToneAction [NumConst [300], EmptyExpr [defVal=NUMBER_INT]], ToneAction [EmptyExpr [defVal=NUMBER_INT], NumConst [100]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_PlaySoundMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_PlaySound.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_PlaySound1.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_PlaySoundMissing.xml");
    }
}
