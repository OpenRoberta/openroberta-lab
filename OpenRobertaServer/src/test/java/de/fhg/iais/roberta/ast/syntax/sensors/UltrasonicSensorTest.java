package de.fhg.iais.roberta.ast.syntax.sensors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.setUltrasonicSensorMode(S4, DISTANCE);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getUltrasonicModeName() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getUltraSonicSensorModeName(S4)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getSampleUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getUltraSonicSensorValue(S4)";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}
