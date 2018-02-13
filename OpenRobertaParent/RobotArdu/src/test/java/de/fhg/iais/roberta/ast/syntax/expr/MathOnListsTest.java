package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MathOnListsTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void mathOnListSum() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_sum.xml", false);
    }

    @Test
    public void mathOnListMin() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_min.xml", false);
    }

    @Test
    public void mathOnListMax() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_max.xml", false);
    }

    @Test
    public void mathOnListAverage() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_average.xml", false);
    }

    @Test
    public void mathOnListMedian() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_median.xml", false);
    }

    @Test
    public void mathOnListStandardDeviatioin() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_std_dev.xml", false);
    }

    @Test
    public void mathOnListRandom() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_random.xml", false);
    }

    @Test
    public void mathOnListMode() throws Exception {
        final String a = "null";

        this.h.assertCodeIsOk(a, "/syntax/math/math_on_list_mode.xml", false);
    }

}