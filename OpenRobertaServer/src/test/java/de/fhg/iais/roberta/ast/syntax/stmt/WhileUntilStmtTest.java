package de.fhg.iais.roberta.ast.syntax.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class WhileUntilStmtTest {

    @Test
    public void whileUntilStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/whileUntil_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "while ( true ) {\n"
                + "}\n"
                + "while ( !(0 == 0) ) {\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "}\n"
                + "while ( !(15 == 20) ) {\n"
                + "    item MATH_CHANGE 1;\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "    while ( !(15 == 20) ) {\n"
                + "        item MATH_CHANGE 1;\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }

}