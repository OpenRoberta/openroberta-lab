package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ForStmtTest {

    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void forStmt1() throws Exception {
        String a = "for ( int ___k0 = 0; ___k0 < 10; ___k0+=1 ) {___item3 += \"Proba\";___item3 += \"Proba1\";for ( int ___k1 = 0; ___k1 < 10; ___k1+=1 ) {}}";

        this.h.assertCodeIsOk(a, "/ast/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/for_stmt.xml");
    }
}