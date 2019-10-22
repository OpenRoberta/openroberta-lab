package de.fhg.iais.roberta.ast.methods;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MethodIfReturnTest extends AstTest {

    @Test
    public void methodIfReturn1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], false, false], "
                + "MethodStmt [MethodIfReturn [SensorExpr [TouchSensor [1, DEFAULT, NO_SLOT]], BOOLEAN, BoolConst [false]]], BOOLEAN, EmptyExpr [defVal=NULL]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_if_return_1.xml");

    }

    @Test
    public void methodIfReturn2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=44, y=104], MethodReturn [Funktionsblock,"
                + " VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false],"
                + " VarDeclaration [NUMBER, x2, EmptyExpr [defVal=NUMBER], true, false],"
                + " VarDeclaration [NUMBER, x3, EmptyExpr [defVal=NUMBER], false, false], "
                + "\nAktionStmt [LightAction [NO_PORT, ON, DEFAULT, EmptyExpr [defVal=COLOR]]]MethodStmt [MethodIfReturn [Binary [EQ, NumConst [0], NumConst [0]], NUMBER, Var [x2]]], NUMBER, Var [x3]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_if_return_2.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_if_return_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_if_return_2.xml");
    }

}
