package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowPictureActionTest {
    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(ShowPicture.EYESOPEN, 0, 0);";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_ShowPicture.xml");
    }
}
