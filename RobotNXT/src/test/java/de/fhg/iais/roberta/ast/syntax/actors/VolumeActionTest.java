package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class VolumeActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void setVolume() throws Exception {
        String a = "\nvolume=(50)*4/100.0;";

        this.h.assertCodeIsOk(a, "/ast/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nvolume*100/4";

        this.h.assertCodeIsOk(a, "/ast/actions/action_GetVolume.xml");
    }
}
