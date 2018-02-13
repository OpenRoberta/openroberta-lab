package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class StiffnessOnTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessBodyOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [BODY, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfBodyOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessArmsOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [ARMS, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfArmsOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftArmOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTARM, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLeftArmOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessHeadOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [HEAD, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfHeadOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightArmOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIGHTARM, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfRightArmOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLegsOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEGS, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLegsOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftLegOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTLEG, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLeftLegOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightLegOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIHTLEG, ON]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfRightLegOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/stiffnessOfBodyOn.xml");
    }
}