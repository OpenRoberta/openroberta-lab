package de.fhg.iais.roberta.ast.stmt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class RepeatStmtTest {

    @Test
    public void repeatStmt() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [TIMES, NumConst [10]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba1]]\n"
                + "(repeat [TIMES, NumConst [10]]\n"
                + ")\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void getMode() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getProject().get(0).get(0);

        Assert.assertEquals(Mode.TIMES, repeatStmt.getMode());
    }

    @Test
    public void getExpr() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getProject().get(0).get(0);

        Assert.assertEquals("NumConst [10]", repeatStmt.getExpr().toString());
    }

    @Test
    public void getList() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        RepeatStmt repeatStmt = (RepeatStmt) transformer.getProject().get(0).get(0);

        String a =
            "\nexprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [Proba1]]\n"
                + "(repeat [TIMES, NumConst [10]]\n"
                + ")";

        Assert.assertEquals(a, repeatStmt.getList().toString());
    }

    @Test
    public void repeatStmt1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[\n" + "(repeat [TIMES, NumConst [10]]\n)]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtWhileUntil() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_whileUntil.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtWhileUntil1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_whileUntil1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [WHILE, BoolConst [true]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [sd]]\n"
                + "exprStmt Binary [MATH_CHANGE, Var [item], NumConst [1]]\n"
                + "StmtFlowCon [BREAK]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtWhileUntil2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_whileUntil2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[\n" + "(repeat [WHILE, EmptyExpr [defVal=class java.lang.Boolean]]\n)]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtFor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_for.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR, Binary [ASSIGNMENT, Var [i], NumConst [1]], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [kllk]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtFor1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_for1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR, Binary [ASSIGNMENT, Var [i], NumConst [1]], Binary [LTE, Var [i], NumConst [10]], Binary [ADD_ASSIGNMENT, Var [i], NumConst [1]]]\n)]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtForEach() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_for_each.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "(repeat [FOR_EACH, Binary [IN, Var [j], EmptyExpr [defVal=interface java.util.List]]]\n"
                + "exprStmt Binary [TEXT_APPEND, Var [item], StringConst [gg]]\n"
                + ")]]]";
        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void repeatStmtForEach1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(RepeatStmtTest.class.getResourceAsStream("/ast/control/repeat_stmt_for_each1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[\n" + "(repeat [FOR_EACH, Binary [IN, Var [i], EmptyExpr [defVal=interface java.util.List]]]\n)]]]";
        Assert.assertEquals(a, transformer.toString());
    }

}
