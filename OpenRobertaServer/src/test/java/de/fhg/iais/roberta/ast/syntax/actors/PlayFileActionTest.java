package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class PlayFileActionTest {
    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml");
    }
}
