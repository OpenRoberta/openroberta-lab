package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionFunctionTest extends NxtAstTest {

    @Test
    public void playTone() throws Exception {
        final String a =
            DEFINES_INCLUDES
                + "void macheEtwas();"
                + "byte volume = 0x02;\n"
                + "float ___Element;"
                + "taskmain(){ "
                + "___Element=0;"
                + "macheEtwas();}"
                + "void macheEtwas(){\n"
                + "volume=(50)*4/100.0;"
                + "PlayToneEx(300, 100, volume, false);\n"
                + "Wait(100);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_PlaySoundFunc.xml", brickConfiguration, true);
    }
}
