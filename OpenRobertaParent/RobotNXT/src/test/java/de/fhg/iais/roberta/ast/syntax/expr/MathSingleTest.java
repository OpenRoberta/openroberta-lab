package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class MathSingleTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        final String a = "sqrt(0)abs(0)-(0)MathLn(0)MathLog(0)MathPow(E,0)MathPow(10,0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_single.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "volume=sqrt(0)*4/100.0+0.5;";

        this.h.assertCodeIsOk(a, "/syntax/math/math_single1.xml");
    }

    @Test
    public void Test2() throws Exception {
        final String a = "item=sqrt(0);";

        this.h.assertCodeIsOk(a, "/syntax/math/math_single2.xml");
    }
}
