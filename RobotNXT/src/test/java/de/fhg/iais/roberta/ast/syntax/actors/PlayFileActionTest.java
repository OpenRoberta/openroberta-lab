package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class PlayFileActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void playFile() throws Exception {
        String a = "\nPlayFile(1);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlayFile.xml");
    }
}
