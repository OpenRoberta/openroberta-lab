package de.fhg.iais.roberta.ast.syntax.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

/**
 * tests absence of exceptions only :-)
 * 
 * @author rbudde
 */
public class StmtTest {

    @Test
    public void ifStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/syntax/stmt/if_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        generate(transformer.getProject().get(0).get(0));

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";

        // Assert.assertEquals(a, transformer.toString());
    }

    private void generate(Phrase p) {
        StringBuilder sb = new StringBuilder();
        p.generateJava(sb, 0);
        System.out.println(sb.toString());
    }

}