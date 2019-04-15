package de.fhg.iais.roberta.ast.syntax.actors.communication;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

@Ignore
public class BluetoothTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void connection() throws Exception {
        String a = "NXTConnectionvariablenName=hal.establishConnectionTo(\"101010\");publicvoidrun(){}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_BluetoothConnection.xml", true);
    }

    @Test
    public void connectionWait() throws Exception {
        String a = "NXTConnectionvariablenName=hal.waitForConnection();publicvoidrun(){}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_BluetoothConnectionWait.xml", true);
    }

    @Test
    public void send() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.establishConnectionTo(\"\");publicvoidrun(){hal.sendMessage(\"\", variablenName2);}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_BluetoothSend.xml", true);
    }

    @Test
    public void recive() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.waitForConnection();publicvoidrun(){hal.drawText(String.valueOf(hal.readMessage(variablenName2)),0,0);}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_BluetoothReceive.xml", true);
    }
}
