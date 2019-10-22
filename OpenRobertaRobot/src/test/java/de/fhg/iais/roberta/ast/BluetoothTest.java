package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BluetoothTest extends AstTest {

    @Test
    public void connectionTest() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothConnectAction [StringConst [101010]]], false, true]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void connectionWaitTest() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName, ActionExpr [BluetoothWaitForConnectionAction []], false, true]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void connectionSendTest() throws Exception {
        String a = "BlockAST [project=[[Location [x=384, y=50], MainTask [], BluetoothSendAction [ConnectConst [2], NumConst [0], 5]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BluetoothSend.xml");
    }

    @Test
    public void connectionReceiveTest() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=97, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [CONNECTION, variablenName2, ActionExpr [BluetoothWaitForConnectionAction []], false, true]], "
                + "ShowTextAction [ActionExpr [BluetoothReceiveAction [connection=Var [variablenName2], -1, String]], NumConst [0], NumConst [0]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_BluetoothReceive.xml");
    }

    @Test
    public void reverseTransformationTest() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BluetoothConnection.xml");
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BluetoothConnectionWait.xml");
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BluetoothSend.xml");
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_BluetoothReceive.xml");
    }
}
