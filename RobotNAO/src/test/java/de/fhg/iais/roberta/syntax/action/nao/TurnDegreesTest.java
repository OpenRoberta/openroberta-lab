package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TurnDegreesTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTurnDegreesActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "TurnDegrees [LEFT, NumConst [20]]]]]";

        String result = Helper.generateTransformerString("/action/turn_left.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingDistance_InstanceOfTurnDegressClassWithMissingMessage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "TurnDegrees [RIGHT, EmptyExpr [defVal=class java.lang.Integer]]]]]";

        String result = Helper.generateTransformerString("/action/turn_right_missing_degrees.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/turn_left.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/turn_right_missing_degrees.xml");
    }
}
