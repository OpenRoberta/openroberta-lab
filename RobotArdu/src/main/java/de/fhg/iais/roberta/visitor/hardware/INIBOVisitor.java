package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface INIBOVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitTemperatureSensor(TemperatureSensor temperatureSensor);

    V visitMainTask(MainTask mainTask);

    V visitLedOnAction(LedOnAction ledOnAction);

    V visitLedOffAction(LedOffAction ledOffAction);

    V visitBodyLEDAction(BodyLEDAction bodyLEDAction);

    V visitSendIRAction(SendIRAction sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction receiveIRAction);

    V visitRememberAction(RememberAction rememberAction);

    V visitRecallAction(RecallAction recallAction);

    V visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct);

    V visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct);

    V visitDebugAction(DebugAction debugAction);

    V visitAssertStmt(AssertStmt assertStmt);

    V visitPinTouchSensor(PinTouchSensor pinTouchSensor);

    V visitCodePadSensor(CodePadSensor codePadSensor);
}
