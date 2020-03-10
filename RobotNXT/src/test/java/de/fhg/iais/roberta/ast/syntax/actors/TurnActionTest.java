package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnActionTest extends NxtAstTest {

    @Test
    public void turn() throws Exception {
        final String a = "OnFwdSync(OUT_BC,MIN(MAX(50, -100), 100), -100);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffTurn.xml", brickConfigurationBC, false);
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "RotateMotorEx(OUT_BC,MIN(MAX(50, -100), 100), ( 20 * TRACKWIDTH / WHEELDIAMETER), -100, true, true );\nWait( 1 );";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/ast/actions/action_MotorDiffTurnFor.xml",
                brickConfigurationBC,
                false);
    }
}