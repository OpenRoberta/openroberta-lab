package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class ToneActionFunctionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

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
                + "volume=50*4/100.0+0.5;"
                + "PlayToneEx(300, 100, volume, false);\n"
                + "Wait(100);}";

        this.h.assertWrappedCodeIsOk(a, "/ast/actions/action_PlaySoundFunc.xml");
    }
}
