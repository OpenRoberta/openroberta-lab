package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsEmptyListTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<String>()" + "newArrayList<Pickcolor>()" + "newArrayList<Boolean>()" + "newArrayList<Float>()}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_empty_list.xml", false);
    }

}
