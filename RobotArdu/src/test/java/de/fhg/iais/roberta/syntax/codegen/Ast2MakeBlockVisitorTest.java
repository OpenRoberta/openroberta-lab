package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperMakeBlock;

public class Ast2MakeBlockVisitorTest {
    HelperMakeBlock h = new HelperMakeBlock();

    private static final String MAIN_METHOD1 = ""
        + "#include <math.h> \n"
        + "#include <MeOrion.h> \n"
        + "#include <Wire.h>\n"
        + "#include <SoftwareSerial.h>\n"
        + "#include <CountUpDownTimer.h>\n"
        + "#include <RobertaFunctions.h>\n"
        + "#include \"MeDrive.h\"\n\n";

    private static final String MAIN_METHOD2 = "" + "void setup(){" + "Serial.begin(9600);";

    @Test
    public void motor1m1Test() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "        motor1.run(30);\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/motor1m1.xml", true);
    }

}
