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
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "SensorStmt UltraSSensor [mode=DISTANCE, port=S4]\n"
                + "SensorStmt ColorSensor [mode=COLOUR, port=S3]\n"
                + "SensorStmt InfraredSensor [mode=DISTANCE, port=S4]\n"
                + "SensorStmt DrehSensor [mode=ROTATION, motor=A]\n"
                + "SensorStmt GyroSensor [mode=ANGLE, port=S2]\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorSetGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[GyroSensor [mode=ANGLE, port=S2]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorSetEncoder() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[DrehSensor [mode=ROTATION, motor=A]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorSetInfrared() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setInfrared.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[InfraredSensor [mode=DISTANCE, port=S4]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorSetColor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[ColorSensor [mode=COLOUR, port=S3]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorSetUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_setUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[UltraSSensor [mode=DISTANCE, port=S4]]]]";

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
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "SensorStmt GyroSensor [mode=RESET, port=S2]\n"
                + "SensorStmt TimerSensor [mode=RESET, timer=1]\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorResetGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[GyroSensor [mode=RESET, port=S2]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorResetTimer() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetTimer.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[TimerSensor [mode=RESET, timer=1]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_resetEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[DrehSensor [mode=RESET, motor=A]]]]";

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
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltraSSensor [mode=GET_MODE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [mode=GET_MODE, port=S3]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [mode=GET_MODE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [DrehSensor [mode=GET_MODE, motor=A]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [mode=GET_MODE, port=S2]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetModeGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[GyroSensor [mode=GET_MODE, port=S2]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetModeEncoder() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[DrehSensor [mode=GET_MODE, motor=A]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetModeInfrared() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeInfrared.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[InfraredSensor [mode=GET_MODE, port=S4]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetModeColor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[ColorSensor [mode=GET_MODE, port=S3]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetModeUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getModeUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[UltraSSensor [mode=GET_MODE, port=S4]]]]";

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
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltraSSensor [mode=GET_SAMPLE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [mode=GET_SAMPLE, port=S3]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [mode=GET_SAMPLE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [DrehSensor [mode=GET_SAMPLE, motor=A]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [mode=GET_SAMPLE, port=S2]]\n\n"
                + "Var [item] := SensorExpr [TimerSensor [mode=GET_SAMPLE, timer=1]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleInfrared() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleInfrared.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[InfraredSensor [mode=GET_SAMPLE, port=S4]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleTimer() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleTimer.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[TimerSensor [mode=GET_SAMPLE, timer=1]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleGyro() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleGyro.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[GyroSensor [mode=GET_SAMPLE, port=S2]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleEncoder() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleEncoder.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[DrehSensor [mode=GET_SAMPLE, motor=A]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleColor() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleColor.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[ColorSensor [mode=GET_SAMPLE, port=S3]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorGetSampleUltrasonic() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_getSampleUltrasonic.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[UltraSSensor [mode=GET_SAMPLE, port=S4]]]]";

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
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [BrickSensor [key=ENTER, mode=IS_PRESSED]]\n\n"
                + "Var [item] := SensorExpr [BrickSensor [key=LEFT, mode=WAIT_FOR_PRESS_AND_RELEASE]]\n\n"
                + "]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorBrick1() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_brick1.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[BrickSensor [key=ENTER, mode=IS_PRESSED]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorBrick2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_brick2.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[BrickSensor [key=ENTER, mode=WAIT_FOR_PRESS_AND_RELEASE]]]]";

        Assert.assertEquals(a, transformer.toString());
    }

    @Test
    public void sensorTouch() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/sensors/sensor_Touch.xml"));
        Project project = (Project) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);

        String a = "BlockAST [project=[[TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, transformer.toString());
    }
}
