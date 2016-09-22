package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class VolumeActionTest {
    @Test
    public void setVolume() throws Exception {
        String a = "\nvolume=50*4/100.0+0.5;";

        Helper.assertCodeIsOk(a, "/ast/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nvolume*100/4";

        Helper.assertCodeIsOk(a, "/ast/actions/action_GetVolume.xml");
    }
}
