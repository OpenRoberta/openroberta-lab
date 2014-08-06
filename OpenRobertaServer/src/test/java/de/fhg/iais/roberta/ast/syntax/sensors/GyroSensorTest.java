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

public class GyroSensorTest {

    @Test
    public void setGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.setGyroSensorMode(S2, ANGLE);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getGyroSensorModeName() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.getGyroSensorModeName(S2)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getGyroSensorValue() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.getGyroSensorValue(S2)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void resetGyroSensor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "\nhal.resetGyroSensor(S2);";

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
