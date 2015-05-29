package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class BluetoothTest {

    @Test
    public void connection() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothConnectAction [StringConst [101010]]], false, true]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BluetoothConnection.xml"));
    }

    @Test
    public void connectionWait() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothWaitForConnectionAction []], false, true]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BluetoothConnectionWait.xml"));
    }

    @Test
    public void connectionSend() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName2, ActionExpr [BluetoothConnectAction [StringConst []]], false, true]], "
                + "BluetoothSendAction [Var [variablenName2], StringConst []]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BluetoothSend.xml"));
    }

    @Test
    public void connectionReceive() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName2, ActionExpr [BluetoothWaitForConnectionAction []], false, true]], "
                + "ShowTextAction [ActionExpr [ReceiveData[]], NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_BluetoothReceive.xml"));
    }

    @Test
    public void reverseTransformatinConnection() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void reverseTransformatinConnectionWait() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void reverseTransformatinSend() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BluetoothSend.xml");
    }

    @Test
    public void reverseTransformatinReceive() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_BluetoothReceive.xml");
    }

}
