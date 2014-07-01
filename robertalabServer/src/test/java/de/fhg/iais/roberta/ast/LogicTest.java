package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class LogicTest {

    @Test
    public void logicCompare() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_compare.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_compare1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [EQ, NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_compare2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [NEQ, Binary [EQ, NumConst [2], NumConst [2]], NumConst [1]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare3() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_compare3.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [NEQ, Binary [GT, NumConst [2], NumConst [2]], Binary [LT, NumConst [6], NumConst [8]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare4() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_compare4.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [NEQ, Binary [GT, NumConst [2], Binary [LTE, NumConst [6], NumConst [5]]], NumConst [8]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logic_operation() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_operation.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [AND, EmptyExpr [defVal=class java.lang.Boolean], EmptyExpr [defVal=class java.lang.Boolean]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logic_negate() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_negate.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [NOT, EmptyExpr [defVal=class java.lang.Boolean]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logic_negate1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_negate1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [NOT, Binary [OR, BoolConst [true], BoolConst [false]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logic_null() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(LogicTest.class.getResourceAsStream("/ast/logic/logic_null.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[NullConst [null]]]]";

        Assert.assertEquals(a, transformer.toString());
    }
}
