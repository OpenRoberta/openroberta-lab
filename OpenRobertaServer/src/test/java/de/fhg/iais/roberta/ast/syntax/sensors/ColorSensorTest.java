package de.fhg.iais.roberta.ast.syntax.sensors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.setColorSensorMode(S3, COLOUR);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getColorModeName() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getColorSensorModeName(S3)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getSampleColor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getColorSensorValue(S3)";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}
