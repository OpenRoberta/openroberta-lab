package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayFileActionTest extends Ev3LejosAstTest {

    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_PlayFile.xml", false);
    }
}
