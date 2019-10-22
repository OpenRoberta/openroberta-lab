package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorDriveStopActionTest extends NxtAstTest {

    @Test
    public void stop() throws Exception {
        final String a =
            "#defineWHEELDIAMETER5.6#defineTRACKWIDTH11.0#defineMAXLINES8#include\"NEPODefs.h\"//containsNEPOdeclarationsfortheNXCNXTAPIresources"
                + "\nOff(OUT_BC);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_Stop.xml", brickConfigurationBC, true);
    }
}