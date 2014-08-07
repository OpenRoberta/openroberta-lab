package de.fhg.iais.roberta.helper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.codegen.lejos.GenerateCode;
import de.fhg.iais.roberta.codegen.lejos.JavaGenerateCode;
import de.fhg.iais.roberta.codegen.lejos.JavaWrappCode;
import de.fhg.iais.roberta.codegen.lejos.ProgramToCode;
import de.fhg.iais.roberta.codegen.lejos.WrappCode;

public class Helper {
    public static String generateSyntax(String programName) throws Exception {
        Project project = createJAXBTree(programName);

        JaxbTransformer transformer = jaxbToAst(project);

        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaGenerateCode generator = new JavaGenerateCode(brickConfiguration, transformer.getTree());

        generator.generate(0);

        System.out.println(generator.getSb());
        return generator.getSb().toString();
    }

    public static String generateASTString(String programName) throws Exception {
        Project project = createJAXBTree(programName);
        JaxbTransformer transformer = jaxbToAst(project);
        return transformer.toString();
    }

    public static JaxbTransformer generateAST(String programName) throws Exception {
        Project project = createJAXBTree(programName);
        return jaxbToAst(project);
    }

    public static String generateProgram(String programLocation) throws Exception {
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        WrappCode wrappCode = new JavaWrappCode("Test", brickConfiguration);
        GenerateCode generateCode = new JavaGenerateCode(brickConfiguration, null);
        ProgramToCode programToCode = new ProgramToCode(programLocation, wrappCode, generateCode);
        programToCode.generate();
        System.out.println(programToCode.getSb().toString());
        return programToCode.getSb().toString();
    }

    private static JaxbTransformer jaxbToAst(Project project) {
        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        return transformer;
    }

    private static Project createJAXBTree(String programName) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream(programName));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);
        return project;
    }

}
