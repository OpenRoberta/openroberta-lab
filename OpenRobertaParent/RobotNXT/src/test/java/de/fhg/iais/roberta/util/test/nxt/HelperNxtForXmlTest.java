package de.fhg.iais.roberta.util.test.nxt;

import java.util.Properties;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.nxt.NxtConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.nxt.Factory;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.syntax.codegen.nxt.NxcVisitor;
import de.fhg.iais.roberta.syntax.codegen.nxt.SimulationVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperNxtForXmlTest extends AbstractHelperForXmlTest {

    public HelperNxtForXmlTest() {
        super(
            new Factory(new RobertaProperties(Util1.loadProperties(null))),
            new NxtConfiguration.Builder()
                .addActor(new ActorPort("A", "MA"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(new ActorPort("B", "MB"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(new ActorPort("C", "MC"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot.properties", robotProperties);
    }

    /**
     * Generate java script code as string from a given program .
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateJavaScript(String pathToProgramXml) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = SimulationVisitor.generate(getRobotConfiguration(), transformer.getTree());
        return code;
    }

    /**
     * Generate nxc as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateNXCWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = NxcVisitor.generate((NxtConfiguration) getRobotConfiguration(), transformer.getTree(), false);
        return code;
    }

    /**
     * Generate nxc as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateNXC(String pathToProgramXml, NxtConfiguration brickConfiguration) throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = NxcVisitor.generate(brickConfiguration, transformer.getTree(), true);
        return code;
    }

    /**
     * Assert that Java code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct java code
     * @param fileName of the program we want to generate java code
     * @throws Exception
     */
    public void assertCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXCWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    public void assertWrappedCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        NxtConfiguration brickConfiguration =
            (NxtConfiguration) new NxtConfiguration.Builder()
                .addActor(new ActorPort("A", "MA"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(new ActorPort("B", "MB"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(new ActorPort("C", "MC"), new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXC(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }

}
