package de.fhg.iais.roberta.sound;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelper;
import de.fhg.iais.roberta.util.test.Helper;

public class PlayNoteActionTest {
    Helper h = new GenericHelper();

    @Test
    public void make_ByDefault_ReturnInstanceOfPlayNoteActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=62], MainTask [], PlayNoteAction [ duration=2000, frequency=261.626]]]]";
        String result = this.h.generateTransformerString("/ast/actions/action_PlayNote.xml");
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_PlayNote.xml");
    }
}
