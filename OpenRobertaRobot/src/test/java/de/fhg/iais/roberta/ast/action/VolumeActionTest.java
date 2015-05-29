package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.ev3.VolumeAction;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class VolumeActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=135], VolumeAction [SET, NumConst [50]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_SetVolume.xml"));
    }

    @Test
    public void getVolume() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [50]", va.getVolume().toString());
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(VolumeAction.Mode.SET, va.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            VolumeAction<Void> va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null, null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.syntax.action.ev3.VolumeAction.Mode.invalid", e.getMessage());
        }
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=189], VolumeAction [GET, NullConst [null]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_GetVolume.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_SetVolume.xml");
    }
}
