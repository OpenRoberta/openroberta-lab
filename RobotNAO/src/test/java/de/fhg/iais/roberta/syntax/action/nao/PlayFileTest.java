package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.Helper;

public class PlayFileTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask [], " + "PlayFile [StringConst [roberta]]]]]";
        
        String result = this.h.generateTransformerString("/action/playFile.xml");

        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/playFile.xml");
    }
}