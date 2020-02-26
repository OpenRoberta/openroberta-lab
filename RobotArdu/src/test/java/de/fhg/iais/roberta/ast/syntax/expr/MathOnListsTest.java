package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class MathOnListsTest extends ArduinoAstTest {

    @Test
    public void mathOnListSum() throws Exception {
        final String a = "_getListSum({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_sum.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void mathOnListMin() throws Exception {
        final String a = "_getListMin({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_min.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void mathOnListMax() throws Exception {
        final String a = "_getListMax({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_max.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void mathOnListAverage() throws Exception {
        final String a = "_getListAverage({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_average.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void mathOnListMedian() throws Exception {
        final String a = "_getListMedian({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_median.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void mathOnListStandardDeviation() throws Exception {
        final String a = "_getListStandardDeviation({5,3,2})";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_std_dev.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    @Ignore
    public void mathOnListRandom() throws Exception {
        final String a = "null";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_on_list_random.xml", new ArduinoCxxGeneratorWorker());
    }
}