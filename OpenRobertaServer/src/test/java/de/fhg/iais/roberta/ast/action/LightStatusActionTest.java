package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction.Status;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class LightStatusActionTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[LightStatusAction [OFF]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLightStatus.xml"));
    }

    @Test
    public void getStatus() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_BrickLightStatus.xml");
        LightStatusAction<Void> lsa = (LightStatusAction<Void>) transformer.getTree().get(0);
        Assert.assertEquals(LightStatusAction.Status.OFF, lsa.getStatus());
    }

    @Test
    public void invalidStatus() throws Exception {
        try {
            @SuppressWarnings("unused")
            LightStatusAction<Void> lsa = LightStatusAction.make(Status.valueOf("invalid"), false, "");
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.action.LightStatusAction.Status.invalid", e.getMessage());
        }
    }

    @Test
    public void brickLightStatus1() throws Exception {
        String a = "BlockAST [project=[[LightStatusAction [RESET]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLightStatus1.xml"));
    }

}
