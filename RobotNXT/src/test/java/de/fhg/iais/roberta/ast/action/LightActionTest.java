package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightActionTest extends NxtAstTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=137, y=188], LightAction [1, ON, RED, EmptyExpr [defVal=COLOR]], LightAction [2, ON, GREEN, EmptyExpr [defVal=COLOR]], LightAction [3, ON, BLUE, EmptyExpr [defVal=COLOR]], LightAction [4, OFF, RED, EmptyExpr [defVal=COLOR]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_LightSensorAction.xml");
    }

    @Test
    public void getLight() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_LightSensorAction.xml");
        LightAction<Void> la = (LightAction<Void>) forest.get(0).get(1);
        Assert.assertEquals(BrickLedColor.RED, la.getColor());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_LightSensorAction.xml");

        LightAction<Void> cs = (LightAction<Void>) forest.get(0).get(1);
        LightAction<Void> cs1 = (LightAction<Void>) forest.get(0).get(2);
        LightAction<Void> cs2 = (LightAction<Void>) forest.get(0).get(3);
        LightAction<Void> cs3 = (LightAction<Void>) forest.get(0).get(4);

        Assert.assertEquals("1", cs.getPort());
        Assert.assertEquals("2", cs1.getPort());
        Assert.assertEquals("3", cs2.getPort());
        Assert.assertEquals("4", cs3.getPort());
    }

    @Test
    public void getState() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_LightSensorAction.xml");
        LightAction<Void> st = (LightAction<Void>) forest.get(0).get(1);
        LightAction<Void> st1 = (LightAction<Void>) forest.get(0).get(4);
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
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_LightSensorAction.xml");
    }
}
