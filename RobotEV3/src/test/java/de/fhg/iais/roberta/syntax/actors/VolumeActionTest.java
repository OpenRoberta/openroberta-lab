package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VolumeActionTest extends Ev3LejosAstTest {

    @Test
    public void setVolume() throws Exception {
        String a = "\nhal.setVolume(50);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_SetVolume.xml", false);
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nhal.getVolume()}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_GetVolume.xml", false);
    }
}
