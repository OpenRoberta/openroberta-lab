package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MathOnListsTest {
    @Test
    public void mathOnListSum() throws Exception {
        String a = "BlocklyMethods.sumOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_sum.xml");
    }

    @Test
    public void mathOnListMin() throws Exception {
        String a = "BlocklyMethods.minOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_min.xml");
    }

    @Test
    public void mathOnListMax() throws Exception {
        String a = "BlocklyMethods.maxOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_max.xml");
    }

    @Test
    public void averageOnListMax() throws Exception {
        String a = "BlocklyMethods.averageOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_average.xml");
    }

}
