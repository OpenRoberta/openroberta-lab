package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class SingleMotorStopActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfSingleMotorStopActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=63], "
                + "MainTask [], "
                + "SingleMotorStopAction [FLOAT], "
                + "SingleMotorStopAction [NONFLOAT], "
                + "SingleMotorStopAction [SLEEP]]]]";

        String result = this.h.generateTransformerString("/action/single_motor_stop.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/single_motor_stop.xml");
    }

}
