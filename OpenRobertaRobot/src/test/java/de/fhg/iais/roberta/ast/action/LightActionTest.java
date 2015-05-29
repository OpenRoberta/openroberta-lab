package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.action.ev3.BlinkMode;
import de.fhg.iais.roberta.shared.action.ev3.BrickLedColor;
import de.fhg.iais.roberta.syntax.action.ev3.LightAction;
import de.fhg.iais.roberta.testutil.Helper;

public class LightActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-9, y=1], LightAction [GREEN, ON]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLight.xml"));
    }

    @Test
    public void getColor() throws Exception {
        LightAction<?> la = (LightAction<?>) Helper.generateAST("/ast/actions/action_BrickLight.xml");

        Assert.assertEquals(BrickLedColor.GREEN, la.getColor());
    }

    @Test
    public void isBlink() throws Exception {
        LightAction<?> la = (LightAction<?>) Helper.generateAST("/ast/actions/action_BrickLight.xml");

        Assert.assertEquals(BlinkMode.ON, la.getBlinkMode());
    }

    @Test
    public void lightAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=46, y=109], LightAction [GREEN, DOUBLE_FLASH]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLight3.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLight.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLight1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLight2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLight3.xml");
    }
}
