package de.fhg.iais.roberta.ast.syntax.actors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class VolumeActionTest {
    @Test
    public void setVolume() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_SetVolume.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.setVolume(50);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getVolume() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_GetVolume.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getVolume()";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}
