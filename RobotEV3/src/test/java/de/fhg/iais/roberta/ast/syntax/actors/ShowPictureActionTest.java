package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class ShowPictureActionTest {
    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(ShowPicture.EYESOPEN, 0, 0);";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_ShowPicture.xml");
    }
}
