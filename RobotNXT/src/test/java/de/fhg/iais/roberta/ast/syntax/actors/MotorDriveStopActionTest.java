package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorDriveStopActionTest extends NxtAstTest {

    @Test
    public void stop() throws Exception {
        final String a =
            DEFINES_INCLUDES
                + "\nOff(OUT_BC);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_Stop.xml", brickConfigurationBC, true);
    }
}