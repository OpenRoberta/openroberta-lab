package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        final String a = "\nTextOut(0,(MAXLINES - 0) * MAXLINES,\"Hallo\");";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml");
    }

    @Test
    public void showHelloWorldActionCode() throws Exception {
        final String a = "!!!Hello World!!!" + "!!!Hello World!!!";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowHelloWorld.xml");
    }
}
