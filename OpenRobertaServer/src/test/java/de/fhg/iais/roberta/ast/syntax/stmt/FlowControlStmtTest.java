package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class FlowControlStmtTest {

    @Test
    public void flowControlStmt() throws Exception {
        String a =
            "\nif ( TRUE ) {\nwhile ( 0 == 0 ) {\n"
                + "    System.out.println(\"123\");\n"
                + "    System.out.println(\"123\");\n"
                + "    if ( TRUE ) {\nwhile ( !(0 == 0) ) {\n"
                + "        System.out.println(\"123\");\n"
                + "        System.out.println(\"123\");\n"
                + "        break;\n"
                + "    }}\n"
                + "    break;\n"
                + "}}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}