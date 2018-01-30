package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class ListsOccurrenceTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    //ignore
    public void Test() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

    //ignore
    public void Test1() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence1.xml");
    }
}
