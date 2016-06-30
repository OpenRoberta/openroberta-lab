package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class VolumeActionTest {
    @Test
    public void setVolume() throws Exception {
        String a = "\nhal.setVolume(50);";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nhal.getVolume()";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_GetVolume.xml");
    }
}
