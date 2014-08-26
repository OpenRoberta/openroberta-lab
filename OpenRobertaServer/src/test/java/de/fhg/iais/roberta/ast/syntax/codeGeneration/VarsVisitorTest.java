package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.AstToVarsVisitor;

public class VarsVisitorTest {
    @Test
    public void test4() throws Exception {
        @SuppressWarnings("unchecked")
        Phrase<Set<String>> p = Helper.generateAST("/syntax/code_generator/java_code_generator4.xml");
        List<Phrase<Set<String>>> ps = new ArrayList<>();
        ps.add(p);
        Set<String> vars = AstToVarsVisitor.generate(ps);
        System.out.println(vars);
    }
}
