package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ControlTest {

    @Test
    public void robWait() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [1]]]\n"
                + ")\n(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [2]]]\n"
                + ")]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/wait_stmt.xml"));
    }
}
