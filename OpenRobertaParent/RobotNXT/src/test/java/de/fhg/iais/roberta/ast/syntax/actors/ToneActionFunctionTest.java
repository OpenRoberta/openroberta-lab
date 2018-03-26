package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ToneActionFunctionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void playTone() throws Exception {
        final String a =
            "#defineWHEELDIAMETER0.0#defineTRACKWIDTH0.0#defineMAXLINES8#include\"NEPODefs.h\"//containsNEPOdeclarationsfortheNXCNXTAPIresources"
                + "void macheEtwas();"
                + "byte volume = 0x02;\n"
                + "float Element;"
                + "taskmain(){ "
                + "Element=0;"
                + "macheEtwas();}"
                + "void macheEtwas(){\n"
                + "volume=(50)*4/100.0;"
                + "PlayToneEx(300, 100, volume, false);\n"
                + "Wait(100);}";

        this.h.assertWrappedCodeIsOk(a, "/ast/actions/action_PlaySoundFunc.xml");
    }
}
