package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowTextActionTest extends BotnrollAstTest {

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\none.lcd1(\"Hallo\");";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_ShowText.xml", false);

    }
}
