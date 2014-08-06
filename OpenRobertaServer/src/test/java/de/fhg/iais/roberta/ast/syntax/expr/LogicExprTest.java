package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
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

        String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "5 <= 7 == 8 > 9\n"
                + "( 5 != 7 ) >= ( 8 == 9 )\n"
                + "5 + 7 >= ( 8 + 4 ) / ( 9 + 3 )\n"
                + "( 5 + 7 == 5 + 7 ) >= ( 8 + 4 ) / ( 9 + 3 )\n"
                + "( 5 + 7 == 5 + 7 ) >= ( 5 + 7 == 5 + 7 && 5 + 7 <= 5 + 7 )\n"
                + "!(5 + 7 == 5 + 7) == true";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void logicNegate() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/logic_negate.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\n!(0 != 0 && false)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void logicNull() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/logic_null.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nnull";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void logicTernary() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/logic_ternary.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\n( 0 == 0 ) ? false : true";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project project) {
        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaGenerator generator = new JavaGenerator("", brickConfiguration);
        for ( ArrayList<Phrase> instance : transformer.getProject() ) {
            generator.generateCodeFromPhrases(instance);
        }
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}