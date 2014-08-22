package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.Color;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class LightActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[LightAction [GREEN, true]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLight.xml"));
    }

    @Test
    public void getColor() throws Exception {
        LightAction la = (LightAction) Helper.generateAST("/ast/actions/action_BrickLight.xml");

        Assert.assertEquals(Color.GREEN, la.getColor());
    }

    @Test
    public void isBlink() throws Exception {
        LightAction la = (LightAction) Helper.generateAST("/ast/actions/action_BrickLight.xml");

        Assert.assertEquals(true, la.isBlink());
    }
}
