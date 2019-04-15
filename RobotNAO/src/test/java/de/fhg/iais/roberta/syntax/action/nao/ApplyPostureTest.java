package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class ApplyPostureTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureCrouchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [CROUCH]]]]";

        String result = this.h.generateTransformerString("/action/posture_crouch.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureLayingBackClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [LYINGBACK]]]]";

        String result = this.h.generateTransformerString("/action/posture_lyingBack.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureLyingBellyClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=113, y=88], " + "MainTask [], " + "ApplyPosture [LYINGBELLY]]]]";

        String result = this.h.generateTransformerString("/action/posture_lyingBelly.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureSitClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [SIT]]]]";

        String result = this.h.generateTransformerString("/action/posture_sit.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureSitRelaxClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [SITRELAX]]]]";

        String result = this.h.generateTransformerString("/action/posture_sitRelax.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STAND]]]]";

        String result = this.h.generateTransformerString("/action/posture_stand.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandInitClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STANDINIT]]]]";

        String result = this.h.generateTransformerString("/action/posture_standInit.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfApplyPostureStandZeroClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "ApplyPosture [STANDZERO]]]]";

        String result = this.h.generateTransformerString("/action/posture_standZero.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/posture_crouch.xml");
    }
}