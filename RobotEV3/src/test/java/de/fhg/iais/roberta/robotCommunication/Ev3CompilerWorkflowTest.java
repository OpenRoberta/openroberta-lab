package de.fhg.iais.roberta.robotCommunication;

import org.junit.Ignore;

import de.fhg.iais.roberta.factory.Ev3lejosCompilerWorkflow;

public class Ev3CompilerWorkflowTest {

    @Ignore
    public void test() throws Exception {
        // FIXME: this needs a property file with paths to pass instead of the nulls
        new Ev3lejosCompilerWorkflow(null, null).runBuild("", "blinker2", "");
    }
}
