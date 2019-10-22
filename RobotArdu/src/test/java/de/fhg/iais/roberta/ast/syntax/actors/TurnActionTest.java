package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnActionTest extends BotnrollAstTest {

    @Test
    public void turn() throws Exception {
        final String a = "\none.movePID(50,-50);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffTurn.xml", makeConfiguration(), false);
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "\nbnr.moveTimePID(50,-50,20);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/ast/actions/action_MotorDiffTurnFor.xml",
                makeConfiguration(),
                false);
    }
}