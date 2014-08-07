package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class VolumeActionTest {
    @Test
    public void setVolume() throws Exception {
        String a = "\nhal.setVolume(50);";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_SetVolume.xml"));
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nhal.getVolume()";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_GetVolume.xml"));
    }
}
