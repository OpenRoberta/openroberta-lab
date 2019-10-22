package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ApplyPostureTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureCrouchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [CROUCH]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_crouch.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureLayingBackClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [LYINGBACK]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_lyingBack.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureLyingBellyClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [LYINGBELLY]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_lyingBelly.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureSitClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [SIT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_sit.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureSitRelaxClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [SITRELAX]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_sitRelax.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STAND]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_stand.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandInitClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STANDINIT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_standInit.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandZeroClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STANDZERO]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/posture_standZero.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/posture_crouch.xml");
    }
}