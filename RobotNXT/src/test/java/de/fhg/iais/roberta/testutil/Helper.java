package de.fhg.iais.roberta.testutil;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.NxtConfiguration;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.nxt.ActorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.codegen.Ast2NxcVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.transformer.Jaxb2NxtConfigurationTransformer;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper {
    NxtFactory factory = new NxtFactory(null);

    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    public static String generateStringWithoutWrapping(String pathToProgramXml) throws Exception {
        //NxtFactory factory = new NxtFactory(null, 0);
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        Configuration brickConfiguration =
            new NxtConfiguration.Builder()
                .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.C, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .build();
        final String javaCode = Ast2NxcVisitor.generate((NxtConfiguration) brickConfiguration, transformer.getTree(), false);
        // System.out.println(javaCode); // only needed for EXTREME debugging
        // String textlyCode = AstToTextlyVisitor.generate("Test", transformer.getTree(), false);
        // System.out.println(textlyCode); // only needed for EXTREME debugging
        return javaCode;
    }

    /**
     * Generate java code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public static String generateString(String pathToProgramXml, NxtConfiguration brickConfiguration) throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = Ast2NxcVisitor.generate(brickConfiguration, transformer.getTree(), true);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }

    /**
     * return the brick configuration for given XML configuration text.
     *
     * @param blocklyXml the configuration XML as String
     * @return brick configuration
     * @throws Exception
     */
    public static Configuration generateConfiguration(String blocklyXml) throws Exception {
        final BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        NxtFactory factory = new NxtFactory(null);
        final Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    /**
     * return the jaxb transformer for a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return jaxb transformer
     * @throws Exception
     */
    public static Jaxb2BlocklyProgramTransformer<Void> generateTransformer(String pathToProgramXml) throws Exception {
        final BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        NxtFactory factory = new NxtFactory(null);
        final Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>(factory);
        transformer.transform(project);
        return transformer;
    }

    /**
     * return the jaxb transformer for a given XML program text.
     *
     * @param blocklyXml the program XML as String
     * @return jaxb the transformer
     * @throws Exception
     */
    public static Jaxb2BlocklyProgramTransformer<Void> generateProgramTransformer(String blocklyXml) throws Exception {
        final BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        NxtFactory factory = new NxtFactory(null);
        final Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>(factory);
        transformer.transform(project);
        return transformer;
    }

    /**
     * return the toString representation for a jaxb transformer for a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the toString representation for a jaxb transformer
     * @throws Exception
     */
    public static String generateTransformerString(String pathToProgramXml) throws Exception {
        return generateTransformer(pathToProgramXml).toString();
    }

    /**
     * return the first and only one phrase from a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the first and only one phrase
     * @throws Exception
     */
    public static <V> ArrayList<ArrayList<Phrase<V>>> generateASTs(String pathToProgramXml) throws Exception {
        final BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        NxtFactory factory = new NxtFactory(null);
        final Jaxb2BlocklyProgramTransformer<V> transformer = new Jaxb2BlocklyProgramTransformer<V>(factory);
        transformer.transform(project);
        final ArrayList<ArrayList<Phrase<V>>> tree = transformer.getTree();
        return tree;
    }

    /**
     * Generate AST from XML Blockly stored program
     *
     * @param pathToProgramXml
     * @return AST of the program
     * @throws Exception
     */
    public static <V> Phrase<V> generateAST(String pathToProgramXml) throws Exception {
        final ArrayList<ArrayList<Phrase<V>>> tree = generateASTs(pathToProgramXml);
        return tree.get(0).get(1);
    }

    /**
     * Asserts if transformation of Blockly XML saved program is correct.<br>
     * <br>
     * <b>Transformation:</b>
     * <ol>
     * <li>XML to JAXB</li>
     * <li>JAXB to AST</li>
     * <li>AST to JAXB</li>
     * <li>JAXB to XML</li>
     * </ol>
     * Return true if the first XML is equal to second XML.
     *
     * @param fileName of the program
     * @throws Exception
     */
    public static void assertTransformationIsOk(String fileName) throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(fileName);
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final BlockSet blockSet = astToJaxb(transformer.getTree());
        //m.marshal(blockSet, System.out); // only needed for EXTREME debugging
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        final String t = Resources.toString(Helper.class.getResource(fileName), Charsets.UTF_8);
        XMLUnit.setIgnoreWhitespace(true);
        final Diff diff = XMLUnit.compareXML(writer.toString(), t);
        //System.out.println(t);

        //System.out.println(diff.toString()); // only needed for EXTREME debugging
        Assert.assertTrue(diff.identical());
    }

    public static BlockSet astToJaxb(ArrayList<ArrayList<Phrase<Void>>> astProgram) {
        final BlockSet blockSet = new BlockSet();

        Instance instance = null;
        for ( final ArrayList<Phrase<Void>> tree : astProgram ) {
            for ( final Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    blockSet.getInstance().add(instance);
                    instance = new Instance();
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        blockSet.getInstance().add(instance);
        return blockSet;
    }

    /**
     * Asserts if two XML string are identical by ignoring white space.
     *
     * @param arg1 first XML string
     * @param arg2 second XML string
     * @throws Exception
     */
    public static void assertXML(String arg1, String arg2) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        final Diff diff = XMLUnit.compareXML(arg1, arg2);
        Assert.assertTrue(diff.identical());
    }

    /**
     * Assert that Java code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct java code
     * @param fileName of the program we want to generate java code
     * @throws Exception
     */
    public static void assertCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), Helper.generateStringWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    public static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

}
