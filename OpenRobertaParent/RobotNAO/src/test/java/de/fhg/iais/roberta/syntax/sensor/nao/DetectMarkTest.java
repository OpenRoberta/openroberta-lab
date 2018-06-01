package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class DetectMarkTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoMarkClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=232, y=251], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [DetectedMark [NO_PORT, IDONE, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [DetectedMark [NO_PORT, IDALL, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        String result = this.h.generateTransformerString("/sensor/detectmark.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/sensor/detectmark.xml");
    }
}