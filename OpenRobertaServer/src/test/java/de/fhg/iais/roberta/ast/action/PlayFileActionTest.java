package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class PlayFileActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=86], PlayFileAction [1]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_PlayFile.xml"));
    }

    @Test
    public void getFileName() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_PlayFile.xml");
        PlayFileAction<Void> pfa = (PlayFileAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals("1", pfa.getFileName());
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_PlayFile1.xml");
    }
}
