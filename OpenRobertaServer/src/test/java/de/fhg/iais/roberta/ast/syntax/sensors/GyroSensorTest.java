package de.fhg.iais.roberta.ast.syntax.sensors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerator;

public class GyroSensorTest {

    @Test
    public void setGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.setGyroSensorMode(S2, ANGLE);";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getGyroSensorModeName() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getGyroSensorModeName(S2)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void getGyroSensorValue() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.getGyroSensorValue(S2)";

        Assert.assertEquals(a, generate(project));
    }

    @Test
    public void resetGyroSensor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        String a = "hal.resetGyroSensor(S2);";

        Assert.assertEquals(a, generate(project));
    }

    private String generate(Project p) {
        JavaGenerator generator = new JavaGenerator();
        generator.generate(p);
        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }
}
