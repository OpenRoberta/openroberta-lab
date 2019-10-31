package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class TurnActionTest extends Ev3LejosAstTest {

    @Test
    public void turn() throws Exception {
        String a = "\nhal.rotateDirectionRegulated(TurnDirection.RIGHT, 50);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/actions/action_MotorDiffTurn.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void turnFor() throws Exception {
        String a = "\nhal.rotateDirectionAngle(TurnDirection.RIGHT, 50, 20);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/actions/action_MotorDiffTurnFor.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}