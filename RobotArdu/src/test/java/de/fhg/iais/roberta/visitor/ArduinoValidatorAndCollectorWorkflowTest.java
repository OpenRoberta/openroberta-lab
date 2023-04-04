package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.mode.action.RelayMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
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
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
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
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.worker.validate.UnoValidatorAndCollectorWorker;
import static org.junit.Assert.assertEquals;

public class ArduinoValidatorAndCollectorWorkflowTest extends WorkflowTestHelper {

    public ArduinoValidatorAndCollectorWorkflowTest() {
        setupRobotFactory("uno");
        workerChain = Arrays.asList(new UnoValidatorAndCollectorWorker());
    }

    @Test
    public void visitKeysSensor_withoutPort() {
        KeysSensor keysSensor = new KeysSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNepoInfo(keysSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitKeysSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.KEY, false, "P1", "P1", new HashMap<>()));

        KeysSensor keysSensor = new KeysSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow();

        assertHasNoNepoInfo(keysSensor);
    }

    @Test
    public void visitMoistureSensor_withoutPort() {
        MoistureSensor moistureSensor = new MoistureSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(moistureSensor);

        executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMoistureSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MoistureSensor moistureSensor = new MoistureSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(moistureSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOISTURE, "");
        assertHasNoNepoInfo(moistureSensor);
    }

    @Test
    public void visitMoistureSensor_withPortWrongSensor() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MoistureSensor moistureSensor = new MoistureSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(moistureSensor);

        executeWorkflow();

        assertHasNepoInfo(moistureSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotionSensor_withoutPort() {
        MotionSensor motionSensor = new MotionSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitMotion_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.MOTION, false, "P1", "P1", new HashMap<>()));

        MotionSensor motionSensor = new MotionSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(motionSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.MOTION, "");
        assertHasNoNepoInfo(motionSensor);
    }

    @Test
    public void visitMotion_withPortWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        MotionSensor motionSensor = new MotionSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(motionSensor);

        executeWorkflow();

        assertHasNepoInfo(motionSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitPulseSensor_withoutPort() {
        PulseSensor pulseSensor = new PulseSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(pulseSensor);

        executeWorkflow();

        assertHasNepoInfo(pulseSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.DROP, false, "P1", "P1", new HashMap<>()));

        DropSensor dropSensor = new DropSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(dropSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.DROP, "");
        assertHasNoNepoInfo(dropSensor);
    }

    @Test
    public void visitDropSensor_withoutPort() {
        DropSensor dropSensor = new DropSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitDropSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        DropSensor dropSensor = new DropSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(dropSensor);

        executeWorkflow();

        assertHasNepoInfo(dropSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitRfidSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor rfidSensor = new RfidSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(rfidSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.RFID, "");
        assertHasNoNepoInfo(rfidSensor);
    }

    @Test
    public void visitRfidSensor_onUnoWifiRev2() {
        setupRobotFactory("unowifirev2");

        configurationComponents.add(new ConfigurationComponent(SC.RFID, false, "P1", "P1", new HashMap<>()));

        RfidSensor rfidSensor = new RfidSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.WARNING, "BLOCK_NOT_SUPPORTED");
    }

    @Test
    public void visitRfidSensor_withoutPort() {
        RfidSensor rfidSensor = new RfidSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitRfidSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        RfidSensor rfidSensor = new RfidSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(rfidSensor);

        executeWorkflow();

        assertHasNepoInfo(rfidSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitHumiditySensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.HUMIDITY, false, "P1", "P1", new HashMap<>()));

        HumiditySensor humiditySensor = new HumiditySensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(humiditySensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.HUMIDITY, "");
        assertHasNoNepoInfo(humiditySensor);
    }

    @Test
    public void visitHumiditySensor_withoutPort() {
        HumiditySensor humiditySensor = new HumiditySensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitHumiditySensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        HumiditySensor humiditySensor = new HumiditySensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(humiditySensor);

        executeWorkflow();

        assertHasNepoInfo(humiditySensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitVoltageSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.POTENTIOMETER, false, "P1", "P1", new HashMap<>()));

        VoltageSensor voltageSensor = new VoltageSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(voltageSensor);

        Project project = executeWorkflow();

        assertHasUsedSensor(project, "P1", SC.VOLTAGE, "");
        assertHasNoNepoInfo(voltageSensor);
    }

    @Test
    public void visitVoltageSensor_withoutPort() {
        VoltageSensor voltageSensor = new VoltageSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitVoltageSensor_withWrongSensorType() {
        configurationComponents.add(new ConfigurationComponent(SC.MOISTURE, false, "P1", "P1", new HashMap<>()));

        VoltageSensor voltageSensor = new VoltageSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(voltageSensor);

        executeWorkflow();

        assertHasNepoInfo(voltageSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_WRONG");
    }

    @Test
    public void visitMotorOnAction() {
        configurationComponents.add(new ConfigurationComponent(SC.STEPMOTOR, true, "P1", "P1", new HashMap<>()));

        MotionParam motionParam = new MotionParam.Builder()
            .speed(new NumConst(null, "10"))
            .duration(new MotorDuration(null, new NumConst(null, "10")))
            .build();

        MotorOnAction motorOnAction = new MotorOnAction("P1", motionParam, bp);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    @Test
    public void visitMotorOnAction_withoutPort() {
        MotionParam motionParam = new MotionParam.Builder()
            .speed(new NumConst(null, "10"))
            .duration(new MotorDuration(null, new NumConst(null, "10")))
            .build();

        MotorOnAction motorOnAction = new MotorOnAction("P1", motionParam, bp);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_MOTOR_MISSING");
    }

    @Test
    public void visitMotorOnAction_withOtherType() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, true, "P1", "P1", new HashMap<>()));

        MotionParam motionParam = new MotionParam.Builder()
            .speed(new NumConst(null, "10"))
            .duration(new MotorDuration(null, new NumConst(null, "10")))
            .build();

        MotorOnAction motorOnAction = new MotorOnAction("P1", motionParam, bp);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNepoInfo(motorOnAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED");
    }

    @Test
    public void visitMotorOnAction_withoutDuration() {
        configurationComponents.add(new ConfigurationComponent(SC.STEPMOTOR, true, "P1", "P1", new HashMap<>()));

        MotionParam motionParam = new MotionParam.Builder()
            .speed(new NumConst(null, "10"))
            .build();

        MotorOnAction motorOnAction = new MotorOnAction("P1", motionParam, bp);
        phrases.add(motorOnAction);

        executeWorkflow();

        assertHasNoNepoInfo(motorOnAction);
    }

    @Test
    public void visitLightAction() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        RgbColor rgbColor = (RgbColor) new RgbColor(bp, new NumConst(null, "10"), new NumConst(null, "10"), new NumConst(null, "10"), new NumConst(null, "10"));

        LightAction lightAction = new LightAction("P1", BrickLedColor.ORANGE, LightMode.DEFAULT, rgbColor, bp);
        phrases.add(lightAction);

        executeWorkflow();

        assertHasNoNepoInfo(lightAction);
    }

    @Test
    public void visitLightAction_noActor() {
        RgbColor rgbColor = (RgbColor) new RgbColor(bp, new NumConst(null, "10"), new NumConst(null, "10"), new NumConst(null, "10"), new NumConst(null, "10"));

        LightAction lightAction = new LightAction("P1", BrickLedColor.ORANGE, LightMode.DEFAULT, rgbColor, bp);
        phrases.add(lightAction);

        executeWorkflow();

        assertHasNepoInfo(lightAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitPlayNoteAction() {
        configurationComponents.add(new ConfigurationComponent(SC.BUZZER, true, "P1", "P1", new HashMap<>()));

        PlayNoteAction playNoteAction = new PlayNoteAction(bp, "100", "100", "P1", null);
        phrases.add(playNoteAction);

        executeWorkflow();

        assertHasNoNepoInfo(playNoteAction);
    }

    @Test
    public void visitPlayNoteAction_noPort() {
        PlayNoteAction playNoteAction = new PlayNoteAction(bp, "100", "100", "no_port", null);
        phrases.add(playNoteAction);

        executeWorkflow();

        assertHasNepoInfo(playNoteAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitLightStatusActionOff() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        LightStatusAction lightStatusAction = new LightStatusAction("P1", LightStatusAction.Status.OFF, bp);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNoNepoInfo(lightStatusAction);
    }

    @Test
    public void visitLightStatusActionOff_noPort() {
        LightStatusAction lightStatusAction = new LightStatusAction("P1", LightStatusAction.Status.OFF, bp);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNepoInfo(lightStatusAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitLightStatusActionReset() {
        configurationComponents.add(new ConfigurationComponent(SC.RGBLED, true, "P1", "P1", new HashMap<>()));

        LightStatusAction lightStatusAction = new LightStatusAction("P1", LightStatusAction.Status.RESET, bp);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNoNepoInfo(lightStatusAction);
    }

    @Test
    public void visitLightStatusActionReset_noPort() {
        LightStatusAction lightStatusAction = new LightStatusAction("P1", LightStatusAction.Status.RESET, bp);
        phrases.add(lightStatusAction);

        executeWorkflow();
        assertHasNepoInfo(lightStatusAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitRelayAction() {
        configurationComponents.add(new ConfigurationComponent(SC.RELAY, true, "P1", "P1", new HashMap<>()));

        RelayAction relayAction = new RelayAction(bp, "P1", RelayMode.DEFAULT);
        phrases.add(relayAction);

        executeWorkflow();
        assertHasNoNepoInfo(relayAction);
    }

    @Test
    public void visitRelayAction_noPort() {
        RelayAction relayAction = new RelayAction(bp, "P1", RelayMode.DEFAULT);
        phrases.add(relayAction);

        executeWorkflow();
        assertHasNepoInfo(relayAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitShowTexAction() {
        configurationComponents.add(new ConfigurationComponent(SC.DISPLAY, true, "P1", "P1", new HashMap<>()));

        ShowTextAction showTextAction = new ShowTextAction(bp, new NumConst(null, "Text"), new NumConst(null, "0"), new NumConst(null, "0"), "P1", null);
        phrases.add(showTextAction);

        executeWorkflow();
        assertHasNoNepoInfo(showTextAction);
    }

    @Test
    public void visitPinGetValueSensor() {
        configurationComponents.add(new ConfigurationComponent(SC.OTHER, false, "P1", "P1", new HashMap<>()));

        PinGetValueSensor pinGetValueSensor = new PinGetValueSensor(bp, new ExternalSensorBean("P1", "", "", null));
        phrases.add(pinGetValueSensor);

        executeWorkflow();
        assertHasNoNepoInfo(pinGetValueSensor);
    }

    @Test
    public void visitPinWriteValueAction() {
        configurationComponents.add(new ConfigurationComponent(SC.DIGITAL_PIN, true, "P1", "P1", new HashMap<>()));

        Mutation mutation = new Mutation();
        PinWriteValueAction pinWriteValueAction = new PinWriteValueAction(bp, mutation, "1", "P1", new NumConst(null, "1"));
        phrases.add(pinWriteValueAction);

        executeWorkflow();
        assertHasNoNepoInfo(pinWriteValueAction);
    }

    @Test
    public void visitPinWriteValueAction_noPort() {
        Mutation mutation = new Mutation();
        PinWriteValueAction pinWriteValueAction = new PinWriteValueAction(bp, mutation, "1", "P1", new NumConst(null, "1"));
        phrases.add(pinWriteValueAction);

        executeWorkflow();
        assertHasNepoInfo(pinWriteValueAction, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_ACTOR_MISSING");
    }

    @Test
    public void visitIndexOfFunct() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        IndexOfFunct listSetIndex = new IndexOfFunct(IndexLocation.FIRST, param, bp);
        phrases.add(listSetIndex);

        executeWorkflow();
        assertHasNoNepoInfo(listSetIndex);
    }

    @Test
    public void visitIndexOfFunct_noFirsstElement() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "ListCreate "));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        IndexOfFunct listSetIndex = new IndexOfFunct(IndexLocation.FIRST, param, bp);
        phrases.add(listSetIndex);

        executeWorkflow();
        assertEquals((((NumConst) listSetIndex.param.get(0)).value), "0");
        assertHasNoNepoInfo(listSetIndex);
    }

    @Test
    public void visitListGetIndex() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        ListGetIndex listGetIndex = new ListGetIndex(ListElementOperations.GET, IndexLocation.FIRST, param, null, bp);
        phrases.add(listGetIndex);

        executeWorkflow();
        assertHasNoNepoInfo(listGetIndex);
    }

    @Test
    public void visitListGetIndex_noFirsstElement() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "ListCreate "));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        ListGetIndex listGetIndex = new ListGetIndex(ListElementOperations.GET, IndexLocation.FIRST, param, null, bp);
        phrases.add(listGetIndex);

        executeWorkflow();
        assertEquals((((NumConst) listGetIndex.param.get(0)).value), "0");
        assertHasNoNepoInfo(listGetIndex);
    }

    @Test
    public void visitLengthOfIsEmptyFunct() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        LengthOfIsEmptyFunct lengthOfIsEmptyFunct = new LengthOfIsEmptyFunct(FunctionNames.LIST_IS_EMPTY, param, bp);
        phrases.add(lengthOfIsEmptyFunct);

        executeWorkflow();
        assertHasNoNepoInfo(lengthOfIsEmptyFunct);
    }

    @Test
    public void visitLengthOfIsEmptyFunct_noFirsstElement() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "ListCreate "));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        LengthOfIsEmptyFunct lengthOfIsEmptyFunct = new LengthOfIsEmptyFunct(FunctionNames.LIST_IS_EMPTY, param, bp);
        phrases.add(lengthOfIsEmptyFunct);

        executeWorkflow();
        assertEquals((((NumConst) lengthOfIsEmptyFunct.param.get(0)).value), "0");
        assertHasNoNepoInfo(lengthOfIsEmptyFunct);
    }

    @Test
    public void visitMathOnListFunct() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        MathOnListFunct mathOnListFunct = new MathOnListFunct(FunctionNames.LIST_IS_EMPTY, param, bp);
        phrases.add(mathOnListFunct);

        executeWorkflow();
        assertHasNoNepoInfo(mathOnListFunct);
    }

    @Test
    public void visitMathOnListFunct_noFirstElement() {
        List<Expr> param = new ArrayList<>();
        param.add(new NumConst(null, "ListCreate "));
        param.add(new NumConst(null, "10"));
        param.add(new NumConst(null, "10"));

        MathOnListFunct mathOnListFunct = new MathOnListFunct(FunctionNames.LIST_IS_EMPTY, param, bp);
        phrases.add(mathOnListFunct);

        executeWorkflow();
        assertEquals((((NumConst) mathOnListFunct.param.get(0)).value), "0");
        assertHasNoNepoInfo(mathOnListFunct);
    }

}