package de.fhg.iais.roberta.ast.syntax.expr;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

/**
 * tests absence of exceptions only :-)
 * 
 * @author rbudde
 */
public class ExprTest {

    @Test
    public void test1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/expr1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        generate(transformer.getProject().get(0).get(0));
        generate(transformer.getProject().get(1).get(0));
        generate(transformer.getProject().get(2).get(0));
        generate(transformer.getProject().get(3).get(0));
        generate(transformer.getProject().get(4).get(0));
        generate(transformer.getProject().get(5).get(0));
        generate(transformer.getProject().get(6).get(0));
        generate(transformer.getProject().get(7).get(0));
        generate(transformer.getProject().get(8).get(0));
        generate(transformer.getProject().get(9).get(0));

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";

        // Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void test() {
        NumConst n1 = NumConst.make("1");
        NumConst n2 = NumConst.make("2");
        NumConst n3 = NumConst.make("3");
        Binary add = Binary.make(Op.ADD, n1, n2);
        Binary mult1 = Binary.make(Op.MULTIPLY, add, n3);
        Binary mult2 = Binary.make(Op.MULTIPLY, add, add);
        Binary addMult = Binary.make(Op.ADD, mult1, mult2);
        Binary minusCplx = Binary.make(Op.MINUS, add, add);
        Binary addCplx = Binary.make(Op.ADD, add, add);

        generate(add);
        generate(add);
        generate(mult1);
        generate(mult2);
        generate(addMult);
        generate(addCplx);
        generate(minusCplx);

    }

    private void generate(Phrase p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
    }

}