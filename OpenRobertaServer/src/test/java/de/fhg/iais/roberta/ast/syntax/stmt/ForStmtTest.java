package de.fhg.iais.roberta.ast.syntax.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class ForStmtTest {

    @Test
    public void forStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/for_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "for ( int i = 0; i < 10; i++ ) {\n"
                + "}\n"
                + "for ( int i = 0; i < 10; i++ ) {\n"
                + "    System.out.println(\"15\");\n"
                + "    System.out.println(\"15\");\n"
                + "}\n"
                + "for ( int i = 0; i < 10; i++ ) {\n"
                + "    for ( int i = 0; i < 10; i++ ) {\n"
                + "        System.out.println(\"15\");\n"
                + "        System.out.println(\"15\");\n"
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