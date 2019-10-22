package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore
public class ClearDisplayActionTest extends AstTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_ClearDisplay.xml", false);
    }
}