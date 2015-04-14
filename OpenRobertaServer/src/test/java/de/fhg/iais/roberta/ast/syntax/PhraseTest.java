package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;

public class PhraseTest {

    @Test
    public void test() {
        Phrase<?> phrase = StringConst.make("testString", BlocklyBlockProperties.make("1", "1", false, false, false, false, false), null);
        assertEquals(Kind.STRING_CONST, phrase.getKind());
    }

}
