package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsOccurrenceTest extends AstTest {

    @Ignore
    public void Test() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence.xml", false);
    }

    @Ignore
    public void Test1() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence1.xml", false);
    }
}
