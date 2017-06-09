package de.fhg.iais.roberta.util.test.ev3;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.factory.EV3lejosFactory;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.syntax.codegen.Ast2Ev3JavaVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2Ev3PythonVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2Ev3SimVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class Helper extends de.fhg.iais.roberta.util.test.Helper {

    public Helper() {
        super();
        this.robotFactory = new EV3lejosFactory(null);
        Configuration brickConfiguration =
            new EV3Configuration.Builder()
                .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.C, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        setRobotConfiguration(brickConfiguration);
    }

    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateStringWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String javaCode = Ast2Ev3JavaVisitor.generate("Test", (EV3Configuration) this.robotConfiguration, transformer.getTree(), false);
        return javaCode;
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
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateStringWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    /**
     * Generate java code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateJava(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ast2Ev3JavaVisitor.generate("Test", (EV3Configuration) brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    /**
     * this.robotConfiguration
     * Generate python code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generatePython(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ast2Ev3PythonVisitor.generate((EV3Configuration) brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
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
        String code = Ast2Ev3SimVisitor.generate(this.robotConfiguration, transformer.getTree());
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

}
