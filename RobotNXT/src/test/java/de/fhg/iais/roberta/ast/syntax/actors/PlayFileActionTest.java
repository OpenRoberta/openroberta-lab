package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayFileActionTest extends NxtAstTest {

    @Test
    public void playFile() throws Exception {
        String a = "\nPlayFile(1);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_PlayFile.xml", false);
    }
}
