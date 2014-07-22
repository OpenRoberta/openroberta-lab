package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class ControlTest {
    @Test
    public void ifStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt3() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt3.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt4() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt4.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt5() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt5.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else if Binary [EQ, NumConst [1], NumConst [1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt6() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt6.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]\n"
                + ",then\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ",else\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/repeat_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [TIMES, NumConst [10]]\n"
                + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba]]\n"
                + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba1]]\n"
                + "   (repeat [TIMES, NumConst [10]]\n"
                + "   )\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtWhileUntil() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/repeat_stmt_whileUntil.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "   exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtWhileUntil1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/repeat_stmt_whileUntil1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "   exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + "   StmtFlowCon [BREAK]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtFor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/repeat_stmt_for.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n" + "(repeat [FOR, i = 1, i <= 10, i + 1]\n" + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [kllk]]\n" + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtForEach() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/repeat_stmt_for_each.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR_EACH, Binary [IN, Var [j], EmptyExpr [defVal=interface java.util.List]]]\n"
                + "   exprStmt Binary [TEXT_APPEND, Var [item], StringConst [gg]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void robWait() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/wait_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "   exprStmt Funct [PRINT, [StringConst [1]]]\n"
                + ")\n(repeat [WHILE, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "   exprStmt Funct [PRINT, [StringConst [2]]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }
}
