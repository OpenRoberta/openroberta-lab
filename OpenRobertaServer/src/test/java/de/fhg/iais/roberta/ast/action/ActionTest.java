package de.fhg.iais.roberta.ast.action;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.helper.Helper;

public class ActionTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "BlockAST [project=[[ClearDisplayAction []]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_ClearDisplay.xml"));
    }

    @Test
    public void stop() throws Exception {
        String a = "BlockAST [project=[[StopAction []]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/actions/action_Stop.xml"));
    }

    @Test
    public void blockException() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_Exception.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        try {
            transformer.projectToAST(project);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("Invalid Block: robActions_brickLight_on1", e.getMessage());
        }
    }
}
