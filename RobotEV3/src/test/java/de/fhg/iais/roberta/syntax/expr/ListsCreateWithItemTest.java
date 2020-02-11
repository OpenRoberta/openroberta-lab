package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsCreateWithItemTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Collections.nCopies((int) 5, (float) 1))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_create_with_item.xml", false);
    }

}
