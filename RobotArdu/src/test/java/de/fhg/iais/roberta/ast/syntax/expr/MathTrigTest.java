package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends BotnrollAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "sin(M_PI/180.0*(0))cos(M_PI/180.0*(0))tan(M_PI/180.0*(0))180.0/M_PI*asin(0)180.0/M_PI*acos(0)180.0/M_PI*atan(0)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig.xml", makeConfiguration(), false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==sin(M_PI/180.0*(0))){one.movePID(180.0/M_PI*acos(0),180.0/M_PI*acos(0));}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml", makeConfiguration(), false);
    }

}
