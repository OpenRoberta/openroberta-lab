package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsIsEmptyTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Arrays.asList((float) 0, (float) 0, (float) 0)).isEmpty()}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_is_empty.xml", false);
    }
}
