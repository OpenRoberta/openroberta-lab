package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class LightActionTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-9, y=1], LightAction [NO_PORT, ON, GREEN, EmptyExpr [defVal=COLOR]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BrickLight.xml"));
    }

    //    @Test
    //    public void getColor() throws Exception {
    //        LightAction<?> la = (LightAction<?>) Helper.generateAST("/ast/actions/action_BrickLight.xml");
    //
    //        Assert.assertEquals(BrickLedColor.GREEN, la.getColor());
    //    }
    //
    //    @Test
    //    public void isBlink() throws Exception {
    //        LightAction<?> la = (LightAction<?>) Helper.generateAST("/ast/actions/action_BrickLight.xml");
    //
    //        Assert.assertEquals(BlinkMode.ON, la.getBlinkMode());
    //    }

    @Test
    public void lightAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=46, y=109], LightAction [NO_PORT, DOUBLE_FLASH, GREEN, EmptyExpr [defVal=COLOR]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BrickLight3.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BrickLight.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BrickLight1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BrickLight2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BrickLight3.xml");
    }
}
