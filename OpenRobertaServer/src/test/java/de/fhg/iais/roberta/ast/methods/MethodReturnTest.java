package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MethodReturnTest {
    @Test
    public void methodReturn1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], true, false], "
                + "VarDeclaration [NUMERIC, x2, EmptyExpr [defVal=class java.lang.Integer], false, false], \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]], NUMERIC, Var [x2]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_return_1.xml"));

    }

    @Test
    public void methodReturn2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, , \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]], NUMERIC, MathConst [PI]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_return_2.xml"));

    }

    @Test
    public void methodReturn3() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, , , BOOL, BoolConst [true]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_return_3.xml"));

    }

    @Test
    public void methodReturn4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], true, false], VarDeclaration [ARRAY_NUMBER, x2, EmptyExpr [defVal=class java.lang.Integer], true, false], "
                + "VarDeclaration [NUMERIC, x3, EmptyExpr [defVal=class java.lang.Integer], false, false], , BOOL, BoolConst [true]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_return_4.xml"));

    }

    @Test
    public void methodReturn5() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], false, false], , BOOL, EmptyExpr [defVal=class de.fhg.iais.roberta.ast.syntax.expr.NullConst]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_return_5.xml"));

    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_return_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_return_2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_return_3.xml");
    }

    @Test
    public void reverseTransformation4() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_return_4.xml");
    }
}
