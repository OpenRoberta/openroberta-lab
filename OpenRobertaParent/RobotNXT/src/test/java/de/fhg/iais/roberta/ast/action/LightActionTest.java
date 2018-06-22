package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class LightActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=137, y=188], LightAction [1, ON, RED, EmptyExpr [defVal=COLOR]], LightAction [2, ON, GREEN, EmptyExpr [defVal=COLOR]], LightAction [3, ON, BLUE, EmptyExpr [defVal=COLOR]], LightAction [4, OFF, RED, EmptyExpr [defVal=COLOR]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_LightSensorAction.xml"));
    }

    @Test
    public void getLight() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");
        LightAction<Void> la = (LightAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(BrickLedColor.RED, la.getColor());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");

        LightAction<Void> cs = (LightAction<Void>) transformer.getTree().get(0).get(1);
        LightAction<Void> cs1 = (LightAction<Void>) transformer.getTree().get(0).get(2);
        LightAction<Void> cs2 = (LightAction<Void>) transformer.getTree().get(0).get(3);
        LightAction<Void> cs3 = (LightAction<Void>) transformer.getTree().get(0).get(4);

        Assert.assertEquals("S1", cs.getPort().getCodeName());
        Assert.assertEquals("S2", cs1.getPort().getCodeName());
        Assert.assertEquals("S3", cs2.getPort().getCodeName());
        Assert.assertEquals("S4", cs3.getPort().getCodeName());

    }

    @Test
    public void getState() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");
        LightAction<Void> st = (LightAction<Void>) transformer.getTree().get(0).get(1);
        LightAction<Void> st1 = (LightAction<Void>) transformer.getTree().get(0).get(4);
        Assert.assertEquals(LightMode.ON, st.getMode());
        Assert.assertEquals(LightMode.OFF, st1.getMode());
    }

    /* @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            VolumeAction<Void> va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null, null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.syntax.action.generic.VolumeAction.Mode.invalid", e.getMessage());
        }
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=189], VolumeAction [GET, NullConst [null]]]]]";
        Assert.assertEquals(a, h.generateTransformerString("/ast/actions/action_GetVolume.xml"));
    }*/

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_LightSensorAction.xml");
    }
}
