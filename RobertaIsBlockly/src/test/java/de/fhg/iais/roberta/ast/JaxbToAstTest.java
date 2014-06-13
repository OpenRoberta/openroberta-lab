package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.BlockAST;
import de.fhg.iais.roberta.blockly.generated.Project;

public class JaxbToAstTest {

    @Test
    public void Test() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(JaxbToAstTest.class.getResourceAsStream("/test_1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat Int[10]\n"
                + "   exprStmt Binary[TEXT_APPEND, Var[item], String[Proba]]\n"
                + "   exprStmt Binary[TEXT_APPEND, Var[item], String[Proba1]]\n"
                + "   (repeat Int[10]\n"
                + "   )\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void Test1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(JaxbToAstTest.class.getResourceAsStream("/test_2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "(if Binary[EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "   exprStmt Binary[MATH_CHANGE, Var[item], Int[1]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void Test4() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(JaxbToAstTest.class.getResourceAsStream("/test_4.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "(if Binary[EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "   exprStmt Binary[MATH_CHANGE, Var[item], Int[1]]\n"
                + ",else\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }
}
