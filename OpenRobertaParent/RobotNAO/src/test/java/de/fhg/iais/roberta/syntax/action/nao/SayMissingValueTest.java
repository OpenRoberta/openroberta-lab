package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.Helper;

public class SayMissingValueTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfSayClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask [], " + "SayText [EmptyExpr [defVal=STRING]]]]]";
        
        String result = this.h.generateTransformerString("/action/say_missingValue.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/say_missingValue.xml");
    }
}