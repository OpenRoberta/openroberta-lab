package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class WhileUntilStmtTest {

    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "\nwhile ( true ) {\n"
                + "}\n"
                + "while ( !(0 == 0) ) {\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "}\n"
                + "while ( !(15 == 20) ) {\n"
                + "    item MATH_CHANGE 1;\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "    while ( !(15 == 20) ) {\n"
                + "        item MATH_CHANGE 1;\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/syntax/stmt/whileUntil_stmt.xml"));
    }

    @Test
    public void loopForever() throws Exception {
        String a = "" //
            + "\nwhile ( true ) {\n"
            + "    System.out.println(PickColor.GREEN);\n"
            + "}\n"
            + "while ( true ) {\n"
            + "    System.out.println(\"\");\n"
            + "}";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/control/repeat_stmt_loopForever.xml"));
    }
}