package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MethodCallTest {
    @Test
    public void methodCall1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=44, y=104], MethodReturn [Funktionsblock, VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], true, false], "
                + "VarDeclaration [NUMERIC, x2, EmptyExpr [defVal=class java.lang.Integer], true, false], "
                + "VarDeclaration [NUMERIC, x3, EmptyExpr [defVal=class java.lang.Integer], false, false], \n"
                + "AktionStmt [LightAction [GREEN, ON]]MethodStmt [MethodIfReturn [Binary [EQ, NumConst [0], NumConst [0]], NUMERIC, Var [x2]]], NUMERIC, Var [x3]]], [Location [x=75, y=488], MethodCall [Funktionsblock, Var [x], Var [x2], Var [x3], NumConst [0], NumConst [1], NumConst [2], NUMERIC]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_call_1.xml"));
    }

    @Test
    public void methodCall2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-28, y=85], MethodReturn [Funktionsblock, , , "
                + "STRING, StringConst []]], [Location [x=-16, y=181], MethodCall [Funktionsblock, , , STRING]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_call_2.xml"));
    }

    @Test
    public void methodCall3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=5, y=28], MethodVoid [Funktionsblock, "
                + "VarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], true, false], "
                + "VarDeclaration [NUMERIC, x2, EmptyExpr [defVal=class java.lang.Integer], false, false], \n"
                + "AktionStmt [VolumeAction [SET, NumConst [50]]]]], [Location [x=6, y=189], MethodCall [Funktionsblock, Var [x], Var [x2], NumConst [0], NumConst [2], null]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_call_3.xml"));
    }

    @Test
    public void methodCall4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=5, y=28], MethodVoid [Funktionsblock, , \n"
                + "AktionStmt [VolumeAction [SET, NumConst [50]]]]], [Location [x=6, y=189], MethodCall [Funktionsblock, , , null]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_call_4.xml"));
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_call_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_call_2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_call_3.xml");
    }

    @Test
    public void reverseTransformation4() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_call_4.xml");
    }

}
