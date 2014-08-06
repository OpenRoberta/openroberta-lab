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
public class ExprTest {

    @Test
    public void test1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/expr1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "\n8 + -3 + 5\n" + "88 - ( 8 + -3 + 5 )\n" + "88 - ( 8 + -3 + 5 ) - ( 88 - ( 8 + -3 + 5 ) )\n" + "2 * ( 2 - 2 )\n" + "2 - 2 * 2";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void test2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/expr/expr2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "\n2 * ( 2 - 2 )\n" + "2 - 2 * 2\n" + "88 - ( 8 + -3 + 5 ) - 2 * 2\n" + "( 88 - ( 8 + -3 + 5 ) - 2 * 2 ) / ( 88 - ( 8 + -3 + 5 ) - 2 * 2 )";

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