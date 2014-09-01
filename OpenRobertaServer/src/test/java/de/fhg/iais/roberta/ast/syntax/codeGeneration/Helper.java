package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.AstToLejosJavaVisitor;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 * 
 * @author kcvejoski
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
        JaxbTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
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
        JaxbTransformer<Void> transformer = generateTransformer(pathToProgramXml);
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
    public static JaxbTransformer<Void> generateTransformer(String pathToProgramXml) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Helper.class.getResourceAsStream(pathToProgramXml));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer<Void> transformer = new JaxbTransformer<>();
        transformer.projectToAST(project);
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
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Helper.class.getResourceAsStream(pathToProgramXml));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer<V> transformer = new JaxbTransformer<V>();
        transformer.projectToAST(project);
        List<Phrase<V>> tree = transformer.getTree();
        return tree;
    }

    public static <V> Phrase<V> generateAST(String pathToProgramXml) throws Exception {
        List<Phrase<V>> tree = generateASTs(pathToProgramXml);
        return tree.get(0);
    }
}
