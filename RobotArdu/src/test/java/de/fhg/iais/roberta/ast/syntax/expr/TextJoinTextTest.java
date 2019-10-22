package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextJoinTextTest extends AstTest {

    @Ignore
    public void Test() throws Exception {
        String a = "BlocklyMethods.textJoin(0, 0, \"a\", \"b\", true, hal.isPressed(SensorPort.S1))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/text/text_join.xml", false);
    }

}
