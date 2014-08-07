package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class PlayFileActionTest {
    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(\"SOUNDFILE2\");";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_PlayFile.xml"));
    }
}
