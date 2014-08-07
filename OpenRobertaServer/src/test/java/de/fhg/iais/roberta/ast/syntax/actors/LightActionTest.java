package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class LightActionTest {
    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(GREEN, true);";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_BrickLight.xml"));
    }
}
