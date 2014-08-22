package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class FlowControlStmtTest {

    @Test
    public void flowControlStmt() throws Exception {
        String a =
            "\nwhile ( 0 == 0 ) {\n"
                + "    System.out.println(\"123\");\n"
                + "    System.out.println(\"123\");\n"
                + "    while ( !(0 == 0) ) {\n"
                + "        System.out.println(\"123\");\n"
                + "        System.out.println(\"123\");\n"
                + "        break;\n"
                + "    }\n"
                + "    break;\n"
                + "}";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/syntax/stmt/flowControl_stmt.xml"));
    }
}