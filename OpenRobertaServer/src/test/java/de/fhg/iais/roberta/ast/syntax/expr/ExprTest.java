package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ExprTest {

    @Test
    public void test1() throws Exception {
        String a = "\n8 + -3 + 5\n" + "88 - ( 8 + -3 + 5 )\n" + "88 - ( 8 + -3 + 5 ) - ( 88 - ( 8 + -3 + 5 ) )\n" + "2 * ( 2 - 2 )\n" + "2 - 2 * 2";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/syntax/expr/expr1.xml"));
    }

    @Test
    public void test2() throws Exception {
        String a = "\n2 * ( 2 - 2 )\n" + "2 - 2 * 2\n" + "88 - ( 8 + -3 + 5 ) - 2 * 2\n" + "( 88 - ( 8 + -3 + 5 ) - 2 * 2 ) / ( 88 - ( 8 + -3 + 5 ) - 2 * 2 )";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/syntax/expr/expr2.xml"));
    }
}