package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;

public class CalliopeStackMachineVisitor extends MbedStackMachineVisitor implements ICalliopeVisitor<Void> {

    public CalliopeStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration, phrases);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, "calliope");
        return add(o);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.param.getSpeed().accept(this);
        add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));

        String port = motorOnAction.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = ((cc == null) || cc.componentType.equals("CALLIBOT")) ? "0" : cc.getProperty("PIN1");

        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, pin1.toLowerCase()).put(C.NAME, pin1.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(port);
        String pin1 = ((cc == null) || cc.componentType.equals("CALLIBOT")) ? "0" : cc.getProperty("PIN1");

        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, pin1.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "calliope");
        return add(o);
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        ledOnAction.ledColor.accept(this);
        JSONObject o = makeNode(C.LED_ON_ACTION);
        return add(o);
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        displaySetBrightnessAction.brightness.accept(this);
        JSONObject o = makeNode(C.DISPLAY_SET_BRIGHTNESS_ACTION);
        return add(o);
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.DISPLAY).put(C.MODE, C.BRIGHTNESS).put(C.NAME, "calliope");
        return add(o);
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        bothMotorsOnAction.speedA.accept(this);
        bothMotorsOnAction.speedB.accept(this);

        String portA = bothMotorsOnAction.portA;
        ConfigurationComponent ccA = this.configuration.optConfigurationComponent(portA);
        String pin1A = ((ccA == null) || ccA.componentType.equals("CALLIBOT")) ? "0" : ccA.getProperty("PIN1");
        String portB = bothMotorsOnAction.portB;
        ConfigurationComponent ccB = this.configuration.optConfigurationComponent(portB);
        String pin1B = ((ccB == null) || ccB.componentType.equals("CALLIBOT")) ? "0" : ccB.getProperty("PIN1");

        JSONObject o = makeNode(C.BOTH_MOTORS_ON_ACTION).put(C.PORT_A, pin1A.toLowerCase()).put(C.PORT_B, pin1B.toLowerCase());

        return add(o);
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, "ab");
        return add(o);
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        return null;
    }
}
