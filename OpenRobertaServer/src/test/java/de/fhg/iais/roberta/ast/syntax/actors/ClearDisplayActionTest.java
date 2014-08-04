package de.fhg.iais.roberta.ast.syntax.actors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class ClearDisplayActionTest {
    @Test
    public void clearDisplay() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_ClearDisplay.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.clearDisplay();";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}
