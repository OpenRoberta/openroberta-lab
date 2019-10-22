package de.fhg.iais.roberta.sound;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SayTextActionTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSayTextActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "SayTextAction [StringConst [Hello], NumConst [100], NumConst [100]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/ast/actions/action_SayText.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_SayText.xml");
    }

    @Test
    public void make_MissingValue_ReturnInstanceOfSayClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=138], "
                + "MainTask [], "
                + "SayTextAction [EmptyExpr [defVal=STRING], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/ast/actions/action_SayTextMissing.xml");
    }

    @Test
    public void missingValueAstToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_SayTextMissing.xml");
    }
}