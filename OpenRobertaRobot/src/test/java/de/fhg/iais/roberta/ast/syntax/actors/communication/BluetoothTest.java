package de.fhg.iais.roberta.ast.syntax.actors.communication;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class BluetoothTest {
    @Test
    public void connection() throws Exception {
        String a = "NXTConnectionvariablenName=hal.establishConnectionTo(\"101010\");publicvoidrun(){}";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void connectionWait() throws Exception {
        String a = "NXTConnectionvariablenName=hal.waitForConnection();publicvoidrun(){}";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void send() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.establishConnectionTo(\"\");publicvoidrun(){hal.sendMessage(\"\");}";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BluetoothSend.xml");
    }

    @Test
    public void recive() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.waitForConnection();publicvoidrun(){hal.drawText(String.valueOf(hal.readMessage()),0,0);}";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BluetoothReceive.xml");
    }
}
