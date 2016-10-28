package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class GestureSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [SensorExpr [GestureSensor [ FACE_DOWN ]]], "
                + "DisplayTextAction [SensorExpr [GestureSensor [ LEFT ]]]"
                + "]]]";

        String result = Helper.generateTransformerString("/sensor/check_gesture.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        //Helper.assertTransformationIsOk("/sensor/check_gesture.xml");
    }

}
