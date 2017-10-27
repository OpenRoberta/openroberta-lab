package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.Helper;

public class SetVolumeTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfSetVolumeClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=88], " + "MainTask [], " + "SetVolume [NumConst [50]]]]]";
        
        String result = this.h.generateTransformerString("/action/setVolume.xml");

        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/setVolume.xml");
    }
}