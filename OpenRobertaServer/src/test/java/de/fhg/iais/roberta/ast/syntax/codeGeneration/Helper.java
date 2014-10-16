package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformer;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.codegen.lejos.AstToLejosJavaVisitor;
import de.fhg.iais.roberta.jaxb.JaxbHelper;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper {
    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    public static String generateStringWithoutWrapping(String pathToProgramXml) throws Exception {
        JaxbProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        BrickConfiguration brickConfiguration =
            new BrickConfiguration.Builder()
                .addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.C, new HardwareComponent(HardwareComponentType.EV3LargeUnRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.D, new HardwareComponent(HardwareComponentType.EV3MediumUnRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .build();
        String code = AstToLejosJavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), false);
        System.out.println(code);
        return code;
    }

    /**
     * Generate java code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public static String generateString(String pathToProgramXml, BrickConfiguration brickConfiguration) throws Exception {
        JaxbProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = AstToLejosJavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), true);
        System.out.println(code);
        return code;
    }

    /**
     * return the jaxb transformer for a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return jaxb transformer
     * @throws Exception
     */
    public static JaxbProgramTransformer<Void> generateTransformer(String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        JaxbProgramTransformer<Void> transformer = new JaxbProgramTransformer<>();
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
    public static <V> List<Phrase<V>> generateASTs(String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        JaxbProgramTransformer<V> transformer = new JaxbProgramTransformer<V>();
        transformer.transform(project);
        List<Phrase<V>> tree = transformer.getTree();
        return tree;
    }

    public static <V> Phrase<V> generateAST(String pathToProgramXml) throws Exception {
        List<Phrase<V>> tree = generateASTs(pathToProgramXml);
        return tree.get(1);
    }

    public static void assertTransformationIsOk(String fileName) throws Exception {
        JaxbProgramTransformer<Void> transformer = generateTransformer(fileName);

        AstJaxbTransformer<Void> astTransformer = new AstJaxbTransformer<>();
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        BlockSet blockSet = new BlockSet();

        Instance instance = null;
        for ( Phrase<Void> phrase : transformer.getTree() ) {
            if ( phrase.getKind() == Kind.LOCATION ) {
                blockSet.getInstance().add(instance);
                instance = new Instance();
                instance.setX(((Location<Void>) phrase).getX());
                instance.setY(((Location<Void>) phrase).getY());
            }
            instance.getBlock().add(astTransformer.astToBlock(phrase));
        }
        blockSet.getInstance().add(instance);

        m.marshal(blockSet, System.out);
        StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        String t = Resources.toString(Helper.class.getResource(fileName), Charsets.UTF_8);

        Assert.assertEquals(writer.toString().replaceAll("<(\\w+)( [^/>]*)?/>", "<$1$2></$1>").replaceAll("\\s+", ""), t.replaceAll("\\s+", ""));
    }

    public static void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateStringWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

}
