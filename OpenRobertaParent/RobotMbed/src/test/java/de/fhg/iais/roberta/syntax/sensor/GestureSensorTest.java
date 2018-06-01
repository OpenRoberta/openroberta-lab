package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class GestureSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Ignore("Test is ignored until next commit")
    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [GestureSensor [ FACE_DOWN ]]], "
                + "DisplayTextAction [TEXT, SensorExpr [GestureSensor [ LEFT ]]]"
                + "]]]";

        String result = this.h.generateTransformerString("/sensor/check_gesture.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/check_gesture.xml");
    }

}
