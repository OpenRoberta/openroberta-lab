package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class ListsOccurrenceTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Ignore
    public void Test() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml", false);
    }

    @Ignore
    public void Test1() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence1.xml", false);
    }
}
