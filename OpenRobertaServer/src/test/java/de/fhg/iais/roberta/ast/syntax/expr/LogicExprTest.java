package de.fhg.iais.roberta.ast.syntax.expr;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

/**
 * tests absence of exceptions only :-)
 * 
 * @author rbudde
 */
public class LogicExprTest {

    @Test
    public void test1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/logic_expr.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        generate(project);

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";

        // Assert.assertEquals(a, transformer.toString());
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}