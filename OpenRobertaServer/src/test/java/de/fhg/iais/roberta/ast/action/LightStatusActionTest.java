package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction.Status;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class LightStatusActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[LightStatusAction [OFF]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_BrickLightStatus.xml"));
    }

    @Test
    public void getStatus() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_BrickLightStatus.xml");

        LightStatusAction lsa = (LightStatusAction) transformer.getTree().get(0);

        Assert.assertEquals(LightStatusAction.Status.OFF, lsa.getStatus());
    }

    @Test
    public void invalidStatus() throws Exception {
        try {
            @SuppressWarnings("unused")
            LightStatusAction lsa = LightStatusAction.make(Status.valueOf("invalid"));
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.action.LightStatusAction.Status.invalid", e.getMessage());
        }
    }

    @Test
    public void brickLightStatus1() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/actions/action_BrickLightStatus1.xml");

        String a = "BlockAST [project=[[LightStatusAction [RESET]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

}
