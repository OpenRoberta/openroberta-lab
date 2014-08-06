package de.fhg.iais.roberta.ast.syntax.sensors;

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

public class MotorTachoTest {

    @Test
    public void setMotorTacho() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.setMotorTachoMode(A, ROTATION);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getMotorTachoMode() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.getMotorTachoMode(A)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getSampleMotorTacho() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.getMotorTachoValue(A)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void resetMotorTacho() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.resetMotorTacho(A);";

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
