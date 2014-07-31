package de.fhg.iais.roberta.ast.syntax.sensors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "hal.setUltrasonicSensorMode(S4, DISTANCE);";

        Assert.assertEquals(a, generate(transformer.getProject().get(0).get(0)));

    }

    @Test
    public void getUltrasonicModeName() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "hal.getUltraSonicSensorModeName(S4)";

        Assert.assertEquals(a, generate(transformer.getProject().get(0).get(0)));
    }

    @Test
    public void getSampleUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "hal.getUltraSonicSensorValue(S4)";

        Assert.assertEquals(a, generate(transformer.getProject().get(0).get(0)));
    }

    private String generate(Phrase p) {
        StringBuilder sb = new StringBuilder();
        p.generateJava(sb, 0);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
