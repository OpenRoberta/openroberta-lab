package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DisplayImageActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], "
                + "MainTask [], "
                + "DisplayImageAction [IMAGE, PredefinedImage [HEART]], "
                + "DisplayImageAction [ANIMATION, ListCreate [IMAGE, PredefinedImage [HEART_SMALL], PredefinedImage [ASLEEP]]"
                + "]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/display_image_show_imag_and_animation.xml");

    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=13, y=13], MainTask [], DisplayImageAction [IMAGE, EmptyExpr [defVal=STRING]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/display_image_missing_image_name.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/display_image_missing_image_name.xml");
    }
}
