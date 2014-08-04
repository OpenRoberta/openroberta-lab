package de.fhg.iais.roberta.ast.syntax.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class IfStmtTest {

    @Test
    public void ifStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/if_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a =
            "if ( true ) {\n"
                + "}\n"
                + "if ( false ) {\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "    item = 3 * 9;\n"
                + "} else if ( true ) {\n"
                + "    item = 3 * 9;\n"
                + "    item = 3 * 9;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
                + "}";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void ifStmt1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/if_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "if ( 5 + 7 == 5 + 7 >= 5 + 7 == 5 + 7 && 5 + 7 <= 5 + 7 ) {\n}";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void ifStmt2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/if_stmt2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a =
            "if ( true ) {\n"
                + "    System.out.println(\"1\");\n"
                + "    System.out.println(\"8\");\n"
                + "} else if ( false ) {\n"
                + "    System.out.println(\"2\");\n"
                + "} else {\n"
                + "    System.out.println(\"3\");\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    System.out.println(\" 1\");\n"
                + "} else {\n"
                + "    System.out.println(\" else\");\n"
                + "    System.out.println(0);\n"
                + "}";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void ifStmt3() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/if_stmt3.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a =
            "if ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
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