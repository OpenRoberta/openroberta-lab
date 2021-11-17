package de.fhg.iais.roberta.visitor;

import java.util.Arrays;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedMethodCollectorWorker;
import de.fhg.iais.roberta.worker.validate.UnoConfigurationValidatorWorker;

public class ArduinoValidatorAndCollectorWorkflowTest extends WorkflowTest {

    public ArduinoValidatorAndCollectorWorkflowTest() {
        setupRobotFactory("uno");
        workerChain = Arrays.asList(new UnoConfigurationValidatorWorker(), new ArduinoUsedHardwareCollectorWorker(), new ArduinoUsedMethodCollectorWorker());
    }

    @Test
    public void visitKeysSensor_withoutPort() {
        KeysSensor<Void> keysSensor = new KeysSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNepoInfo(keysSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitKeysSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.KEY, false, "P1", "P1", new HashMap<>()));

        KeysSensor<Void> keysSensor = new KeysSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNoNepoInfo(keysSensor);
    }

    @Test
    public void visitMoistureSensor_withoutPort() {
        MoistureSensor<Void> moistureSensor = new MoistureSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(moistureSensor);

        executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMoistureSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MoistureSensor<Void> moistureSensor = new MoistureSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(moistureSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOISTURE, "");
        assertHasNoNepoInfo(moistureSensor);
    }

    @Test
    public void visitMoistureSensor_withPortWrongSensor() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MoistureSensor<Void> moistureSensor = new MoistureSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(moistureSensor);

        Project project = executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR ,"CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotionSensor_withoutPort() {
        MotionSensor<Void> motionSensor = new MotionSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMotion_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MotionSensor<Void> motionSensor = new MotionSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(motionSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOTION, "");
        assertHasNoNepoInfo(motionSensor);
    }

    @Test
    public void visitMotion_withPortWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MotionSensor<Void> motionSensor = new MotionSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitPulseSensor_withoutPort() {
        PulseSensor<Void> pulseSensor = new PulseSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(pulseSensor);

        executeWorkflow();

        assertHasNepoInfo(pulseSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.DROP, false, "P1", "P1", new HashMap<>()));

        DropSensor<Void> dropSensor = new DropSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(dropSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.DROP, "");
        assertHasNoNepoInfo(dropSensor);
    }

    @Test
    public void visitDropSensor_withoutPort() {
        DropSensor<Void> dropSensor = new DropSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        DropSensor<Void> dropSensor = new DropSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitRfidSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = new RfidSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(rfidSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.RFID, "");
        assertHasNoNepoInfo(rfidSensor);
    }

    @Test
    public void visitRfidSensor_onUnoWifiRev2() {
        setupRobotFactory("unowifirev2");

        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = new RfidSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.WARNING, "BLOCK_NOT_SUPPORTED");
    }

    @Test
    public void visitRfidSensor_withoutPort() {
        RfidSensor<Void> rfidSensor = new RfidSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitRfidSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = new RfidSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitHumiditySensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.HUMIDITY, false, "P1", "P1", new HashMap<>()));

        HumiditySensor<Void> humiditySensor = new HumiditySensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(humiditySensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.HUMIDITY, "");
        assertHasNoNepoInfo(humiditySensor);
    }

    @Test
    public void visitHumiditySensor_withoutPort() {
        HumiditySensor<Void> humiditySensor = new HumiditySensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitHumiditySensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        HumiditySensor<Void> humiditySensor = new HumiditySensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitVoltageSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.POTENTIOMETER, false, "P1", "P1", new HashMap<>()));

        VoltageSensor<Void> voltageSensor = new VoltageSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(voltageSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.VOLTAGE, "");
        assertHasNoNepoInfo(voltageSensor);
    }

    @Test
    public void visitVoltageSensor_withoutPort() {
        VoltageSensor<Void> voltageSensor = new VoltageSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitVoltageSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        VoltageSensor<Void> voltageSensor = new VoltageSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitEncoderSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.ENCODER, false, "P1", "P1", new HashMap<>()));

        EncoderSensor<Void> encoderSensor = new EncoderSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(encoderSensor);

        executeWorkflow();

        assertHasNoNepoInfo(encoderSensor);
    }

    @Test
    public void visitEncoderSensor_withoutPort() {
        EncoderSensor<Void> encoderSensor = new EncoderSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(encoderSensor);

        executeWorkflow();

        assertHasNepoInfo(encoderSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitEncoderSensor_withWrongType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        EncoderSensor<Void> encoderSensor = new EncoderSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(encoderSensor);

        executeWorkflow();

        assertHasNepoInfo(encoderSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotorOnAction() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTOR_DRIVE, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(new NumConst<>("10"))
            .duration(new MotorDuration<>(null, new NumConst<>("10")))
            .build();

        MotorOnAction<Void> motorOnAction = new MotorOnAction<>("P1", motionParam);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    @Test
    public void visitMotorOnAction_withoutPort() {
        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(new NumConst<>("10"))
            .duration(new MotorDuration<>(null, new NumConst<>("10")))
            .build();

        MotorOnAction<Void> motorOnAction = new MotorOnAction<>("P1", motionParam);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitMotorOnAction_withOtherType() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(new NumConst<>("10"))
            .duration(new MotorDuration<>(null, new NumConst<>("10")))
            .build();

        MotorOnAction<Void> motorOnAction = new MotorOnAction<>("P1", motionParam);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED");
    }

    @Test
    public void visitMotorOnAction_withoutDuration() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(new NumConst<>("10"))
            .build();

        MotorOnAction<Void> motorOnAction = new MotorOnAction<>("P1", motionParam);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    private UsedHardwareBean getUsedHardwareBean(Project project) {
        return project.getWorkerResult(UsedHardwareBean.class);
    }

    private void assertHasUsedSensor(Project project, String userDefinedPort, String type, String mode) {
        UsedHardwareBean usedHardwareBean = getUsedHardwareBean(project);
        assertHasUsedSensor(usedHardwareBean, userDefinedPort, type, mode);
    }

    private void assertHasUsedSensor(UsedHardwareBean usedHardwareBean, String userDefinedPort, String type, String mode) {
        Assertions.assertThat(usedHardwareBean.getUsedSensors()).anySatisfy(usedSensor -> {
            Assertions.assertThat(usedSensor.getPort()).isEqualTo(userDefinedPort);
            Assertions.assertThat(usedSensor.getMode()).isEqualTo(mode);
            Assertions.assertThat(usedSensor.getType()).isEqualTo(type);
        });
    }

    private void assertHasUsedActor(Project project, String userDefinedPort, String type) {
        UsedHardwareBean usedHardwareBean = getUsedHardwareBean(project);
        assertHasUsedActor(usedHardwareBean, userDefinedPort, type);
    }

    private void assertHasUsedActor(UsedHardwareBean usedHardwareBean, String userDefinedPort, String type) {
        Assertions.assertThat(usedHardwareBean.getUsedActors()).anySatisfy(usedActor -> {
            Assertions.assertThat(usedActor.getPort()).isEqualTo(userDefinedPort);
            Assertions.assertThat(usedActor.getType()).isEqualTo(type);
        });
    }

    private void assertHasNoNepoInfo(Phrase<Void> phrase) {
        Assertions.assertThat(phrase.getInfos().getInfos()).isEmpty();
    }

    private void assertHasNepoInfo(Phrase<Void> phrase, NepoInfo.Severity severity, String message) {
        Assertions.assertThat(phrase.getInfos().getInfos())
            .as(String.format("Phrase %s should have NepoInfo %s with message \"%s\"", phrase, severity, message))
            .anySatisfy((nepoInfo) -> {
                Assertions.assertThat(nepoInfo.getMessage()).isEqualTo(message);
                Assertions.assertThat(nepoInfo.getSeverity()).isEqualTo(severity);
            });
    }
}