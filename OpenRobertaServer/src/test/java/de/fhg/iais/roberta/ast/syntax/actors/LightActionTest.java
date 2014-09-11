package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class LightActionTest {
    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(BrickLedColor.GREEN, true);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BrickLight.xml");
    }
}
