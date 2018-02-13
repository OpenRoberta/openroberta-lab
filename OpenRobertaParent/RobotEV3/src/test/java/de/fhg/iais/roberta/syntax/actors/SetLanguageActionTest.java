package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class SetLanguageActionTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void setLanguage() throws Exception {
        String a = "\nhal.setLanguage(\"de\");}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_SetLanguage.xml");
    }
}
