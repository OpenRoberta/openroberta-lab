package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class RepeatStmtTest {

    @Test
    public void repeatStmt() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [TIMES, Binary [ASSIGNMENT, Var [i], NumConst [0]], Binary [LT, Var [i], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba1]]\n"
                + "(repeat [TIMES, Binary [ASSIGNMENT, Var [i], NumConst [0]], Binary [LT, Var [i], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i]]]\n"
                + ")\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getTree().get(0);

        Assert.assertEquals(Mode.TIMES, repeatStmt.getMode());
    }

    @Test
    public void getExpr() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getTree().get(0);

        Assert.assertEquals("Binary [ASSIGNMENT, Var [i], NumConst [0]], Binary [LT, Var [i], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i]]", repeatStmt
            .getExpr()
            .toString());
    }

    @Test
    public void getList() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/control/repeat_stmt.xml");

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getTree().get(0);

        String a =
            "\nexprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba1]]\n"
                + "(repeat [TIMES, Binary [ASSIGNMENT, Var [i], NumConst [0]], Binary [LT, Var [i], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i]]]\n"
                + ")";

        Assert.assertEquals(a, repeatStmt.getList().toString());
    }

    @Test
    public void repeatStmt1() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [TIMES, Binary [ASSIGNMENT, Var [i], NumConst [0]], Binary [LT, Var [i], NumConst [10]], Unary [POSTFIX_INCREMENTS, Var [i]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt1.xml"));
    }

    @Test
    public void repeatStmtWhileUntil() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil.xml"));
    }

    @Test
    public void repeatStmtWhileUntil1() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + "StmtFlowCon [BREAK]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil1.xml"));
    }

    @Test
    public void repeatStmtWhileUntil2() throws Exception {
        String a = "BlockAST [project=[[\n" + "(repeat [WHILE, EmptyExpr [defVal=class java.lang.Boolean]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_whileUntil2.xml"));
    }

    @Test
    public void repeatStmtFor() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR, Binary [ASSIGNMENT, Var [i], NumConst [1]], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [kllk]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for.xml"));
    }

    @Test
    public void repeatStmtFor1() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR, Binary [ASSIGNMENT, Var [i], NumConst [1]], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for1.xml"));
    }

    @Test
    public void repeatStmtForEach() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR_EACH, Binary [IN, Var [j], EmptyExpr [defVal=interface java.util.List]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [gg]]\n"
                + ")]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for_each.xml"));
    }

    @Test
    public void repeatStmtForEach1() throws Exception {
        String a = "BlockAST [project=[[\n" + "(repeat [FOR_EACH, Binary [IN, Var [i], EmptyExpr [defVal=interface java.util.List]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_for_each1.xml"));
    }

    @Test
    public void loopForever() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\nexprStmt Funct [PRINT, [ColorConst [GREEN]]]\n), \n(repeat [WHILE, BoolConst [true]]\nexprStmt Funct [PRINT, [EmptyExpr [defVal=class java.lang.String]]]\n)]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/repeat_stmt_loopForever.xml"));
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        RepeatStmt.Mode op = RepeatStmt.Mode.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        RepeatStmt.Mode op = RepeatStmt.Mode.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        RepeatStmt.Mode op = RepeatStmt.Mode.get("asdf");
    }

}
