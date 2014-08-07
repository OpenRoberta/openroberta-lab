package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class ShowPictureActionTest {
    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(\"SMILEY1\", 0, 0);";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_ShowPicture.xml"));
    }
}
