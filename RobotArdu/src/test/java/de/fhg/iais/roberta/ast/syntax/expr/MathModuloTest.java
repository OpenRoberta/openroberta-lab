package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MathModuloTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            ""
                + "doublevariablenName;"
                + "voidsetup()"
                + "{Wire.begin();Serial.begin(9600);"
                + "//setsbaudrateto9600bpsforprintingvaluesatserialmonitor.one.spiConnect(SSPIN);//startstheSPIcommunicationmodulebrm.i2cConnect(MODULE_ADDRESS);//startsI2Ccommunicationbrm.setModuleAddress(0x2C);one.stop();bnr.setOne(one);bnr.setBrm(brm);"
                + "variablenName=fmod(1,0);}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml", false);
    }

}
