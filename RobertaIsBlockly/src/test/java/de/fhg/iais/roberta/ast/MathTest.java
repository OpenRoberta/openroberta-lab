package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.BlockAST;
import de.fhg.iais.roberta.blockly.generated.Project;

public class MathTest {

    @Test
    public void mathArithmetic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_arithmetic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [ADD, NumConst [1], Binary [POWER, NumConst [5], NumConst [8]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathSingle() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_single.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [LN, Binary [POWER, NumConst [5], NumConst [8]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathTrig() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_trig.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [ATAN, Unary [LN, Binary [POWER, NumConst [5], NumConst [8]]]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathConstant() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_constant.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [COS, MathConst [E]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathNumberProperty() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_number_property.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [PRIME, NumConst [0]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathNumberProperty1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_number_property1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [DIVISIBLE_BY, NumConst [8], NumConst [5]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathChange() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_change.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [MATH_CHANGE, Var [item], NumConst [1]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void mathRound() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_round.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [ROUNDUP, NumConst [0]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void math_on_list() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_on_list.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Unary [AVERAGE, EmptyExpr [defVal=class java.util.ArrayList]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void math_on_constrain() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/math/math_constrain.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        BlockAST transformer = new BlockAST();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[Binary [MIN, Binary [MAX, NumConst [1], NumConst [8]], NumConst [100]]]]]";

        Assert.assertEquals(a, transformer.toString());
    }
}
