package de.fhg.iais.roberta.util.test.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.Ev3LejosV0Factory;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.Ev3C4ev3Visitor;
import de.fhg.iais.roberta.visitor.codegen.Ev3JavaVisitor;
import de.fhg.iais.roberta.visitor.codegen.Ev3PythonVisitor;
import de.fhg.iais.roberta.visitor.codegen.Ev3SimVisitor;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class HelperEv3ForXmlTest extends AbstractHelperForXmlTest {

    public HelperEv3ForXmlTest() {
        super(new Ev3LejosV0Factory(new PluginProperties("ev3lejosv1", "", "", Util1.loadProperties("classpath:/ev3lejosv1.properties"))), makeConfiguration());
    }

    public static Configuration makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", "D", motorDproperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorC, motorD));
        Configuration configuration = builder.build();
        configuration.setRobotName("ev3lejosV1");
        return configuration;
    }

    public static Configuration makeStandardEv3DevConfiguration() {
        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> touchSensorProperties = createMap();
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", touchSensorProperties);

        Map<String, String> gyroSensorProperties = createMap();
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "S2", "2", gyroSensorProperties);

        Map<String, String> colourSensorProperties = createMap();
        ConfigurationComponent colourSensor = new ConfigurationComponent("COLOR", false, "S3", "3", colourSensorProperties);

        Map<String, String> ultrasonicSensorProperties = createMap();
        ConfigurationComponent ultrasonicSensor =
            new ConfigurationComponent("ULTRASONIC", false, "S4", "4", ultrasonicSensorProperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorB, motorC, touchSensor, gyroSensor, colourSensor, ultrasonicSensor));
        Configuration configuration = builder.build();
        configuration.setRobotName("ev3dev");
        return configuration;
    }

    public static Configuration makeTouchUltrasonicColorConfiguration () {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        //        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", BlocklyConstants.NO_SLOT, "4", Collections.emptyMap());
        final Configuration.Builder builder = new Configuration.Builder();
        return builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(Arrays.asList(motorA, motorB, touchSensor, ultrasonicSensor, colorSensor))
            .build();
    }

    public static Configuration makeTouchUltrasonicColorUltrasonicConfiguration () {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent colorSensor = new ConfigurationComponent("COLOR", false, "S3", "3", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", Collections.emptyMap());
        final Configuration.Builder builder = new Configuration.Builder();
        return builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(Arrays.asList(motorA, motorB, touchSensor, ultrasonicSensor, colorSensor, ultrasonicSensor4))
            .build();
    }

    public static Configuration makeTouchGyroInfraredUltrasonic () {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("MEDIUM", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);
        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent gyro = new ConfigurationComponent("GYRO", false, "S2", "2", Collections.emptyMap());
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", false, "S3", "3", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor4 = new ConfigurationComponent("ULTRASONIC", false, "S4", "4", Collections.emptyMap());
        final Configuration.Builder builder = new Configuration.Builder();
        return builder
                .setTrackWidth(17f)
                .setWheelDiameter(5.6f)
                .addComponents(Arrays.asList(motorA, motorB, touchSensor, infrared, ultrasonicSensor4, gyro))
                .build();
    }

    public static Configuration makeRotateRegulatedUnregulatedForwardBackwardMotors () {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "ON", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "ON", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("LARGE", true, "D",  "D", motorDproperties);

        final Configuration.Builder builder = new Configuration.Builder();
        return builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(Arrays.asList(motorA, motorB, motorC, motorD))
            .build();
    }

    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateStringWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String javaCode = Ev3JavaVisitor.generate("Test", getRobotConfiguration(), transformer.getTree(), false, Language.ENGLISH);
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
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ev3JavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), true, Language.ENGLISH);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    /**
     * this.robotConfiguration Generate python code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generatePython(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ev3PythonVisitor.generate(brickConfiguration, transformer.getTree(), true, Language.ENGLISH);
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
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ev3SimVisitor.generate(getRobotConfiguration(), transformer.getTree(), Language.ENGLISH);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    public String generateC4ev3(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ev3C4ev3Visitor.generate("Test", brickConfiguration, transformer.getTree(), false, Language.ENGLISH);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    public void compareExistingAndGeneratedPythonSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert
            .assertEquals(
                Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""),
                generatePython(xmlFilename, configuration).replaceAll("\\s+", ""));
    }

    public void compareExistingAndGeneratedJavaSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert
            .assertEquals(
                Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""),
                generateJava(xmlFilename, configuration).replaceAll("\\s+", ""));
    }

}
