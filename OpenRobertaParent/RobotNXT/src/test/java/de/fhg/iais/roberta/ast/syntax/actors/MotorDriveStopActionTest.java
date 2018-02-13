package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MotorDriveStopActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void stop() throws Exception {
        final String a =
            "#defineWHEELDIAMETER0.0#defineTRACKWIDTH0.0#defineMAXLINES8#include\"NEPODefs.h\"//containsNEPOdeclarationsfortheNXCNXTAPIresources"
                + "\nOff(OUT_BC);}";

        this.h.assertWrappedCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}