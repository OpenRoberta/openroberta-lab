package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class FsrTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [FsrSensor [LEFT, VALUE, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [FsrSensor [RIGHT, VALUE, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        String result = this.h.generateTransformerString("/sensor/fsr.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/fsr.xml");
    }
}