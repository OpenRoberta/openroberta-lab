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
        String a = "BlockAST [project=[[Location [x=-8, y=105], LightStatusAction [OFF]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLightStatus.xml"));
    }

    @Test
    public void getStatus() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/actions/action_BrickLightStatus.xml");
        LightStatusAction<Void> lsa = (LightStatusAction<Void>) transformer.getTree().get(1);
        Assert.assertEquals(LightStatusAction.Status.OFF, lsa.getStatus());
    }

    @Test
    public void invalidStatus() throws Exception {
        try {
            @SuppressWarnings("unused")
            LightStatusAction<Void> lsa = LightStatusAction.make(Status.valueOf("invalid"), null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.action.LightStatusAction.Status.invalid", e.getMessage());
        }
    }

    @Test
    public void brickLightStatus1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-8, y=105], LightStatusAction [RESET]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BrickLightStatus1.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BrickLightStatus1.xml");
    }

}
