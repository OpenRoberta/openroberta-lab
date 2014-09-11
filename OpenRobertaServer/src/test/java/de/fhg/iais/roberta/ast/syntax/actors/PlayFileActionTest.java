package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class PlayFileActionTest {
    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(\"SOUNDFILE2\");";

        Helper.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml");
    }
}
