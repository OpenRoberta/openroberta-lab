package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore
public class ClearDisplayActionTest extends NxtAstTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_ClearDisplay.xml", false);
    }
}