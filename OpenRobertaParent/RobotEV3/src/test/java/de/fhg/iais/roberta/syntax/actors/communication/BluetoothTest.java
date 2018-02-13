package de.fhg.iais.roberta.syntax.actors.communication;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class BluetoothTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void connection() throws Exception {
        String a = "NXTConnectionvariablenName=hal.establishConnectionTo(\"101010\");publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void connectionWait() throws Exception {
        String a = "NXTConnectionvariablenName=hal.waitForConnection();publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void send() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.establishConnectionTo(\"\");publicvoidrun()throwsException{hal.sendMessage(\"\", variablenName2);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BluetoothSend.xml");
    }

    @Test
    public void recive() throws Exception {
        String a =
            "NXTConnectionvariablenName2=hal.waitForConnection();publicvoidrun()throwsException{hal.drawText(String.valueOf(hal.readMessage(variablenName2)),0,0);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BluetoothReceive.xml");
    }
}
