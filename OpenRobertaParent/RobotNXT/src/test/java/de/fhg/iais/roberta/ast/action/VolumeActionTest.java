package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class VolumeActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=135], VolumeAction [SET, NumConst [50]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_SetVolume.xml"));
    }

    @Test
    public void getVolume() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [50]", va.getVolume().toString());
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(VolumeAction.Mode.SET, va.getMode());
    }

    @Test(expected = Exception.class)
    public void invalideMode() throws Exception {
        @SuppressWarnings("unused")
        VolumeAction<Void> va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null, null, null);
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=189], VolumeAction [GET, NullConst [null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_GetVolume.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_SetVolume.xml");
    }
}
