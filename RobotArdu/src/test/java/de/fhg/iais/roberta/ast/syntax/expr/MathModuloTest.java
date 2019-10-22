package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathModuloTest extends BotnrollAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            ""
                + "double___variablenName;"
                + "voidsetup()"
                + "{Wire.begin();Serial.begin(9600);"
                + "//setsbaudrateto9600bpsforprintingvaluesatserialmonitor.one.spiConnect(SSPIN);//startstheSPIcommunicationmodulebrm.i2cConnect(MODULE_ADDRESS);//startsI2Ccommunicationbrm.setModuleAddress(0x2C);one.stop();bnr.setOne(one);bnr.setBrm(brm);"
                + "___variablenName=fmod(1,0);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_modulo.xml", makeConfiguration(), false);
    }

}
