package de.fhg.iais.roberta.syntax.codegen;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class AstToVarsVisitorTest {
    Helper h = new Helper();

    @Test
    public void test() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> ps = this.h.generateASTs("/syntax/expr/expr1.xml");
        Set<String> vars = AstToVarsVisitor.generate(ps);
        Set<String> expected = new HashSet<>(Arrays.asList("-3", "2", "5", "8", "88"));
        assertEquals(expected, vars);
    }
}
