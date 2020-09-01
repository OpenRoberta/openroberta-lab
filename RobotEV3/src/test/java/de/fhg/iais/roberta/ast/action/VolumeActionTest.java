package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VolumeActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=135], VolumeAction [SET, NumConst [50]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("NumConst [50]", va.getVolume().toString());
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_SetVolume.xml");
        VolumeAction<Void> va = (VolumeAction<Void>) forest.get(0).get(1);
        Assert.assertEquals(VolumeAction.Mode.SET, va.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            VolumeAction<Void> va = VolumeAction.make(VolumeAction.Mode.valueOf("invalid"), null, null, null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.syntax.action.sound.VolumeAction.Mode.invalid", e.getMessage());
        }
    }

    @Test
    public void getVolumeAction() throws Exception {
        String a = "BlockAST [project=[[Location [x=-2, y=189], VolumeAction [GET, NullConst [null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_GetVolume.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_SetVolume.xml");
    }
}
