package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

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
    public void mathOnListAverage() throws Exception {
        String a = "BlocklyMethods.averageOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_average.xml");
    }

    @Test
    public void mathOnListMedian() throws Exception {
        String a = "BlocklyMethods.medianOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_median.xml");
    }

    @Test
    public void mathOnListStandardDeviatioin() throws Exception {
        String a = "BlocklyMethods.standardDeviatioin(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_std_dev.xml");
    }

    @Test
    public void mathOnListRandom() throws Exception {
        String a = "BlocklyMethods.randOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_random.xml");
    }

    @Test
    public void mathOnListMode() throws Exception {
        String a = "BlocklyMethods.modeOnList(BlocklyMethods.createListWith(5,3,2))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_on_list_mode.xml");
    }

}
