package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsLengthTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "ArrayLen({0.1,0.0,0})";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_length.xml", false);
    }
}
