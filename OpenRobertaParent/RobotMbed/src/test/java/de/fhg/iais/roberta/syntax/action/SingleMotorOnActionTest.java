package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class SingleMotorOnActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfSingleMotorOnActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=138], "
                + "MainTask [], "
                + "SingleMotorOnAction [NumConst [0]], "
                + "SingleMotorOnAction [NumConst [14]]]]]";

        String result = this.h.generateTransformerString("/action/single_motor_on.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingColor_InstanceOfSingleMotorOnActionClassWithMissingMotorPower() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=13, y=138], " + "MainTask [], " + "SingleMotorOnAction [EmptyExpr [defVal=NUMBER_INT]]]]]";

        String result = this.h.generateTransformerString("/action/single_motor_on_missing_power.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/single_motor_on.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/single_motor_on_missing_power.xml");
    }
}
