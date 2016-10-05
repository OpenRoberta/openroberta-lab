package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.StringConst;

public class PhraseTest {

    @Test
    public void test() {
        Phrase<?> phrase = StringConst.make("testString", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        assertEquals("STRING_CONST", phrase.getKind().getName());
    }

}
