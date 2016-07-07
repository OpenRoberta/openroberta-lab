package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowPictureActionTest {
    @Test
    public void drawPicture() throws Exception {
        final String a = "\nGraphicOut(0, 0,\"EYESOPEN\");";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowPicture.xml");
    }
}
