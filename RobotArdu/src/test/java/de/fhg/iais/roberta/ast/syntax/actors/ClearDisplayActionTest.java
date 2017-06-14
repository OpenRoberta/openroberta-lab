package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

@Ignore
public class ClearDisplayActionTest {
	HelperBotNroll h = new HelperBotNroll();

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ClearDisplay.xml", false);
    }
}