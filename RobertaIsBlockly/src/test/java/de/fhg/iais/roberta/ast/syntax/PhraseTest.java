package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;

public class PhraseTest {

    @Test
    public void test() {
        Phrase phrase = StringConst.make("testString");
        StringConst stringConst = phrase.getAs(StringConst.class);
        assertEquals("testString", stringConst.getValue());
        assertEquals(Kind.StringConst, phrase.getKind());
        try {
            phrase.getAs(NumConst.class);
            fail();
        } catch ( Exception e ) {
            // OK
        }
    }

}
