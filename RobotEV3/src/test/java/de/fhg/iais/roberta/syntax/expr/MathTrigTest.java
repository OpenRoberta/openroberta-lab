package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "Math.sin(0)Math.cos(0)Math.tan(0)Math.asin(0)" + "Math.acos(0)Math.atan(0)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==Math.sin(0)){hal.regulatedDrive(DriveDirection.FOREWARD,Math.acos(0));}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml", makeStandard(), false);
    }

}
