package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class VolumeActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[VolumeAction [SET, NumConst [50]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_SetVolume.xml"));
    }

    @Test
    public void getVolume() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_SetVolume.xml");

        VolumeAction va = (VolumeAction) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [50]", va.getVolume().toString());
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_SetVolume.xml");

        VolumeAction va = (VolumeAction) transformer.getTree().get(0);

        Assert.assertEquals(VolumeAction.Mode.SET, va.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            VolumeAction va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.action.VolumeAction.Mode.invalid", e.getMessage());
        }
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[VolumeAction [GET, NullConst [null]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_GetVolume.xml"));
    }
}
