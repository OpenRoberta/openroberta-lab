package de.fhg.iais.roberta.syntax.sensor.raspberrypi;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.raspberrypi.HelperRaspberryPiForXmlTest;

@Ignore
public class WallTest {
    private final HelperRaspberryPiForXmlTest h = new HelperRaspberryPiForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, item, NumConst [0], false, true]], \n"
                + "Var [item] := SensorExpr [WallSensor [NO_PORT, DISTANCE, EMPTY_SLOT]]\n"
                + "]]]";

        String result = this.h.generateTransformerString("/sensors/wall.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensors/wall.xml");
    }
}