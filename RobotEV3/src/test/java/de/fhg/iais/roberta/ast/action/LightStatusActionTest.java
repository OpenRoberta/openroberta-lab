package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction.Status;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightStatusActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-8, y=105], LightStatusAction [0, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void getStatus() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_BrickLightStatus.xml");
        LightStatusAction<Void> lsa = (LightStatusAction<Void>) forest.get(0).get(1);
        Assert.assertEquals(LightStatusAction.Status.OFF, lsa.getStatus());
    }

    @Test
    public void invalidStatus() throws Exception {
        try {
            @SuppressWarnings("unused")
            LightStatusAction<Void> lsa = LightStatusAction.make(null, Status.valueOf("invalid"), null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.syntax.action.light.LightStatusAction.Status.invalid", e.getMessage());
        }
    }

    @Test
    public void brickLightStatus1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-8, y=105], LightStatusAction [0, RESET]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BrickLightStatus1.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BrickLightStatus1.xml");
    }

}
