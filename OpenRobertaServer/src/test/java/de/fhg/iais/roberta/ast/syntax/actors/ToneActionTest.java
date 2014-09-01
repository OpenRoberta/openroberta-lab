package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ToneActionTest {
    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_PlaySound.xml"));
    }
}
