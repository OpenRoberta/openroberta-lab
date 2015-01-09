package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class RepeatStmtTest {

    @Test
    public void repeatStmt() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=33, y=-573], \n"
                + "(repeat [TIMES, VarDeclaration [NUMERIC_INT, i0, NumConst [0], false], Binary [LT, Var [i0], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i0]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item3], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item3], StringConst [Proba1]]\n"
                + "(repeat [TIMES, VarDeclaration [NUMERIC_INT, i1, NumConst [0], false], Binary [LT, Var [i1], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i1]]]\n"
                + ")\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt<Void> repeatStmt = (RepeatStmt<Void>) transformer.getTree().get(1);

        Assert.assertEquals(Mode.TIMES, repeatStmt.getMode());
    }

    @Test
    public void getExpr() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt<Void> repeatStmt = (RepeatStmt<Void>) transformer.getTree().get(1);

        Assert.assertEquals(
            "VarDeclaration [NUMERIC_INT, i0, NumConst [0], false], Binary [LT, Var [i0], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i0]]",
            repeatStmt.getExpr().toString());
    }

    @Test
    public void getList() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt<Void> repeatStmt = (RepeatStmt<Void>) transformer.getTree().get(1);

        String a =
            "\nexprStmt Binary [TEXT_APPEND, Var [item3], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item3], StringConst [Proba1]]\n"
                + "(repeat [TIMES, VarDeclaration [NUMERIC_INT, i1, NumConst [0], false], Binary [LT, Var [i1], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i1]]]\n"
                + ")";

        Assert.assertEquals(a, repeatStmt.getList().toString());
    }

    @Test
    public void repeatStmt1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-93, y=1], \n"
                + "(repeat [TIMES, VarDeclaration [NUMERIC_INT, i0, NumConst [0], false], Binary [LT, Var [i0], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i0]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt1.xml"));
    }

    @Test
    public void repeatStmtWhileUntil() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-372, y=47], \n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil.xml"));
    }

    @Test
    public void repeatStmtWhileUntil1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-372, y=47], \n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + "StmtFlowCon [BREAK]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil1.xml"));
    }

    @Test
    public void repeatStmtWhileUntil2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-93, y=101], \n" + "(repeat [WHILE, EmptyExpr [defVal=class java.lang.Boolean]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil2.xml"));
    }

    @Test
    public void repeatStmtFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-517, y=190], \n"
                + "(repeat [FOR, VarDeclaration [NUMERIC_INT, i, NumConst [1], false], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [kllk]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for.xml"));
    }

    @Test
    public void repeatStmtFor1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-93, y=190], \n"
                + "(repeat [FOR, VarDeclaration [NUMERIC_INT, i, NumConst [1], false], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for1.xml"));
    }

    @Test
    public void repeatStmtForEach() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-436, y=284], \n"
                + "(repeat [FOR_EACH, Binary [IN, Var [j], EmptyList [STRING]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [gg]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for_each.xml"));
    }

    @Test
    public void repeatStmtForEach1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-93, y=290], \n" + "(repeat [FOR_EACH, Binary [IN, Var [i], EmptyExpr [defVal=interface java.util.List]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for_each1.xml"));
    }

    @Test
    public void loopForever() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=1,y=379],(repeat[FOREVER,BoolConst[true]]FunctionStmt[TextPrintFunct[[ColorConst[GREEN]]]]),(repeat[FOREVER,BoolConst[true]]FunctionStmt[TextPrintFunct[[EmptyExpr[defVal=classjava.lang.String]]]])]]]";

        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateTransformerString("/ast/control/repeat_stmt_loopForever.xml").replaceAll("\\s+", ""));
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        RepeatStmt.Mode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        RepeatStmt.Mode.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        RepeatStmt.Mode.get("asdf");
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt1.xml");
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_whileUntil.xml");
    }

    @Test
    public void reverseTransformationWhileUntil1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_whileUntil1.xml");
    }

    @Test
    public void reverseTransformationWhileUntil2() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_whileUntil2.xml");
    }

    @Test
    public void reverseTransformationWhileUntil3() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_whileUntil3.xml");
    }

    @Test
    public void reverseTransformationWhileUntil4() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_whileUntil4.xml");
    }

    @Test
    public void reverseTransformationFor() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_for.xml");
    }

    @Test
    public void reverseTransformationFor1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_for1.xml");
    }

    @Test
    public void reverseTransformationForEach() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_for_each.xml");
    }

    @Test
    public void reverseTransformationForEach1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_for_each1.xml");
    }

    @Test
    public void reverseTransformationForLoopForever() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/repeat_stmt_loopForever.xml");
    }

}
