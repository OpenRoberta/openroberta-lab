package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class LightActionTest {
    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(BrickLedColor.GREEN, true);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_BrickLight.xml"));
    }
}
