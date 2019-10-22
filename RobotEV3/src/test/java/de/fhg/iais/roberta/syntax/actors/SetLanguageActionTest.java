package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetLanguageActionTest extends Ev3LejosAstTest {

    @Test
    public void setLanguage() throws Exception {
        String a = "\nhal.setLanguage(\"de\");}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_SetLanguage.xml", makeStandard(), false);
    }
}
