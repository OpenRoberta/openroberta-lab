package de.fhg.iais.roberta.util.test.mbed;

import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.mbed.MicrobitConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.mbed.microbit.Factory;
import de.fhg.iais.roberta.syntax.codegen.mbed.SimulationVisitor;
import de.fhg.iais.roberta.syntax.codegen.mbed.microbit.PythonVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperMicrobitForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperMicrobitForXmlTest() {
        super(new Factory(new RobertaProperties(Util1.loadProperties(null))), new MicrobitConfiguration.Builder().build());
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
     * Generate python code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generatePython(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = PythonVisitor.generate((MicrobitConfiguration) brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }
}
