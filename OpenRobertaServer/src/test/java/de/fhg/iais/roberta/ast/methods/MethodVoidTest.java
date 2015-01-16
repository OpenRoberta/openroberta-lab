package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MethodVoidTest {
    @Test
    public void methodVoid1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=103, y=38], MethodVoid [do something, GlobalVarDeclaration [NUMERIC, x, EmptyExpr [defVal=class java.lang.Integer], false, true], \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_void_1.xml"));

    }

    @Test
    public void methodVoid2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=103, y=38], MethodVoid [do something, , \n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/methods/method_void_2.xml"));

    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_void_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/methods/method_void_2.xml");
    }
}
