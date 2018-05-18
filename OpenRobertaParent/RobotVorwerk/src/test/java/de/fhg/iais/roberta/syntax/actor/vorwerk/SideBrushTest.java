package de.fhg.iais.roberta.syntax.actor.vorwerk;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperVorwerkForXmlTest;

public class SideBrushTest {
    private final HelperVorwerkForXmlTest h = new HelperVorwerkForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=384, y=50], " + "MainTask [], SideBrush [ON], SideBrush [OFF]]]]";

        String result = this.h.generateTransformerString("/actors/side_brush.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/actors/side_brush.xml");
    }
}