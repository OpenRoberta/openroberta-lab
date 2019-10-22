package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;

public class MotorDriveStopActionTest extends Ev3LejosAstTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive();}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/actions/action_Stop.xml", new Ev3JavaGeneratorWorker());
    }
}