package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsSubListTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String code =
            "ArrayList<Float> ___Element = new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0));\n"
                + "public void run() throws Exception {"
                + "___Element = new ArrayList<>(new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).subList((int) 0, (int) 0));"
                + "___Element = new ArrayList<>(new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).subList((new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).size() - 1) - (int) 0, (new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).size() - 1) - (int) 0));"
                + "___Element = new ArrayList<>(new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).subList((int) 0, new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).size()));"
                + "___Element = new ArrayList<>(new ArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).subList(0, (int) 0));"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, code, "/syntax/lists/lists_sub_list.xml", makeStandard(), false);
    }
}
