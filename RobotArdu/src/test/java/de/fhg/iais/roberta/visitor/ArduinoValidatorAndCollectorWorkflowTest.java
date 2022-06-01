package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.mode.action.RelayMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.worker.validate.UnoValidatorAndCollectorWorker;

public class ArduinoValidatorAndCollectorWorkflowTest extends WorkflowTestHelper {

    public ArduinoValidatorAndCollectorWorkflowTest() {
        setupRobotFactory("uno");
        workerChain = Arrays.asList(new UnoValidatorAndCollectorWorker());
    }

    @Test
    public void visitKeysSensor_withoutPort() {
        KeysSensor<Void> keysSensor = KeysSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNepoInfo(keysSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitKeysSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.KEY, false, "P1", "P1", new HashMap<>()));

        KeysSensor<Void> keysSensor = KeysSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNoNepoInfo(keysSensor);
    }

    @Test
    public void visitMoistureSensor_withoutPort() {
        MoistureSensor<Void> moistureSensor = MoistureSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(moistureSensor);

        executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMoistureSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MoistureSensor<Void> moistureSensor = MoistureSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(moistureSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOISTURE, "");
        assertHasNoNepoInfo(moistureSensor);
    }

    @Test
    public void visitMoistureSensor_withPortWrongSensor() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MoistureSensor<Void> moistureSensor = MoistureSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(moistureSensor);

        executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotionSensor_withoutPort() {
        MotionSensor<Void> motionSensor = MotionSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMotion_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MotionSensor<Void> motionSensor = MotionSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(motionSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOTION, "");
        assertHasNoNepoInfo(motionSensor);
    }

    @Test
    public void visitMotion_withPortWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MotionSensor<Void> motionSensor = MotionSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitPulseSensor_withoutPort() {
        PulseSensor<Void> pulseSensor = PulseSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(pulseSensor);

        executeWorkflow();

        assertHasNepoInfo(pulseSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.DROP, false, "P1", "P1", new HashMap<>()));

        DropSensor<Void> dropSensor = DropSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(dropSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.DROP, "");
        assertHasNoNepoInfo(dropSensor);
    }

    @Test
    public void visitDropSensor_withoutPort() {
        DropSensor<Void> dropSensor = DropSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        DropSensor<Void> dropSensor = DropSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitRfidSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = RfidSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(rfidSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.RFID, "");
        assertHasNoNepoInfo(rfidSensor);
    }

    @Test
    public void visitRfidSensor_onUnoWifiRev2() {
        setupRobotFactory("unowifirev2");

        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = RfidSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.WARNING, "BLOCK_NOT_SUPPORTED");
    }

    @Test
    public void visitRfidSensor_withoutPort() {
        RfidSensor<Void> rfidSensor = RfidSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitRfidSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        RfidSensor<Void> rfidSensor = RfidSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitHumiditySensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.HUMIDITY, false, "P1", "P1", new HashMap<>()));

        HumiditySensor<Void> humiditySensor = HumiditySensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(humiditySensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.HUMIDITY, "");
        assertHasNoNepoInfo(humiditySensor);
    }

    @Test
    public void visitHumiditySensor_withoutPort() {
        HumiditySensor<Void> humiditySensor = HumiditySensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitHumiditySensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        HumiditySensor<Void> humiditySensor = HumiditySensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitVoltageSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.POTENTIOMETER, false, "P1", "P1", new HashMap<>()));

        VoltageSensor<Void> voltageSensor = VoltageSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(voltageSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.VOLTAGE, "");
        assertHasNoNepoInfo(voltageSensor);
    }

    @Test
    public void visitVoltageSensor_withoutPort() {
        VoltageSensor<Void> voltageSensor = VoltageSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitVoltageSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        VoltageSensor<Void> voltageSensor = VoltageSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotorOnAction() {
        configurationComponents.add(new ConfigurationComponent(SC.STEPMOTOR, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(NumConst.make("10"))
            .duration(new MotorDuration<>(null, NumConst.make("10")))
            .build();

        MotorOnAction<Void> motorOnAction = MotorOnAction.make("P1", motionParam, bp, null);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    @Test
    public void visitMotorOnAction_withoutPort() {
        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(NumConst.make("10"))
            .duration(new MotorDuration<>(null, NumConst.make("10")))
            .build();

        MotorOnAction<Void> motorOnAction = MotorOnAction.make("P1", motionParam, bp, null);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_MOTOR_MISSING");
    }

    @Test
    public void visitMotorOnAction_withOtherType() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(NumConst.make("10"))
            .duration(new MotorDuration<>(null, NumConst.make("10")))
            .build();

        MotorOnAction<Void> motorOnAction = MotorOnAction.make("P1", motionParam, bp, null);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED");
    }

    @Test
    public void visitMotorOnAction_withoutDuration() {
        configurationComponents.add(new ConfigurationComponent(SC.STEPMOTOR, true, "P1", "P1", new HashMap<>()));

        MotionParam<Void> motionParam = new MotionParam.Builder<Void>()
            .speed(NumConst.make("10"))
            .build();

        MotorOnAction<Void> motorOnAction = MotorOnAction.make("P1", motionParam, bp, null);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    @Test
    public void visitLightAction() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        RgbColor<Void> rgbColor = RgbColor.make(bp, null, NumConst.make("10"), NumConst.make("10"), NumConst.make("10"), NumConst.make("10"));

        LightAction<Void> lightAction = LightAction.make("P1", BrickLedColor.ORANGE, LightMode.DEFAULT, rgbColor, bp, null);
        phrases.add(lightAction);

        executeWorkflow();

        assertHasNoNepoInfo(lightAction);
    }

    @Test
    public void visitLightAction_noActor() {
        RgbColor<Void> rgbColor = RgbColor.make(bp, null, NumConst.make("10"), NumConst.make("10"), NumConst.make("10"), NumConst.make("10"));

        LightAction<Void> lightAction = LightAction.make("P1", BrickLedColor.ORANGE, LightMode.DEFAULT, rgbColor, bp, null);
        phrases.add(lightAction);

        executeWorkflow();

        assertHasNepoInfo(lightAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitPlayNoteAction() {
        configurationComponents.add(new ConfigurationComponent(SC.BUZZER, true, "P1", "P1", new HashMap<>()));

        PlayNoteAction<Void> playNoteAction = PlayNoteAction.make("P1", "100", "100", bp, null, null);
        phrases.add(playNoteAction);

        executeWorkflow();

        assertHasNoNepoInfo(playNoteAction);
    }

    @Test
    public void visitPlayNoteAction_noPort() {
        PlayNoteAction<Void> playNoteAction = PlayNoteAction.make("P1", "100", "100", bp, null, null);
        phrases.add(playNoteAction);

        executeWorkflow();

        assertHasNepoInfo(playNoteAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitLightStatusActionOff() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        LightStatusAction<Void> lightStatusAction = LightStatusAction.make("P1", LightStatusAction.Status.OFF, bp, null);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNoNepoInfo(lightStatusAction);
    }

    @Test
    public void visitLightStatusActionOff_noPort() {
        LightStatusAction<Void> lightStatusAction = LightStatusAction.make("P1", LightStatusAction.Status.OFF, bp, null);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNepoInfo(lightStatusAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitLightStatusActionReset() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        LightStatusAction<Void> lightStatusAction = LightStatusAction.make("P1", LightStatusAction.Status.RESET, bp, null);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNoNepoInfo(lightStatusAction);
    }

    @Test
    public void visitLightStatusActionReset_noPort() {
        LightStatusAction<Void> lightStatusAction = LightStatusAction.make("P1", LightStatusAction.Status.RESET, bp, null);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNepoInfo(lightStatusAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitRelayAction() {
        configurationComponents.add(new ConfigurationComponent(SC.RELAY, true, "P1", "P1", new HashMap<>()));

        RelayAction<Void> relayAction = RelayAction.make("P1", RelayMode.DEFAULT, bp, null);
        phrases.add(relayAction);

        executeWorkflow();
        assertHasNoNepoInfo(relayAction);
    }

    @Test
    public void visitRelayAction_noPort() {
        RelayAction<Void> relayAction = RelayAction.make("P1", RelayMode.DEFAULT, bp, null);
        phrases.add(relayAction);

        executeWorkflow();
        assertHasNepoInfo(relayAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitShowTexAction() {
        configurationComponents.add(new ConfigurationComponent(SC.DISPLAY, true, "P1", "P1", new HashMap<>()));

        ShowTextAction<Void> showTextAction = ShowTextAction.make(NumConst.make("Text"), NumConst.make("0"), NumConst.make("0"), "P1", bp, null);
        phrases.add(showTextAction);

        executeWorkflow();
        assertHasNoNepoInfo(showTextAction);
    }

    @Test
    public void visitPinGetValueSensor() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, false, "P1", "P1", new HashMap<>()));

        PinGetValueSensor<Void> pinGetValueSensor = PinGetValueSensor.make(new SensorMetaDataBean("P1", "", "", null), bp, null);
        phrases.add(pinGetValueSensor);

        executeWorkflow();
        assertHasNoNepoInfo(pinGetValueSensor);
    }

    @Test
    public void visitPinWriteValueAction() {
        configurationComponents.add(new ConfigurationComponent(SC.DIGITAL_PIN, true, "P1", "P1", new HashMap<>()));

        PinWriteValueAction<Void> pinWriteValueAction = PinWriteValueAction.make("1", "P1", NumConst.make("1"), true, bp, null);
        phrases.add(pinWriteValueAction);

        executeWorkflow();
        assertHasNoNepoInfo(pinWriteValueAction);
    }

    @Test
    public void visitPinWriteValueAction_noPort() {
        PinWriteValueAction<Void> pinWriteValueAction = PinWriteValueAction.make("1", "P1", NumConst.make("1"), true, bp, null);
        phrases.add(pinWriteValueAction);

        executeWorkflow();
        assertHasNepoInfo(pinWriteValueAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitIndexOfFunct() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        IndexOfFunct<Void> listSetIndex = IndexOfFunct.make(IndexLocation.FIRST, param, bp, null);
        phrases.add(listSetIndex);

        executeWorkflow();
        assertHasNoNepoInfo(listSetIndex);
    }

    @Test
    public void visitIndexOfFunct_noFirsstElement() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("ListCreate "));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        IndexOfFunct<Void> listSetIndex = IndexOfFunct.make(IndexLocation.FIRST, param, bp, null);
        phrases.add(listSetIndex);

        executeWorkflow();
        assertHasNepoInfo(listSetIndex, NepoInfo.Severity.ERROR, "BLOCK_USED_INCORRECTLY");
    }

    @Test
    public void visitListGetIndex() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        ListGetIndex<Void> listGetIndex = ListGetIndex.make(ListElementOperations.GET, IndexLocation.FIRST, param, null, bp, null);
        phrases.add(listGetIndex);

        executeWorkflow();
        assertHasNoNepoInfo(listGetIndex);
    }

    @Test
    public void visitListGetIndex_noFirsstElement() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("ListCreate "));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        ListGetIndex<Void> listGetIndex = ListGetIndex.make(ListElementOperations.GET, IndexLocation.FIRST, param, null, bp, null);
        phrases.add(listGetIndex);

        executeWorkflow();
        assertHasNepoInfo(listGetIndex, NepoInfo.Severity.ERROR, "BLOCK_USED_INCORRECTLY");
    }

    @Test
    public void visitLengthOfIsEmptyFunct() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct = LengthOfIsEmptyFunct.make(FunctionNames.LIST_IS_EMPTY, param, bp, null);
        phrases.add(lengthOfIsEmptyFunct);

        executeWorkflow();
        assertHasNoNepoInfo(lengthOfIsEmptyFunct);
    }

    @Test
    public void visitLengthOfIsEmptyFunct_noFirsstElement() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("ListCreate "));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct = LengthOfIsEmptyFunct.make(FunctionNames.LIST_IS_EMPTY, param, bp, null);
        phrases.add(lengthOfIsEmptyFunct);

        executeWorkflow();
        assertHasNepoInfo(lengthOfIsEmptyFunct, NepoInfo.Severity.ERROR, "BLOCK_USED_INCORRECTLY");
    }

    @Test
    public void visitMathOnListFunct() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        MathOnListFunct<Void> mathOnListFunct = MathOnListFunct.make(FunctionNames.LIST_IS_EMPTY, param, bp, null);
        phrases.add(mathOnListFunct);

        executeWorkflow();
        assertHasNoNepoInfo(mathOnListFunct);
    }

    @Test
    public void visitMathOnListFunct_noFirstElement() {
        List<Expr<Void>> param = new ArrayList<>();
        param.add(NumConst.make("ListCreate "));
        param.add(NumConst.make("10"));
        param.add(NumConst.make("10"));

        MathOnListFunct<Void> mathOnListFunct = MathOnListFunct.make(FunctionNames.LIST_IS_EMPTY, param, bp, null);
        phrases.add(mathOnListFunct);

        executeWorkflow();
        assertHasNepoInfo(mathOnListFunct, NepoInfo.Severity.ERROR, "BLOCK_USED_INCORRECTLY");
    }

}