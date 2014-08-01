package de.fhg.iais.roberta.ast.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class IfStmtTest {

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
    public void getExpr() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        IfStmt ifStmt = (IfStmt) transformer.getProject().get(0).get(0);

        String a = "[Binary [EQ, EmptyExpr [defVal=class java.lang.Integer], EmptyExpr [defVal=class java.lang.Integer]]]";
        Assert.assertEquals(a, ifStmt.getExpr().toString());
    }

    @Test
    public void getThen() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        IfStmt ifStmt = (IfStmt) transformer.getProject().get(0).get(0);

        String a = "[\nexprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]]";
        Assert.assertEquals(a, ifStmt.getThenList().toString());
    }

    @Test
    public void getElse() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        IfStmt ifStmt = (IfStmt) transformer.getProject().get(0).get(0);

        String a = "\nSensorStmt DrehSensor [mode=RESET, motor=A]";
        Assert.assertEquals(a, ifStmt.getElseList().toString());
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
    public void ifStmt7() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt7.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a =
            "BlockAST [project=[[\n"
                + "if NullConst [null]\n"
                + ",then\n"
                + "AktionStmt [DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=null]]]\n"
                + "AktionStmt [MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]]\n"
                + "]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt8() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt8.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a = "BlockAST [project=[[\nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt9() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt9.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a = "BlockAST [project=[[\nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void ifStmt10() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(ControlTest.class.getResourceAsStream("/ast/control/if_stmt10.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        String a = "BlockAST [project=[[\nif EmptyExpr [defVal=class java.lang.Boolean]\n,then\n]]]";
        Assert.assertEquals(a, transformer.toString());
    }

}
