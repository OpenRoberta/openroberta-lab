package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.nxt.LightSensorActionMode;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class LightSensorActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=137, y=188], LightSensorAction [RED, ON, S1], LightSensorAction [GREEN, ON, S2], LightSensorAction [BLUE, ON, S3], LightSensorAction [RED, OFF, S4]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_LightSensorAction.xml"));
    }

    @Test
    public void getLight() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");
        LightSensorAction<Void> la = (LightSensorAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(LightSensorActionMode.RED, la.getLight());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");

        LightSensorAction<Void> cs = (LightSensorAction<Void>) transformer.getTree().get(0).get(1);
        LightSensorAction<Void> cs1 = (LightSensorAction<Void>) transformer.getTree().get(0).get(2);
        LightSensorAction<Void> cs2 = (LightSensorAction<Void>) transformer.getTree().get(0).get(3);
        LightSensorAction<Void> cs3 = (LightSensorAction<Void>) transformer.getTree().get(0).get(4);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
        Assert.assertEquals(SensorPort.S2, cs1.getPort());
        Assert.assertEquals(SensorPort.S3, cs2.getPort());
        Assert.assertEquals(SensorPort.S4, cs3.getPort());

    }

    @Test
    public void getState() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_LightSensorAction.xml");
        LightSensorAction<Void> st = (LightSensorAction<Void>) transformer.getTree().get(0).get(1);
        LightSensorAction<Void> st1 = (LightSensorAction<Void>) transformer.getTree().get(0).get(4);
        Assert.assertEquals(WorkingState.ON, st.getState());
        Assert.assertEquals(WorkingState.OFF, st1.getState());
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
