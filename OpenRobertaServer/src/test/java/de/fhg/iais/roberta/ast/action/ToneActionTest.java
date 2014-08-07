package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class ToneActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ToneAction [NumConst [300], NumConst [100]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_PlaySound.xml"));
    }

    @Test
    public void getFrequency() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_PlaySound.xml");

        ToneAction ta = (ToneAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [300]", ta.getFrequency().toString());
    }

    @Test
    public void getDuration() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_PlaySound.xml");

        ToneAction ta = (ToneAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [100]", ta.getDuration().toString());
    }
}
