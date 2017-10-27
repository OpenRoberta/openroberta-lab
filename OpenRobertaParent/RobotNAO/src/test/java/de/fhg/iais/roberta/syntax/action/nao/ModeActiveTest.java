package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.Helper;

public class ModeActiveTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfSetModeClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "Mode [ACTIVE]]]]";
        
        String result = this.h.generateTransformerString("/action/setModeActive.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/setModeActive.xml");
    }
}