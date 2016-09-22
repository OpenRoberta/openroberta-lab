package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.testutil.Helper;

public class IfStmtTest {

    @Test
    public void ifStmt() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt.xml"));
    }

    @Test
    public void ifStmt1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt1.xml"));
    }

    @Test
    public void getExpr() throws Exception {
        IfStmt<Void> ifStmt = (IfStmt<Void>) Helper.generateTransformer("/ast/control/if_stmt1.xml").getTree().get(0).get(1);

        String a = "[Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]";
        Assert.assertEquals(a, ifStmt.getExpr().toString());
    }

    @Test
    public void getThen() throws Exception {
        IfStmt<Void> ifStmt = (IfStmt<Void>) Helper.generateTransformer("/ast/control/if_stmt1.xml").getTree().get(0).get(1);

        String a = "[\nexprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]]";
        Assert.assertEquals(a, ifStmt.getThenList().toString());
    }

    @Test
    public void getElse() throws Exception {
        IfStmt<Void> ifStmt = (IfStmt<Void>) Helper.generateTransformer("/ast/control/if_stmt1.xml").getTree().get(0).get(1);

        String a = "\nSensorStmt DrehSensor [mode=RESET, motor=A]";
        Assert.assertEquals(a, ifStmt.getElseList().toString());
    }

    @Test
    public void ifStmt2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt2.xml"));
    }

    @Test
    public void ifStmt3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt3.xml"));
    }

    @Test
    public void ifStmt4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt4.xml"));
    }

    @Test
    public void ifStmt5() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt5.xml"));
    }

    @Test
    public void ifStmt6() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=68, y=133], \n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt6.xml"));
    }

    @Test
    public void ifStmt7() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=64, y=67], \n"
                + "if NullConst [null]\n"
                + ",then\n"
                + "AktionStmt [DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=null]]]\n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt7.xml"));
    }

    @Test
    public void ifStmt8() throws Exception {
        String a = "BlockAST [project=[[Location [x=-93, y=1], \nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt8.xml"));
    }

    @Test
    public void ifStmt9() throws Exception {

        String a = "BlockAST [project=[[Location [x=-93, y=90], \nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt9.xml"));
    }

    @Test
    public void ifStmt10() throws Exception {
        String a = "BlockAST [project=[[Location [x=-93, y=179], \nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/if_stmt10.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt3.xml");
    }

    @Test
    public void reverseTransformation4() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt4.xml");
    }

    @Test
    public void reverseTransformation5() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt5.xml");
    }

    @Test
    public void reverseTransformation6() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt6.xml");
    }

    @Test
    public void reverseTransformation7() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt7.xml");
    }

    @Test
    public void reverseTransformation8() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt8.xml");
    }

    @Test
    public void reverseTransformation9() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt9.xml");
    }

    @Test
    public void reverseTransformation10() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/if_stmt10.xml");
    }

}
