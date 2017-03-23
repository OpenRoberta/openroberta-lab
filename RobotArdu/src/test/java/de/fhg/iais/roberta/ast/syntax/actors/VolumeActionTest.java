package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class VolumeActionTest {
    Helper h = new Helper();

    @Test
    public void setVolume() throws Exception {
        String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_SetVolume.xml", false);
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_GetVolume.xml", false);
    }
}
