package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.ev3.PlayFileAction;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class PlayFileActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=86], PlayFileAction [1]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlayFile.xml"));
    }

    @Test
    public void getFileName() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlayFile.xml");
        PlayFileAction<Void> pfa = (PlayFileAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("1", pfa.getFileName());
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_PlayFile1.xml");
    }
}
