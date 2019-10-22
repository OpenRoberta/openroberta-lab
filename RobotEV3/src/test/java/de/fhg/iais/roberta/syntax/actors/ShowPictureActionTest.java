package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowPictureActionTest extends Ev3LejosAstTest {

    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(predefinedImages.get(\"EYESOPEN\"), 0, 0);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_ShowPicture.xml", false);
    }
}
