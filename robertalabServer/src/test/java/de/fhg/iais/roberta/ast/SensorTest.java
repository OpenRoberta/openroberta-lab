package de.fhg.iais.roberta.ast;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class SensorTest {

    @Test
    public void sensorSet() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_set.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=_1]]\n"
                + ",then\n"
                + "   SensorStmt UltraSSensor [mode=DISTANCE, port=_4]\n"
                + "   SensorStmt ColorSensor [mode=COLOUR, port=_3]\n"
                + "   SensorStmt InfraredSensor [mode=DISTANCE, port=_4]\n"
                + "   SensorStmt DrehSensor [mode=ROTATION, motor=A]\n"
                + "   SensorStmt GyroSensor [mode=ANGLE, port=_2]\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorReset() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_reset.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=_1]]\n"
                + ",then\n"
                + "   SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "   SensorStmt GyroSensor [mode=RESET, port=_2]\n"
                + "   SensorStmt TimerSensor [mode=RESET, timer=1]\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetMode() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getMode.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=_1]]\n"
                + ",then\n"
                + "   Var [item] := SensorExpr [UltraSSensor [mode=GET_MODE, port=_4]]\n\n"
                + "   Var [item] := SensorExpr [ColorSensor [mode=GET_MODE, port=_3]]\n\n"
                + "   Var [item] := SensorExpr [InfraredSensor [mode=GET_MODE, port=_4]]\n\n"
                + "   Var [item] := SensorExpr [DrehSensor [mode=GET_MODE, motor=A]]\n\n"
                + "   Var [item] := SensorExpr [GyroSensor [mode=GET_MODE, port=_2]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSample() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSample.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=_1]]\n"
                + ",then\n"
                + "   Var [item] := SensorExpr [UltraSSensor [mode=GET_SAMPLE, port=_4]]\n\n"
                + "   Var [item] := SensorExpr [ColorSensor [mode=GET_SAMPLE, port=_3]]\n\n"
                + "   Var [item] := SensorExpr [InfraredSensor [mode=GET_SAMPLE, port=_4]]\n\n"
                + "   Var [item] := SensorExpr [DrehSensor [mode=GET_SAMPLE, motor=A]]\n\n"
                + "   Var [item] := SensorExpr [GyroSensor [mode=GET_SAMPLE, port=_2]]\n\n"
                + "   Var [item] := SensorExpr [TimerSensor [mode=GET_SAMPLE, timer=1]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorBrick() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_brick.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=_1]]\n"
                + ",then\n"
                + "   Var [item] := SensorExpr [BrickSensor [key=ENTER, mode=IS_PRESSED]]\n\n"
                + "   Var [item] := SensorExpr [BrickSensor [key=LEFT, mode=WAIT_FOR_PRESS_AND_RELEASE]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }
}
