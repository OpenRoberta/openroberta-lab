package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MethodIfReturnTest {
    @Test
    public void methodIfReturn1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], false, false], "
                + "MethodStmt [MethodIfReturn [SensorExpr [TouchSensor [port=S1]], BOOL, BoolConst [false]]], BOOL, EmptyExpr [defVal=class de.fhg.iais.roberta.syntax.expr.NullConst]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_if_return_1.xml"));

    }

    @Test
    public void methodIfReturn2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=44, y=104], MethodReturn [Funktionsblock,"
                + " VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], true, false],"
                + " VarDeclaration [NUMERIC, x2, EmptyExpr [defVal=class java.lang.Integer], true, false],"
                + " VarDeclaration [NUMERIC, x3, EmptyExpr [defVal=class java.lang.Integer], false, false], "
                + "\nAktionStmt [LightAction [GREEN, ON]]MethodStmt [MethodIfReturn [Binary [EQ, NumConst [0], NumConst [0]], NUMERIC, Var [x2]]], NUMERIC, Var [x3]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_if_return_2.xml"));
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_if_return_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_if_return_2.xml");
    }

}
