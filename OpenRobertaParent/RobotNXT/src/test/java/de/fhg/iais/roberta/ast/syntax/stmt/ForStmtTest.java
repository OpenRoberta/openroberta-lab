package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ForStmtTest {

    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void forStmt1() throws Exception {
        String a = "for ( int k0 = 0; k0 < 10; k0+=1 ) {item3 += \"Proba\";item3 += \"Proba1\";for ( int k1 = 0; k1 < 10; k1+=1 ) {}}";

        this.h.assertCodeIsOk(a, "/ast/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/for_stmt.xml");
    }
}