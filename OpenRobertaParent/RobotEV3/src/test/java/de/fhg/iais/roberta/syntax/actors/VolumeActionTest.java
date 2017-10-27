package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class VolumeActionTest {
    Helper h = new Helper();

    @Test
    public void setVolume() throws Exception {
        String a = "\nhal.setVolume(50);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nhal.getVolume()}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_GetVolume.xml");
    }
}
