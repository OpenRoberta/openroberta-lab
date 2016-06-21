package de.fhg.iais.roberta.robotCommunication.ev3;

import org.junit.Ignore;

import de.fhg.iais.roberta.robotCommunication.Ev3Communicator;

public class Ev3CompilerWorkflowTest {

    @Ignore
    public void test() throws Exception {
        // FIXME: this needs a property file with paths to pass instead of the nulls
        new Ev3CompilerWorkflow(new Ev3Communicator(), null, null, null).runBuild("1Q2W3E4R", "blinker2", "generated.main");
    }
}
