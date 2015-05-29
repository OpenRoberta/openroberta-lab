package de.fhg.iais.roberta.robotCommunication.ev3;

import org.junit.Ignore;

public class CompilerWorkflowTest {

    @Ignore
    public void test() {
        new Ev3CompilerWorkflow(null, null, null).runBuild("1Q2W3E4R", "blinker2", "generated.main");
    }
}
