package de.fhg.iais.roberta.sound;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelper;
import de.fhg.iais.roberta.util.test.Helper;

public class SayTextActionTest {
    Helper h = new GenericHelper();

    @Test
    public void make_ByDefault_ReturnInstanceOfSayTextActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask [], " + "SayTextAction [StringConst [Hello]]]]]";

        String result = this.h.generateTransformerString("/ast/actions/action_SayText.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/ast/actions/action_SayText.xml");
    }

    @Test
    public void make_MissingValue_ReturnInstanceOfSayClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask [], " + "SayTextAction [EmptyExpr [defVal=STRING]]]]]";

        String result = this.h.generateTransformerString("/ast/actions/action_SayTextMissing.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void missingValueAstToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/ast/actions/action_SayTextMissing.xml");
    }
}