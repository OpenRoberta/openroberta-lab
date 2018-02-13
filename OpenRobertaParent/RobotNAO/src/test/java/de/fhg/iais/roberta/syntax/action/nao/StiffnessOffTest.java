package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class StiffnessOffTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessBodyOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [BODY, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfBodyOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessArmsOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [ARMS, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfArmsOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftArmOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTARM, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLeftArmOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessHeadOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [HEAD, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfHeadOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightArmOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIGHTARM, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfRightArmOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLegsOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEGS, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLegsOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftLegOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTLEG, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfLeftLegOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightLegOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIHTLEG, OFF]]]]";

        String result = this.h.generateTransformerString("/action/stiffnessOfRightLegOff.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/stiffnessOfBodyOff.xml");
    }
}