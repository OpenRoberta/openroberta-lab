package de.fhg.iais.roberta.syntax.codegen.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Ast2C4ev3VisitorTest {


    private static final String CONSTANTS_AND_IMPORTS =
        "" //
            + "#define WHEEL_DIAMETER 5.6\n"
            + "#define TRACK_WIDTH 18.0\n"
            + "#include <ev3.h>\n"
            + "#include <math.h>\n"
            + "#include \"NEPODefs.h\"\n";

    private static final String BEGIN_MAIN =
        "" //
            + "int main() {\n"
            + "    InitEV3();\n"
            + "    \n";

    private static final String END_MAIN =
        "" //
            + "    \n"
            + "    FreeEV3();\n"
            + "    return 0;\n"
            + "}\n";

    private static Configuration brickConfiguration = HelperEv3ForXmlTest.makeConfiguration();

    private final HelperEv3ForXmlTest helper = new HelperEv3ForXmlTest();

    // TODO: test_0
    // TODO: test_1
    // TODO: test_2
    // TODO: test_3
    // TODO: test_4
    // TODO: test_5
    // TODO: test_6


    @Test
    public void testMotorAction() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN
                + "    OnFwdReg(OUT_B, Speed(30));\n"
                + "    RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + END_MAIN;

        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator7.xml", expectedCode);
    }


    // TODO: test_8
    // TODO: test_9
    // TODO: test_10
    // TODO: test_11
    // TODO: test_12
    // TODO: test_13
    // TODO: test_14
    // TODO: test_15
    // TODO: test_16
    // TODO: test_17
    // TODO: test_18

    private void checkCodeGeneratorForInput(String fileName, String expectedSourceCode) throws Exception {
        String generatedSourceCode = helper.generateC4ev3(fileName, brickConfiguration);
        assertEquals(removeSpaces(expectedSourceCode), removeSpaces(generatedSourceCode));
    }

    private static String removeSpaces (String string) {
        return string.replaceAll("\\s+", "");
    }

}
