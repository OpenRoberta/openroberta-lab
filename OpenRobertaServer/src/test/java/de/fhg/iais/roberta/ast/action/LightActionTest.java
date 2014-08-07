package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.Color;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class LightActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[LightAction [GREEN, true]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_BrickLight.xml"));
    }

    @Test
    public void getColor() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_BrickLight.xml");

        LightAction la = (LightAction) transformer.getTree().get(0);

        Assert.assertEquals(Color.GREEN, la.getColor());
    }

    @Test
    public void isBlink() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_BrickLight.xml");

        LightAction la = (LightAction) transformer.getTree().get(0);

        Assert.assertEquals(true, la.isBlink());
    }
}
