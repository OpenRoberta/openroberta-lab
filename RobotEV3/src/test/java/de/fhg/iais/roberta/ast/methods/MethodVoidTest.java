package de.fhg.iais.roberta.ast.methods;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MethodVoidTest extends AstTest {

    @Test
    public void methodVoid1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=103, y=38], MethodVoid [do something, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], false, false], \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_void_1.xml");

    }

    @Test
    public void methodVoid2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=103, y=38], MethodVoid [do something, , \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_void_2.xml");

    }

    @Test
    public void reverseTransformation1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_void_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_void_2.xml");
    }
}
