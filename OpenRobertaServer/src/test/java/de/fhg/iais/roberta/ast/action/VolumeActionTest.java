package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class VolumeActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[VolumeAction [SET, NumConst [50]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_SetVolume.xml"));
    }

    @Test
    public void getVolume() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals("NumConst [50]", va.getVolume().toString());
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals(VolumeAction.Mode.SET, va.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            VolumeAction<Void> va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null, false, "");
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.action.VolumeAction.Mode.invalid", e.getMessage());
        }
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[VolumeAction [GET, NullConst [null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_GetVolume.xml"));
    }
}
