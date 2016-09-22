package de.fhg.iais.roberta.robotCommunication;

import org.junit.Ignore;

import de.fhg.iais.roberta.factory.Ev3CompilerWorkflow;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

public class Ev3CompilerWorkflowTest {

    @Ignore
    public void test() throws Exception {
        // FIXME: this needs a property file with paths to pass instead of the nulls
        new Ev3CompilerWorkflow(new RobotCommunicator(), null, null, null).runBuild("1Q2W3E4R", "blinker2", "generated.main");
    }
}
