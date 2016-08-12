package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowPictureActionTest {
    @Test
    public void drawPicture() throws Exception {
        final String a = "\n";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowPicture.xml");
    }
}
