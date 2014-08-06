package de.fhg.iais.roberta.ast.syntax.actors;

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

public class ToneActionTest {
    @Test
    public void playTone() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_PlaySound.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.playTone(300, 100);";

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
