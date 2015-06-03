package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class ToneActionTest {
    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_PlaySound.xml");
    }
}
