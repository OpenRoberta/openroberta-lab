package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathOnListsTest extends Ev3LejosAstTest {

    @Test
    public void mathOnListSum() throws Exception {
        String a = "_sum(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_sum.xml", false);
    }

    @Test
    public void mathOnListMin() throws Exception {
        String a = "Collections.min(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_min.xml", false);
    }

    @Test
    public void mathOnListMax() throws Exception {
        String a = "Collections.max(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_max.xml", false);
    }

    @Test
    public void mathOnListAverage() throws Exception {
        String a = "_average(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)" + "))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_average.xml", false);
    }

    @Test
    public void mathOnListMedian() throws Exception {
        String a = "_median(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_median.xml", false);
    }

    @Test
    public void mathOnListStandardDeviatioin() throws Exception {
        String a = "_standardDeviation(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float)" + " 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_std_dev.xml", false);
    }

    @Test
    public void mathOnListRandom() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)).get(0)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_on_list_random.xml", false);
    }
}
