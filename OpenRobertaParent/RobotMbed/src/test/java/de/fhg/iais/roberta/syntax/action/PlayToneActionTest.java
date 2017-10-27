package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class PlayToneActionTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfPlayToneActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=62], MainTask [], PlayNoteAction [ duration=2000, frequency=261.626]]]]";
        String result = this.h.generateTransformerString("/action/play_note.xml");
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/play_note.xml");
    }
}
