package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.Ev3JavaGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.Ev3UsedHardwareCollectorWorker;

public class ListsSetIndexTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).set((int) (1), (float) 99);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_set_index.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void Test1() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).add( (int) (1), (float) 99);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_set_index1.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }

    @Test
    public void Test2() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 55, (float) 66, (float) 11)).add((float) 99);}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_set_index2.xml", new Ev3UsedHardwareCollectorWorker(), new Ev3JavaGeneratorWorker());
    }
}
