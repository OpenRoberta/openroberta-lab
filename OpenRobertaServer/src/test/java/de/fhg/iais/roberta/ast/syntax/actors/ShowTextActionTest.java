package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.drawText(\"Hallo\", (int) 0, (int) 0);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml");

    }
}
