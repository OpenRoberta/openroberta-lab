package de.fhg.iais.roberta.ast.methods;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MethodReturnTest extends AstTest {

    @Test
    public void methodReturn1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false], "
                + "VarDeclaration [NUMBER, x2, EmptyExpr [defVal=NUMBER], false, false], \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]], NUMBER, Var [x2]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_return_1.xml");

    }

    @Test
    public void methodReturn2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, , \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]], NUMBER, MathConst [PI]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_return_2.xml");

    }

    @Test
    public void methodReturn3() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, , , BOOLEAN, BoolConst [true]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_return_3.xml");

    }

    @Test
    public void methodReturn4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false], VarDeclaration [ARRAY_NUMBER, x2, EmptyExpr [defVal=ARRAY_NUMBER], true, false], "
                + "VarDeclaration [NUMBER, x3, EmptyExpr [defVal=NUMBER], false, false], , BOOLEAN, BoolConst [true]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_return_4.xml");

    }

    @Test
    public void methodReturn5() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], false, false], , BOOLEAN, EmptyExpr [defVal=NULL]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/methods/method_return_5.xml");

    }

    @Test
    public void reverseTransformation1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_return_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_return_2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_return_3.xml");
    }

    @Test
    public void reverseTransformation4() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/methods/method_return_4.xml");
    }
}
