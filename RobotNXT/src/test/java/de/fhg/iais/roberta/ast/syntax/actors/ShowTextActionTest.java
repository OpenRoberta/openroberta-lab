package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        final String a = "\nTextOut(0,(MAXDISPLAYPIXELS-(abs(0-1)*PIXELPERLINE)%(MAXDISPLAYPIXELS+PIXELPERLINE)),\"Hallo\");";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml");

    }
}
