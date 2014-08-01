package de.fhg.iais.roberta.ast.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class ControlTest {

    @Test
    public void robWait() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/wait_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [1]]]\n"
                + ")\n(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [2]]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }
}
