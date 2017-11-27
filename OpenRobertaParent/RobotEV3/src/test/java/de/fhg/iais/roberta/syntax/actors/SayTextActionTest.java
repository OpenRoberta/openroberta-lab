package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class SayTextActionTest {
    Helper h = new Helper();

    @Test
    public void sayText() throws Exception {
        String a = "\nhal.sayText(\"Hello world\");}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_SayText.xml");
    }
}
