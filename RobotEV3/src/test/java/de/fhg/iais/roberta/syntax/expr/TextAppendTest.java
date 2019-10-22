package de.fhg.iais.roberta.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextAppendTest extends Ev3LejosAstTest {

    // TODO Invalid test
    @Ignore
    @Test
    public void Test() throws Exception {
        String a = "item+=String.valueOf(hal.isPressed(SensorPort.S1));item+=String.valueOf(0);item+=String.valueOf(\"aaa\");}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/text/text_append.xml", makeStandard(), false);
    }
}
