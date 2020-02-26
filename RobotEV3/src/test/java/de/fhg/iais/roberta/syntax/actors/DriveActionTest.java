package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedMethodCollectorWorker;

public class DriveActionTest extends Ev3LejosAstTest {

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(DriveDirection.FOREWARD, 50);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/actions/action_MotorDiffOn.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3UsedMethodCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(DriveDirection.FOREWARD, 50, 20);}";

        UnitTestHelper
            .checkWorkers(testFactory, a, "/syntax/actions/action_MotorDiffOnFor.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3UsedMethodCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}