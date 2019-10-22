package de.fhg.iais.roberta.sound;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayNoteActionTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPlayNoteActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=62], MainTask [], PlayNoteAction [ duration=2000, frequency=261.626]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/ast/actions/action_PlayNote.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_PlayNote.xml");
    }
}
