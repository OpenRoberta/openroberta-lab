package de.fhg.iais.roberta.ast.syntax.actors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class VolumeActionTest {
    @Test
    public void setVolume() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_SetVolume.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "hal.setVolume(50);";

        Assert.assertEquals(a, generate(transformer.getProject().get(0).get(0)));

    }

    @Test
    public void getVolume() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_GetVolume.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "hal.getVolume()";

        Assert.assertEquals(a, generate(transformer.getProject().get(0).get(0)));

    }

    private String generate(Phrase p) {
        StringBuilder sb = new StringBuilder();
        p.generateJava(sb, 0);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
