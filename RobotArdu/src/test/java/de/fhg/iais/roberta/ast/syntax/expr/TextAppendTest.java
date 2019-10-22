package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextAppendTest extends AstTest {

    @Ignore
    public void Test() throws Exception {
        final String a = "item+String(SENSOR_1)item+String(0)item+String(\"aaa\")";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/text/text_append.xml", false);
    }
}
