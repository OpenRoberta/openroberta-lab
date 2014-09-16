package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.BrickConfigurationN;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbBrickConfigTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;

public class BrickConfigurationTest {

    @Test
    public void test1() throws Exception {
        String a =
            "private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()"
                + ".addActor(ActorPort.A, new HardwareComponentN(EV3MediumRegulatedMotor, FOREWARD, RIGHT)"
                + ".addActor(ActorPort.B, new HardwareComponentN(EV3LargeRegulatedMotor, FOREWARD, NONE)"
                + ".addSensor(SensorPort.S1, new HardwareComponentN(EV3TouchSensor)"
                + ".addSensor(SensorPort.S2, new HardwareComponentN(EV3ColorSensor)"
                + ".addSensor(SensorPort.S3, new HardwareComponentN(EV3UltrasonicSensor)"
                + ".build();";

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Helper.class.getResourceAsStream("/ast/brickConfiguration/brick_configuration.xml"));

        BlockSet project = (BlockSet) jaxbUnmarshaller.unmarshal(src);

        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BrickConfigurationN b = transformer.blockSetToBrickConfiguration(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }

    @Test
    public void test2() throws Exception {
        String a =
            "private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()"
                + ".addActor(ActorPort.A, new HardwareComponentN(EV3MediumRegulatedMotor, FOREWARD, LEFT)"
                + ".addSensor(SensorPort.S3, new HardwareComponentN(EV3IRSensor)"
                + ".build();";

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Helper.class.getResourceAsStream("/ast/brickConfiguration/brick_configuration1.xml"));

        BlockSet project = (BlockSet) jaxbUnmarshaller.unmarshal(src);

        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BrickConfigurationN b = transformer.blockSetToBrickConfiguration(project);
        System.out.println(b.generateRegenerate());
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateRegenerate().replaceAll("\\s+", ""));
    }
}
