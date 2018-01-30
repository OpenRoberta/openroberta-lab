package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nao.HelperNaoForTest;

public class SetLedIntensityTest {
    HelperNaoForTest h = new HelperNaoForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void make_ByDefault_ReturnInstanceOfSetLedIntensityClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "SetLeds [EARS, NumConst [2], ]]]]";
        
        String result = this.h.generateTransformerString("/action/setLedIntensity.xml");

        Assert.assertEquals(expectedResult, result);
    }
    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/setLedIntensity.xml");
    }*/
}