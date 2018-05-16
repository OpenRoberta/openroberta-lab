package de.fhg.iais.roberta.syntax.sensor.vorwerk;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperVorwerkForXmlTest;

public class WallTest {
    private final HelperVorwerkForXmlTest h = new HelperVorwerkForXmlTest();

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