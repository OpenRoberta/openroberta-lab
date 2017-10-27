package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class VolumeActionTest {
    Helper h = new Helper();

    @Test
    public void setVolume() throws Exception {
        String a = "\nvolume=50*4/100.0+0.5;";

        this.h.assertCodeIsOk(a, "/ast/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nvolume*100/4";

        this.h.assertCodeIsOk(a, "/ast/actions/action_GetVolume.xml");
    }
}
