package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathOnListsTest extends NxtAstTest {

    @Test
    public void mathOnListSum() throws Exception {
        final String a = "ArrSum({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_sum.xml", false);
    }

    @Test
    public void mathOnListMin() throws Exception {
        final String a = "ArrMin({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_min.xml", false);
    }

    @Test
    public void mathOnListMax() throws Exception {
        final String a = "ArrMax({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_max.xml", false);
    }

    @Test
    public void mathOnListAverage() throws Exception {
        final String a = "ArrMean({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_average.xml", false);
    }

    @Test
    public void mathOnListMedian() throws Exception {
        final String a = "ArrMedian({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_median.xml", false);
    }

    @Test
    public void mathOnListStandardDeviatioin() throws Exception {
        final String a = "ArrStandardDeviatioin({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_std_dev.xml", false);
    }

    @Test
    public void mathOnListRandom() throws Exception {
        final String a = "ArrRand({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_random.xml", false);
    }
}
