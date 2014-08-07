package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class LightActionStatusTest {
    @Test
    public void ledOff() throws Exception {
        String a = "\nhal.ledOff();";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_BrickLightStatus.xml"));
    }

    @Test
    public void resetLED() throws Exception {
        String a = "\nhal.resetLED();";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_BrickLightStatus1.xml"));
    }
}
