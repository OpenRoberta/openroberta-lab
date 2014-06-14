package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.BlockAST;
import de.fhg.iais.roberta.blockly.generated.Project;

public class Logic {

    @Test
    public void logicCompare() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Logic.class.getResourceAsStream("/ast/logic/logic_compare.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Logic.class.getResourceAsStream("/ast/logic/logic_compare1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [EQ, NumConst [0], NumConst [0]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Logic.class.getResourceAsStream("/ast/logic/logic_compare2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [NEQ, Binary [EQ, NumConst [2], NumConst [2]], NumConst [1]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void logicCompare3() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Logic.class.getResourceAsStream("/ast/logic/logic_compare3.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [NEQ, Binary [GT, NumConst [2], NumConst [2]], Binary [LT, NumConst [6], NumConst [8]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }
}
