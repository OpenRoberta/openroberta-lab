package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.INIBOVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class Rob3rtaStackMachineVisitor extends AbstractStackMachineVisitor implements INIBOVisitor<Void> {

    private int port;

    public Rob3rtaStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        String port = pinTouchSensor.getUserDefinedPort();
        String mode = pinTouchSensor.getMode();
        String slot = pinTouchSensor.getSlot();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + slot).put(C.MODE, mode.toLowerCase()).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        ledOnAction.ledColor.accept(this);
        int port;
        if ( ledOnAction.side.equals("Left") ) {
            port = 1;
        } else {
            port = 2;
        }
        JSONObject o = makeNode(C.LED_ON_ACTION).put(C.NAME, "rob3rta").put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction bodyLEDAction) {
        int port = 0;
        if ( bodyLEDAction.side.equals("LED_3") ) {
            port = 3;
        } else if ( bodyLEDAction.side.equals("LED_4") ) {
            port = 4;
        }
        if ( bodyLEDAction.ledState.equals("ON") ) {
            JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(0xFF, 0xFF, 0x99)));
            add(o);
            JSONObject oOn = makeNode(C.LED_ON_ACTION).put(C.NAME, "rob3rta").put(C.PORT, port);
            return add(oOn);
        } else if ( bodyLEDAction.ledState.equals("OFF") ) {
            JSONObject oOff = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, "rob3rta").put(C.PORT, port);
            return add(oOff);
        }
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction ledOffAction) {
        int port;
        if ( ledOffAction.side.equals("Left") ) {
            port = 1;
        } else {
            port = 2;
        }
        JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, "rob3rta").put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return add(o);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.LIGHT).put(C.MODE, C.AMBIENTLIGHT).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitRememberAction(RememberAction rememberAction) {
        rememberAction.code.accept(this);
        JSONObject o = makeNode(C.REMEMBER).put(C.NAME, "rob3rta");
        return add(o);
    }

    @Override
    public Void visitRecallAction(RecallAction recallAction) {
        JSONObject o = makeNode(C.RECALL).put(C.NAME, "rob3rta");
        return add(o);
    }

    public Void visitSendIRAction(SendIRAction sendIRAction) {
        throw new DbcException("not supported");
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        throw new DbcException("not supported");
    }

    @Override
    public Void visitCodePadSensor(CodePadSensor codePadSensor) {
        throw new DbcException("not supported");
    }

}
