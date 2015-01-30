package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.AstToVarsVisitor;

public class AstToVarsVisitorTest {
    @Test
    public void test() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> ps = Helper.generateASTs("/syntax/expr/expr1.xml");
        Set<String> vars = AstToVarsVisitor.generate(ps);
        Set<String> expected = new HashSet<>(Arrays.asList("-3", "2", "5", "8", "88"));
        assertEquals(expected, vars);
    }
}
