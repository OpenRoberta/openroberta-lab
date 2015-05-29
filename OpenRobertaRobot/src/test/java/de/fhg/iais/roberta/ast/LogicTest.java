package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LogicTest {

    @Test
    public void logicCompare() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=1], Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_compare.xml"));
    }

    @Test
    public void logicCompare1() throws Exception {
        String a = "BlockAST [project=[[Location [x=81, y=14], Binary [EQ, NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_compare1.xml"));
    }

    @Test
    public void logicCompare2() throws Exception {
        String a = "BlockAST [project=[[Location [x=81, y=14], Binary [NEQ, Binary [EQ, NumConst [2], NumConst [2]], NumConst [1]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_compare2.xml"));
    }

    @Test
    public void logicCompare3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=81, y=14], Binary [NEQ, Binary [GT, NumConst [2], NumConst [2]], Binary [LT, NumConst [6], NumConst [8]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_compare3.xml"));
    }

    @Test
    public void logicCompare4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=81, y=14], Binary [NEQ, Binary [GT, NumConst [2], Binary [LTE, NumConst [6], NumConst [5]]], NumConst [8]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_compare4.xml"));
    }

    @Test
    public void logic_operation() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=263, y=-422], Binary [AND, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_operation.xml"));
    }

    @Test
    public void logic_negate() throws Exception {
        String a = "BlockAST [project=[[Location [x=157, y=-187], Unary [NOT, EmptyExpr [defVal=class java.lang.Boolean]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_negate.xml"));
    }

    @Test
    public void reverseTransformatinLogicNegate() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_negate.xml");
    }

    @Test
    public void logic_negate1() throws Exception {
        String a = "BlockAST [project=[[Location [x=9, y=-434], Unary [NOT, Binary [OR, BoolConst [true], BoolConst [false]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_negate1.xml"));
    }

    @Test
    public void reverseTransformatinLogicNegate1() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_negate1.xml");
    }

    @Test
    public void reverseTransformatinLogicNegate2() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_negate2.xml");
    }

    @Test
    public void logic_null() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=-873], NullConst [null]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_null.xml"));
    }

    @Test
    public void reverseTransformatinLogicNull() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_null.xml");
    }

    @Test
    public void logic_ternary() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-12, y=271], \nif Binary [EQ, NumConst [0], NumConst [0]]\n"
                + ",then\n"
                + "exprStmt StringConst [2]\n"
                + ",else\n"
                + "exprStmt StringConst [3]\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_ternary.xml"));
    }

    @Test
    public void reverseTransformatinLogicTernary() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_ternary.xml");
    }
}
