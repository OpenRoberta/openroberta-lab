package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class SetLanguageActionTest {
    Helper h = new Helper();

    @Test
    public void setLanguage() throws Exception {
        String a = "\nhal.setLanguage(\"de\");}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_SetLanguage.xml");
    }
}
