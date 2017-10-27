package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ShowPictureActionTest {
    Helper h = new Helper();

    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(predefinedImages.get(\"EYESOPEN\"), 0, 0);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_ShowPicture.xml");
    }
}
