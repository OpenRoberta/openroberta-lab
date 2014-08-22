package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.drawText(\"Hallo\", 0, 0);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_ShowText.xml"));

    }
}
