package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class ClearDisplayActionTest {
    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.clearDisplay();";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_ClearDisplay.xml"));
    }
}
