package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;

public class BluetoothTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void connection() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothConnectAction [StringConst [101010]]], false, true]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BluetoothConnection.xml"));
    }

    @Test
    public void connectionWait() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothWaitForConnectionAction []], false, true]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BluetoothConnectionWait.xml"));
    }

    @Test
    public void connectionSend() throws Exception {
        String a = "BlockAST [project=[[Location [x=384, y=50], MainTask [], BluetoothSendAction [ConnectConst [2], NumConst [0], 5]]]]";

        System.out.println(this.h.generateTransformerString("/ast/actions/action_BluetoothSend.xml"));
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BluetoothSend.xml"));
    }

    @Test
    public void connectionReceive() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName2, ActionExpr [BluetoothWaitForConnectionAction []], false, true]], "
                + "ShowTextAction [ActionExpr [BluetoothReceiveAction [connection=Var [variablenName2], -1, String]], NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_BluetoothReceive.xml"));
    }

    @Test
    public void reverseTransformatinConnection() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void reverseTransformatinConnectionWait() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void reverseTransformatinSend() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BluetoothSend.xml");
    }

    @Test
    public void reverseTransformatinReceive() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_BluetoothReceive.xml");
    }

}
