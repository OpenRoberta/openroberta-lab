package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class PlayFileActionTest {
    Helper h = new Helper();

    @Test
    public void playFile() throws Exception {
        String a = "\nPlayFile(1);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml");
    }
}
