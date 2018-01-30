package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class ListsLengthTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        final String a = "ArrayLen({0.1,0.0,0})";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }
}
