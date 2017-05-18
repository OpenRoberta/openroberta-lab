package de.fhg.iais.roberta.util.test.mbed;

import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.MicrobitConfiguration;
import de.fhg.iais.roberta.factory.Calliope2016Factory;
import de.fhg.iais.roberta.syntax.codegen.Ast2CppCalliopeVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2MbedSimVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2PythonMicroBitVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper extends de.fhg.iais.roberta.util.test.Helper {

    public Helper() {
        super();
        this.robotFactory = new Calliope2016Factory(null);
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
        Configuration brickConfiguration = new CalliopeConfiguration.Builder().build();
        String code = Ast2MbedSimVisitor.generate(brickConfiguration, transformer.getTree());
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    /**
     * Generate java code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateString(String pathToProgramXml, CalliopeConfiguration brickConfiguration) throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = Ast2CppCalliopeVisitor.generate(brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
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
        String code = Ast2PythonMicroBitVisitor.generate((MicrobitConfiguration) brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }
}
